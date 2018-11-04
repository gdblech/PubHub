package me.lgbt.pubhub.Users;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import me.lgbt.pubhub.connect.RestConnection;

public class User {

    private int userId;
    private String email;
    private String userName;
    private String roleName;
    private int authType;
    private Uri profilePicture;
    private int teamId;
    private String teamName;

    public User() {
        userId = 0;
        email = null;
        userName = null;
        roleName = null;
        authType = 0;
        profilePicture = null;
        teamId = 0;
        teamName = null;
    }

    public User(int userId, String email, String userName, String roleName, int authType, String profilePicturePath, int teamId, String teamName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.roleName = roleName;
        this.authType = authType;
        this.profilePicture = Uri.parse(profilePicturePath);
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public User(int userId, String email, String userName, String roleName, int authType, Uri profilePicture, int teamId, String teamName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.roleName = roleName;
        this.authType = authType;
        this.profilePicture = profilePicture;
        this.teamId = teamId;
        this.teamName = teamName;
    }
    public User(String server, String phbToken){
        String userJson = fetchJsonFromServer(server, phbToken);
        if(userJson != null){
            JSONObject json = null;
            try {
                json = new JSONObject(userJson);
                userName = json.getString("userName");
                roleName = json.getString("role");
            } catch (JSONException e) {
                //if it fails give up
            }
        }
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getTeam() {
        return teamName;
    }

    public void setTeam(String teamName) {
        this.teamName = teamName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getAuthType() {
        return authType;
    }

    public void setAuthType(int authType) {
        this.authType = authType;
    }

    public String getProfilePicturePath() {
        return profilePicture.toString();
    }

    public Uri getPrifilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setProfilePicture(String profilePicturePath) {
        this.profilePicture = Uri.parse(profilePicturePath);
    }

    private String fetchJsonFromServer(String server, String PHBToken){
        RestConnection getProfile = new RestConnection(server, PHBToken, RestConnection.FETCHPROFILE);
        getProfile.start();
        try {
            getProfile.join();
        } catch (InterruptedException e) {
            return null;
        }
        return getProfile.getResponse();

    }
}
