package me.lgbt.pubhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import me.lgbt.pubhub.chat.ChatClickListener;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.trivia.creation.QuestionListActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements ChatClickListener {
    private Button start;
    private TextView output;
    private OkHttpClient client;
    private String phbToken;
    private WebSocket ws;
    private String textFromFragment;

    @Override
    public void clicked(String data) {
        textFromFragment = data;
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("MessageToServer: " + "test successful");
            webSocket.send("{\"messageType\":\"ClientServerChatMessage\",\"payload\": {\"message\":\"chat connection success <3\"}}");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println("MessageFromServer: " + text);
            try {
                JSONObject messageObject = new JSONObject(text);
                String messageType = messageObject.getString("messageType");
                if (messageType.equals("ServerClientChatMessage")) {
                    JSONArray messages = messageObject.getJSONObject("payload").getJSONArray("messages");
                    for (int i = 0; i < messages.length(); i++) {
                        JSONObject message = messages.getJSONObject(i);
                        String user = message.getString("user");
                        String messageString = message.getString("message");
                        String displayMessage = user + ": " + messageString;
                        output(displayMessage);
                    }
                }
            }catch(org.json.JSONException e) {
                System.out.println(e.getMessage());
            }
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving bytes : " + bytes.hex());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            output("Closing : " + code + " / " + reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error : " + t.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity onCreate successful");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unPack();

        System.out.println("Unpack successful");
        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        client = new OkHttpClient();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start();
            }
        });
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
        }
    }

    private void start() {
        System.out.print("I made it to start.");
        Intent nextActivity = new Intent(this, MainActivity.class);
        Bundle extras = new Bundle();
        extras.putString(IntentKeys.PUBHUB, phbToken);

        String authHeader = "Bearer " + phbToken;
        System.out.println("Auth token: " + authHeader);

        Request request = new Request.Builder().url("ws://pubhub.me:8082").addHeader("Authorization", authHeader).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }
}