package me.lgbt.pubhub;
//todo make nav bar not move with resize as the pan method.

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import me.lgbt.pubhub.chat.ChatClickListener;
import me.lgbt.pubhub.chat.UserMessage;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.Websockets.ClientChatMessage;
import me.lgbt.pubhub.main.ChatFragment;
import me.lgbt.pubhub.main.PlayFragment;
import me.lgbt.pubhub.main.ScoreFragment;
import me.lgbt.pubhub.main.TeamFragment;
import me.lgbt.pubhub.main.WaitingOpenFragment;
import me.lgbt.pubhub.trivia.utils.PlayListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity implements ChatClickListener, BottomNavigationView.OnNavigationItemSelectedListener, PlayListener {
    public final int NEXT = 1;
    public final int PREVIOUS = -1;
    public final int START = 0;
    private OkHttpClient client;
    private String phbToken;
    private WebSocket ws;
    private String textFromFragment;
    private ChatFragment chatFrag;
    private PlayFragment playFrag;
    private ScoreFragment scoreFrag;
    private TeamFragment teamFrag;
    private BottomNavigationView navBar;
    private Fragment active;
    private FragmentManager manager;
    private boolean hosting = false;
    private WaitingOpenFragment waiting;
    private int gameID;

    @Override
    public void clicked(String data) {
        textFromFragment = data;
        ClientChatMessage message = new ClientChatMessage(data);
        ws.send(message.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("MainActivity onCreate successful");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navBar = findViewById(R.id.navigation);
        navBar.setOnNavigationItemSelectedListener(this);
        manager = getSupportFragmentManager();

        unPack();

        if (findViewById(R.id.fragContainer) != null) {

            if (savedInstanceState != null) {
                return;
            }
            waiting = new WaitingOpenFragment();
            chatFrag = new ChatFragment();
            playFrag = new PlayFragment();
            scoreFrag = new ScoreFragment();
            teamFrag = new TeamFragment();
            active = waiting;

            manager.beginTransaction().add(R.id.fragContainer, waiting).commit(); //change me to fragment you want to test
            manager.beginTransaction().add(R.id.fragContainer, chatFrag).hide(chatFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, playFrag).hide(playFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, teamFrag).hide(teamFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, scoreFrag).hide(scoreFrag).commit();

        }

        isHost();
        client = new OkHttpClient();
        start();
    }

    public void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            gameID = data.getInt(IntentKeys.GAMEID);
            if(data.getBoolean(IntentKeys.HOST)){
                hosting = true;
            }
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

    private void output(final UserMessage mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatFrag.addMessage(mes);
            }
        });
    }

    private void output(final String mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //chatFrag.addMessage(mes);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navigation_chat: {
                manager.beginTransaction().hide(active).show(chatFrag).commit();
                active = chatFrag;
                return true;
            }
            case R.id.navigation_scores: {
                manager.beginTransaction().hide(active).show(scoreFrag).commit();
                active = scoreFrag;
                return true;
            }
            case R.id.navigation_team: {
                manager.beginTransaction().hide(active).show(teamFrag).commit();
                active = teamFrag;
                return true;
            }
            case R.id.navigation_trivia: {
                manager.beginTransaction().hide(active).show(playFrag).commit();
                active = playFrag;
                return true;
            }
        }
        return false;
    }

    /*
     * Start Playing Fragment control code
     */
    //a customer sends in an answer
    @Override
    public void answerClicked(String data) {
        //todo get answr and send to team/grading
    }

    //host navigates either to the next or previous slide
    @Override
    public void slideNavClicked(int button) {
        switch(button){
            case START:{
                 break; //todo
            }
            case PREVIOUS:{
                break; //todo
            }
            case NEXT:{
                break; //todo
            }
        }
    }

    //if the user if a host, change the play fragment to host mode
    private void isHost() {
        if (hosting) {
            playFrag.hostMode();
        }
    }

    //being the game
    private void startGame() {
        playFrag.startGame();
    }
    /*
     * End Playing Fragment Control Code
     */


    private final class EchoWebSocketListener extends WebSocketListener {


        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("MessageToServer: " + "test successful");
//            webSocket.send("{\"messageType\":\"ClientServerChatMessage\",\"payload\": {\"message\":\"chat connection success <3\"}}");
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
                        String timeStamp = message.getString("timestamp");
                        UserMessage mes = new UserMessage(messageString, user, timeStamp);
                        output(mes);
                        // String displayMessage = user + ": " + messageString;
                        // output(displayMessage);
                    }
                }
            } catch (org.json.JSONException e) {
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
}