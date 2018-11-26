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

import me.lgbt.pubhub.chat.UserMessage;
import me.lgbt.pubhub.connect.IntentKeys;
import me.lgbt.pubhub.connect.Websockets.ClientChatMessage;
import me.lgbt.pubhub.interfaces.ChatClickListener;
import me.lgbt.pubhub.interfaces.GradingListener;
import me.lgbt.pubhub.interfaces.HostListener;
import me.lgbt.pubhub.interfaces.JoinTeamListener;
import me.lgbt.pubhub.interfaces.PlayListener;
import me.lgbt.pubhub.interfaces.TeamAnswerListener;
import me.lgbt.pubhub.interfaces.TeamNameCreatedListenser;
import me.lgbt.pubhub.main.ChatFragment;
import me.lgbt.pubhub.main.CreateTeam;
import me.lgbt.pubhub.main.GradingFragment;
import me.lgbt.pubhub.main.HostFragment;
import me.lgbt.pubhub.main.JoinTeam;
import me.lgbt.pubhub.main.PlayFragment;
import me.lgbt.pubhub.main.ScoreFragment;
import me.lgbt.pubhub.main.TeamAnswerFragment;
import me.lgbt.pubhub.main.TeamFragment;
import me.lgbt.pubhub.main.WaitingOpenFragment;
import me.lgbt.pubhub.trivia.start.HostOptionsActivity;
import me.lgbt.pubhub.trivia.utils.Answer;
import me.lgbt.pubhub.trivia.utils.TriviaMessage;
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
    final static int TEAMANSWER = 4; //if team lead, you can chose team answer.
    final static int GRADING = 5; //if grading is now active

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
    private TeamAnswerFragment teamAnswer;
    private WaitingOpenFragment waiting;
    private GradingFragment grading;

    private Fragment active;
    private Fragment currentTriv;

    private BottomNavigationView navBar;
    private FragmentManager manager;
    private boolean hosting = false;
    private int gameID;
    private boolean teamLead = false;
    private String qrCode = "";
    private int currentRound = -1;
    private int currentQuestion = -1;

    /**
     * Handles when a chat message is sent from the chat fragment
     *
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
            teamAnswer = new TeamAnswerFragment();
            active = chatFrag;

            manager.beginTransaction().add(R.id.fragContainer, chatFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, waiting).hide(waiting).commit();
            manager.beginTransaction().add(R.id.fragContainer, teamAnswer).hide(teamAnswer).commit();
            manager.beginTransaction().add(R.id.fragContainer, grading).hide(grading).commit();
            manager.beginTransaction().add(R.id.fragContainer, createTeam).hide(createTeam).commit();
            manager.beginTransaction().add(R.id.fragContainer, joinTeam).hide(joinTeam).commit();
            manager.beginTransaction().add(R.id.fragContainer, triviaFrag).hide(triviaFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, teamFrag).hide(teamFrag).commit();
            manager.beginTransaction().add(R.id.fragContainer, scoreFrag).hide(scoreFrag).commit();

            if (hosting) {
                triviaTracker = PLAYING;
                trivSwitcher();
            } else {
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

    private void trivSwitcher() {
        if (currentTriv != null) {
            manager.beginTransaction().hide(currentTriv).commit();
        }
        switch (triviaTracker) {
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
            case TEAMANSWER:
                currentTriv = teamAnswer;
                break;
        }
        if (navBar.getSelectedItemId() == R.id.navigation_trivia) {
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

    /**
     * On back button press, takes host back to host option screen
     */
    @Override
    public void onBackPressed() {
        if (hosting && gameID != -1) {
            closeGame();
            Intent nextActivity = new Intent(this, HostOptionsActivity.class);
            Bundle extras = new Bundle();
            extras.putString(IntentKeys.PUBHUB, phbToken);
            extras.putInt(IntentKeys.GAMEID, -1);
            nextActivity.putExtras(extras);
            startActivity(nextActivity);
            finish();
        } else {
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
     *
     * @param data the answer returned from the customer
     */
    @Override
    public void answerClicked(String data) {
        String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"AnswerSubmission\",\"payload\":{\"roundNumber\": " + currentRound + ",\"questionNumber\": " + currentQuestion + ",\"answer\":\"" + data + "\"}}}";
        ws.send(startGameJSON);
        if (teamLead) {
            triviaTracker = TEAMANSWER;
            trivSwitcher();
        }
    }

    /**
     * gets the button push from the host fragment.
     *
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
     *
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
     *
     * @param msg         trivia slide to display
     * @param answerGiven answer given by the server to check against
     */
    private void updateUI(TriviaMessage msg, String answerGiven) {
        if (!hosting) {
            grading.updateUI(msg, answerGiven);
        }
    }

    /**
     * adds the list of answers to the grading fragment
     *
     * @param answers array of answersto be graded
     */
    private void answerRunner(Answer[] answers) {
        if (!hosting) {
            grading.answerList(answers);
        }
    }

    /**
     * changes the trivia fragment from lockout mode to playMode
     *
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
     * Takes the qrCode scanned by the user and sends it to the server
     *
     * @param qrCode the code scanned by the user from the table
     */
    @Override
    public void qrCodeScanned(String qrCode) {
        this.qrCode = qrCode;
        String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"JoinTeam\",\"payload\":{\"QRCode\":\"" + qrCode + "\"}}}";
        ws.send(startGameJSON);
    }

    /**
     * gets the team name from the customer and sends it to the server
     *
     * @param teamName the name chosen by the customer for their team
     */
    @Override
    public void nameChosen(String teamName) {
        String gameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"CreateTeam\",\"payload\":{\"QRCode\": \"" + qrCode + "\",\"teamName\":\"" + teamName + "\"}}}";
        ws.send(gameJSON);
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
     *
     * @param answers answer array thath as been graded
     */
    @Override
    public void answerGraded(Answer[] answers) {
        JSONObject obj = new JSONObject();
        JSONArray ans = new JSONArray();
        for (int i = 0; i < answers.length; i++) {
            JSONObject ansObj = new JSONObject();
            try {
                ansObj.put("teamId", answers[i].getTeamID());
                ansObj.put("correct", answers[i].isCorrect());
                ans.put(ansObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            obj.put("teamGrades", ans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String gameJSON = "{\"messageType\": \"PlayerServerMessage\",\"payload\": {\"messageType\": \"Grading\",\"payload\": {\"questionNumber\": " + currentQuestion + ",\"roundNumber\": " + currentRound;
        gameJSON = gameJSON + obj.toString();
        ws.send(gameJSON);
    }

    @Override
    public void teamAnswerChosen(String answer) {
        String startGameJSON = "{\"messageType\":\"PlayerServerMessage\",\"payload\":{\"messageType\":\"FinalAnswer\",\"payload\":{\"answer\":\"" + answer + "\"}}}";
        ws.send(startGameJSON);
    }

    /*
     * End Playing Fragment Control Code
     */

    /**
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

    private Context getContext() {
        return this;
    }

    private Answer[] gradeExtractor(JSONArray array) throws JSONException {
        int length = array.length();
        Answer[] answers = new Answer[length];
        for (int i = 0; i < length; i++) {
            JSONObject obj = array.getJSONObject(i);
            answers[i] = new Answer(obj.getString("answer"), obj.getInt("teamId"));
        }
        return answers;
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

                        if (subMessageType.equals("Grading")) {
                            //todo handle grading
                        } else {
                            output(extract(subPayloadJSON));
                        }
                    } else if (messageType.equals("ServerPlayerMessage")) {
                        String title;
                        String qtext;
                        String qrcode;
                        String qimage;
                        String teamName;
                        int currentQuestion;

                        TriviaMessage triviaMessage;

                        switch (subMessageType) {
                            case "CreateTeamResponse":
                                boolean isTeamCreated = subPayloadJSON.getBoolean("success");

                                if (isTeamCreated) {
                                    triviaTracker = PLAYING;
                                    teamLead = true;
                                    trivSwitcher();
                                } else {
                                    String reasonTC = subPayloadJSON.getString("reason");
                                    if (reasonTC.equals("Team already exists for table")) {
                                        Toast.makeText(getContext(), reasonTC, Toast.LENGTH_LONG).show();
                                    }
                                }
                                break;
                            case "TableStatusResponse":
                                break;
                            case "JoinTeamResponse":
                                boolean success = subPayloadJSON.getBoolean("success");

                                if (success) {
                                    triviaTracker = PLAYING;
                                    trivSwitcher();
                                } else {
                                    String reason = subPayloadJSON.getString("reason");
                                    if (reason.equals("User already belongs to a team")) {
                                        wrongTeam();
                                    } else {
                                        triviaTracker = TEAMNOEXIST;
                                        trivSwitcher();
                                    }
                                }
                                break;
                            case "GameInfo":
                                JSONObject gameJSON = subPayloadJSON.getJSONObject("game");
//                                if(subPayloadJSON.getBoolean("onTeam")){ //todo
                                if (false) {
                                    triviaTracker = PLAYING;
                                    trivSwitcher();
                                } else if (!hosting) {
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
                                currentQuestion = subPayloadJSON.getInt("questionNumber");
                                triviaMessage = extract(subPayloadJSON);
                                triviaMessage.isQuestion(true);
                                output(triviaMessage);
                                break;
                            case "AnswerSubmission":
                                teamAnswer.addAnswer(subPayloadJSON.getString("answer"));
                                break;
                            case "FinalAnswerResponse":
                                break;
                            case "Grading":
                                currentQuestion = subPayloadJSON.getInt("questionNumber");
                                currentRound = subPayloadJSON.getInt("roundNumber");
                                triviaMessage = extract(subPayloadJSON);
                                updateUI(triviaMessage, subPayloadJSON.getString("answer"));
                                answerRunner(gradeExtractor(subPayloadJSON.getJSONArray("teamAnswers")));
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
            int currentRound = Payload.getInt("roundNumber");

            return new TriviaMessage(title, text, image, currentRound, currentQuestion);
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