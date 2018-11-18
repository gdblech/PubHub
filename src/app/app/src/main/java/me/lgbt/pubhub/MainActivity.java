package me.lgbt.pubhub;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;

import me.lgbt.pubhub.chat.ChatClickListener;
import me.lgbt.pubhub.chat.UserMessage;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.Websockets.ClientChatMessage;
import me.lgbt.pubhub.main.ChatFragment;
import me.lgbt.pubhub.main.HostFragment;
import me.lgbt.pubhub.main.PlayFragment;
import me.lgbt.pubhub.main.ScoreFragment;
import me.lgbt.pubhub.main.TeamFragment;
import me.lgbt.pubhub.main.WaitingOpenFragment;
import me.lgbt.pubhub.trivia.utils.HostListener;
import me.lgbt.pubhub.trivia.utils.PlayListener;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity implements ChatClickListener, BottomNavigationView.OnNavigationItemSelectedListener, PlayListener, HostListener {
    private OkHttpClient client;
    private String phbToken;
    private WebSocket ws;
    private String textFromFragment;

    private ChatFragment chatFrag;
    private Fragment triviaFrag;
    private ScoreFragment scoreFrag;
    private TeamFragment teamFrag;
    private Fragment active;
    private Fragment currentTriv;
    private BottomNavigationView navBar;
    private String playAnswer;
    private FragmentManager manager;
    private boolean hosting = true;
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
            if (hosting) {
                triviaFrag = new HostFragment();
            } else {
                triviaFrag = new PlayFragment();
            }


            scoreFrag = new ScoreFragment();
            teamFrag = new TeamFragment();
            active = waiting;

            manager.beginTransaction().add(R.id.fragContainer, waiting).commit(); //change me to fragment you want to test
            manager.beginTransaction().add(R.id.fragContainer, chatFrag).hide(chatFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, triviaFrag).hide(triviaFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, teamFrag).hide(teamFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, scoreFrag).hide(scoreFrag).commit();
        }

        client = new OkHttpClient();
        websocketConnectionOpen();
        openGame();
    }

    private void unPack() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            phbToken = data.getString(IntentKeys.PUBHUB);
            gameID = data.getInt(IntentKeys.GAMEID);
            hosting = data.getBoolean(IntentKeys.HOST);
        }
    }

    private void websocketConnectionOpen() {
        String authHeader = "Bearer " + phbToken;
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
                manager.beginTransaction().hide(active).show(triviaFrag).commit();
                active = triviaFrag;
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
        playAnswer = data;
        //todo get answer and send to team/grading
    }

    //host navigates either to the next or previous slide
    @Override
    public void slideNavClicked(int button) {
        switch (button) {
            case HostFragment.START: {
                break; //todo
            }
            case HostFragment.PREVIOUS: {
                break; //todo
            }
            case HostFragment.NEXT: {
                break; //todo
            }
        }
    }

    //update the info on the current slide
    private void updateUI(TriviaMessage msg) {
        if (hosting) {
            ((HostFragment) triviaFrag).setSlide(msg);
        } else {
            ((PlayFragment) triviaFrag).setSlide(msg);
        }
    }


    //being the game
    private void startGame() {
        if (hosting) {
            ((HostFragment) triviaFrag).startGame();
        } else {
            ((PlayFragment) triviaFrag).startGame();
        }
    }
    /*
     * End Playing Fragment Control Code
     */

    /*
     * Opens game so that teams can be created and joined.
     * Tells server that game has started.
     * Game ID is from the TriviaGameListActivity.sendMessagePlay(int id) method.
     */
    private void openGame() {
        System.out.println("Game ID on openGame(): " + gameID);
        System.out.println(gameID + " has started.");

        String startGameJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"OpenGame\",\"payload\":{\"gameId\":" + gameID + "}}}";

        // send to server
        System.out.println("openGame JSON: " + startGameJSON);
        ws.send(startGameJSON);
    }

    /* Tells server that game has ended. */
    private void closeGame() {
        String endGameJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"EndGame\"}}";

        // send to server
        System.out.println("endGame JSON: " + endGameJSON);
        ws.send(endGameJSON);
        System.out.println("Game " + gameID + "has ended.");
    }

    private final class EchoWebSocketListener extends WebSocketListener {

        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("Websocket Connection Success");
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
                    }
                } else if (messageType.equals("ServerHostMessage")) {
                    JSONObject payload = messageObject.getJSONObject("payload");
                    String subMessageType = payload.getString("messageType");
                    //TODO finish code or omit and implement REST API

                } else if (messageType.equals("ServerPlayerMessage")) {
                    JSONObject payload = messageObject.getJSONObject("payload");
                    String subMessageType = payload.getString("messageType");

                    String qtext;
                    String qrcode;
                    String teamName;

                    switch (subMessageType) {
                        case "GameInfo":
                            String title = messageObject.getString("title");
                            qtext = messageObject.getString("text");
                            String qpicture = messageObject.getString("picture");
                            Uri picture = Uri.parse(qpicture);
                            TriviaMessage triviaMessage = new TriviaMessage(title, text, picture);
                            break;

                        case "TableStatusResponse":
                            qrcode = messageObject.getString("QRCode");
                            String status = messageObject.getString("status");
                            teamName = messageObject.getString("teamName");
                            String teamLeader = messageObject.getString("teamLeader");
                            break;
                        case "CreateTeamResponse":
                            String isTeamCreated = messageObject.getString("success");
                            qrcode = messageObject.getString("QRCode");
                            teamName = messageObject.getString("teamName");
                            if (messageObject.getString("reason") != null) {
                                String reason = messageObject.getString("reason");
                            }
                            break;
                        case "JoinTeamResponse":
                            String joinedTeam = messageObject.getString("success");
                            qrcode = messageObject.getString("QRCode");
                            teamName = messageObject.getString("teamName");
                            if (messageObject.getString("reason") != null) {
                                String reason = messageObject.getString("reason");
                            }
                            break;
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