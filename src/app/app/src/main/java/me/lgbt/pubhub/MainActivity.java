package me.lgbt.pubhub;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.lgbt.pubhub.interfaces.ChatClickListener;
import me.lgbt.pubhub.chat.UserMessage;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.Websockets.ClientChatMessage;
import me.lgbt.pubhub.interfaces.GradingListener;
import me.lgbt.pubhub.interfaces.JoinTeamListener;
import me.lgbt.pubhub.interfaces.TeamNameCreatedListenser;
import me.lgbt.pubhub.main.ChatFragment;
import me.lgbt.pubhub.main.CreateTeam;
import me.lgbt.pubhub.main.GradingFragment;
import me.lgbt.pubhub.main.HostFragment;
import me.lgbt.pubhub.main.JoinTeam;
import me.lgbt.pubhub.main.PlayFragment;
import me.lgbt.pubhub.main.ScoreFragment;
import me.lgbt.pubhub.main.TeamFragment;
import me.lgbt.pubhub.main.WaitingOpenFragment;
import me.lgbt.pubhub.trivia.start.HostOptionsActivity;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;
import me.lgbt.pubhub.interfaces.HostListener;
import me.lgbt.pubhub.interfaces.PlayListener;
import me.lgbt.pubhub.interfaces.TeamAnswerListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity implements ChatClickListener,
        BottomNavigationView.OnNavigationItemSelectedListener, PlayListener, HostListener, TeamAnswerListener,
        JoinTeamListener, TeamNameCreatedListenser, GradingListener {
    final static int NOGAME = 0; //waiting for open
    final static int NOTONTEAM = 1; // JoinTeam
    final static int TEAMNOEXIST = 2; //Create team
    final static int PLAYING = 3; //if on a team and ready to play
    final static int GRADING = 4; //if grading is now active

    private int triviaTracker = -1;
    private OkHttpClient client;
    private String phbToken;
    private WebSocket ws;

    private ChatFragment chatFrag;
    private Fragment triviaFrag;
    private ScoreFragment scoreFrag;
    private TeamFragment teamFrag;
    private CreateTeam createTeam;
    private JoinTeam joinTeam;
    private WaitingOpenFragment waiting;
    private GradingFragment grading;

    private Fragment active;
    private Fragment currentTriv;

    private BottomNavigationView navBar;
    private String playAnswer;
    private FragmentManager manager;
    private boolean hosting = false;
    private int gameID;
    private String qrCode = "";

    /**
     * Handles when a chat message is sent from the chat fragment
     * @param data the message the use wants to send
     */
    @Override
    public void clicked(String data) {
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
            createTeam = new CreateTeam();
            joinTeam = new JoinTeam();
            waiting = new WaitingOpenFragment();
            chatFrag = new ChatFragment();
            if (hosting) {
                triviaFrag = new HostFragment();
            } else {
                triviaFrag = new PlayFragment();
            }

            scoreFrag = new ScoreFragment();
            teamFrag = new TeamFragment();
            grading = new GradingFragment();
            active = chatFrag;


            manager.beginTransaction().add(R.id.fragContainer, chatFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, waiting).hide(waiting).commit();
            manager.beginTransaction().add(R.id.fragContainer, grading).hide(grading).commit();
            manager.beginTransaction().add(R.id.fragContainer, createTeam).hide(createTeam).commit();
            manager.beginTransaction().add(R.id.fragContainer, joinTeam).hide(joinTeam).commit();
            manager.beginTransaction().add(R.id.fragContainer, triviaFrag).hide(triviaFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, teamFrag).hide(teamFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, scoreFrag).hide(scoreFrag).commit();

            if(hosting){
                triviaTracker = PLAYING;
                trivSwitcher();
            }else{
                triviaTracker = NOGAME;
                trivSwitcher();
            }
        }
        navBar.setSelectedItemId(R.id.navigation_chat);
        client = new OkHttpClient();
        websocketConnectionOpen();

        if (gameID != -1) {
            openGame();
        }
    }

    private void trivSwitcher(){
        if (currentTriv != null) {
            manager.beginTransaction().hide(currentTriv).commit();
        }
        switch (triviaTracker){
            case NOGAME:
                currentTriv = waiting;
                break;
            case NOTONTEAM:
                currentTriv = joinTeam;
                break;
            case TEAMNOEXIST:
                currentTriv = createTeam;
                break;
            case PLAYING:
                currentTriv = triviaFrag;
                break;
            case GRADING:
                currentTriv = grading;
                break;
        }
        if(navBar.getSelectedItemId() == R.id.navigation_trivia){
            manager.beginTransaction().show(currentTriv).commit();
        }
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

        Resources res = getResources();
        String ws_url;
        if (res.getBoolean(R.bool.backend)) {
            ws_url = getString(R.string.testingBackendWS);
        } else {
            ws_url = getString(R.string.phb_ws);
        }
        Request request = new Request.Builder().url(ws_url).addHeader("Authorization", authHeader).build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    /*
     * Start MainActivity click controllers
     */

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
                manager.beginTransaction().hide(active).show(currentTriv).commit();
                active = currentTriv;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (hosting && gameID != -1){
            closeGame();
            Intent nextActivity = new Intent(this, HostOptionsActivity.class);
            Bundle extras = new Bundle();
            extras.putString(IntentKeys.PUBHUB, phbToken);
            extras.putInt(IntentKeys.GAMEID, -1);
            nextActivity.putExtras(extras);
            startActivity(nextActivity);
            finish();
        }else{
            //todo add back button for players
        }
    }

    /*
     * End MainActivity click controllers
     *
     * Start Playing Fragment control code
     */

    /**
     * Gets the answer from the customer when press the submit button.
     * @param data the answer returned from the customer
     */
    @Override
    public void answerClicked(String data) {
        playAnswer = data;
        //todo get answer and send to team/grading
    }

    /**
     * gets the button push from the host fragment.
     * @param button which button was pushed, see final ints for more info.
     */
    @Override
    public void slideNavClicked(int button) {
        switch (button) {
            case HostFragment.START: {
                String startTriviaJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"StartTrivia\"}}";
                // send to server
                System.out.println("Start Trivia JSON: " + startTriviaJSON);
                ws.send(startTriviaJSON);
                break; //todo
            }
            case HostFragment.PREVIOUS: {

                break;
            }
            case HostFragment.NEXT: {
                String nextJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"Next\"}}";
                ws.send(nextJSON);
                break; //todo
            }
        }
    }

    /**
     * Updates the playing of hosting fragments with the current slide from the server.
     * @param msg contains the message from the server to be displayed
     */
    private void updateUI(TriviaMessage msg) {
        if (hosting) {
            ((HostFragment) triviaFrag).setSlide(msg);
        } else {
            ((PlayFragment) triviaFrag).setSlide(msg);
        }
    }

    /**
     * update the UI on the grading fragment
     * @param msg trivia slide to display
     * @param answerGiven answer given by the server to check against
     * @param answer the answer given by the team
     */
    private void updateUI(TriviaMessage msg, String answerGiven, String answer ){
        if(!hosting){
            grading.updateUI(msg, answerGiven, answer);
        }
    }

    /**
     *  changes the trivia fragment from lockout mode to playMode
     * @param hos whether the user is a host or not
     */
    private void startGame(final boolean hos) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!hos) {
                    ((PlayFragment) triviaFrag).startGame();
                }
            }
        });
    }

    /**
     *  Takes the qrCode scanned by the user and sends it to the server
     * @param qrCode the code scanned by the user from the table
     */
    @Override
    public void qrCodeScanned(String qrCode){
        this.qrCode = qrCode;
        String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"JoinTeam\",\"payload\":{\"QRCode\":\"" + qrCode + "\"}}}";
        ws.send(startGameJSON);
    }

    /**
     *  gets the team name from the customer and sends it to the server
     * @param teamName the name chosen by the customer for their team
     */
    @Override
    public void nameChosen(String teamName) {
        String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"CreateTeam\",\"payload\":{\"QRCode\": \"" + qrCode + "\",\"teamName\":\""+ teamName +"\"}}}";
        ws.send(startGameJSON);
    }

    /**
     * if the user is already on the team updates the UI to tell them so they scanned the QR code for the wrong team.
     */
    private void wrongTeam() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                joinTeam.upDateText("Wrong table, Please Try Again");
            }
        });
    }

    /**
     * Get the whether the answer is right or wrong
     * @param grade true for correct answer, false for wrong
     */
    @Override
    public void answerGraded(boolean grade) {
        //todo change message parameters
        //String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"CreateTeam\",\"payload\":{\"QRCode\": \"" + qrCode + "\",\"teamName\":\""+ teamName +"\"}}}";
        //ws.send(startGameJSON);
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
        String startGameJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"OpenGame\",\"payload\":{\"gameId\":" + gameID + "}}}";
        ws.send(startGameJSON);
    }

    /* Tells server that game has ended. */
    private void closeGame() {
        String endGameJSON = "{\"messageType\":\"HostServerMessage\",\"payload\":{\"messageType\":\"EndGame\"}}";
        ws.send(endGameJSON);
    }

    @Override
    public void teamAnswerChosen(String answer) {
        //todo send team's answer to backend
    }

    private void output(final UserMessage mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatFrag.addMessage(mes);
            }
        });
    }

    private void output(final TriviaMessage mes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUI(mes);
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

    private Context getContext(){
        return this;
    }


    private final class EchoWebSocketListener extends WebSocketListener {


        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("Websocket Connection Success");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            //System.out.println(text);
            try {
                JSONObject messageObject = new JSONObject(text);
                JSONObject payloadJSON = messageObject.getJSONObject("payload");

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
                } else {
                    String subMessageType = payloadJSON.getString("messageType");
                    JSONObject subPayloadJSON = payloadJSON.getJSONObject("payload");

                    if (messageType.equals("ServerHostMessage")) {
                        String qtitle;
                        String qtext;
                        String qimage;
                        TriviaMessage triviaMessage;
                        if(subMessageType.equals("Grading")){
                            //todo handle grading
                        }else {
                            output(extract(subPayloadJSON));
                        }
                    } else if (messageType.equals("ServerPlayerMessage")) {
                        String title;
                        String qtext;
                        String qrcode;
                        String qimage;
                        String teamName;
                        TriviaMessage triviaMessage;

                        switch (subMessageType) {
                            case "CreateTeamResponse":
                                boolean isTeamCreated = subPayloadJSON.getBoolean("success");

                                if(isTeamCreated){
                                    triviaTracker = PLAYING;
                                    trivSwitcher();
                                }else{
                                    String reasonTC = subPayloadJSON.getString("reason");
                                    if(reasonTC.equals("Team already exists for table")){
                                        Toast.makeText( getContext(), "A Team Name Is Required", Toast.LENGTH_LONG).show();
                                    }
                                }
                                break;
                            case "TableStatusResponse":
                                break;
                            case "JoinTeamResponse":
                                boolean success = subPayloadJSON.getBoolean("success");

                                if(success){
                                    triviaTracker = PLAYING;
                                    trivSwitcher();
                                }else{
                                    String reason = subPayloadJSON.getString("reason");
                                    if(reason.equals("User already belongs to a team")){
                                        wrongTeam();
                                    }else{
                                        triviaTracker = TEAMNOEXIST;
                                        trivSwitcher();
                                    }
                                }
                                break;
                            case "GameInfo":
                                JSONObject gameJSON = subPayloadJSON.getJSONObject("game");
//                                if(subPayloadJSON.getBoolean("onTeam")){ //todo
                                if(false) {
                                    triviaTracker = PLAYING;
                                    trivSwitcher();
                                }else if(!hosting){
                                    triviaTracker = NOTONTEAM;
                                    trivSwitcher();
                                }
                                output(extract(gameJSON));
                                break;
                            case "TriviaStart":
                                startGame(hosting);
                            case "RoundStart":
                                output(extract(subPayloadJSON));
                                break;
                            case "Question":
                                triviaMessage = extract(subPayloadJSON);
                                triviaMessage.isQuestion(true);
                                output(triviaMessage);
                                break;
                            case "AnswerSubmission":
                                break;
                            case "FinalAnswerResponse":
                                break;
                            case "Grading":
                                break;
                        }
                    }
                }
            } catch (org.json.JSONException e) {
                System.out.println(e.getMessage());
            }
        }

        private TriviaMessage extract(JSONObject Payload) throws JSONException {
            String title = Payload.getString("title");
            String text = Payload.getString("text");
            String image = Payload.getString("image");

            return new TriviaMessage(title, text, image);
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
            Log.d("WS OnFailure", t.getMessage(), t);
        }

    }
}