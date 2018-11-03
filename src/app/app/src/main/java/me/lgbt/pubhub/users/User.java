package me.lgbt.pubhub.users;

import android.net.Uri;

//TODO are we using intent keys?

public class User {
    private static int userId;
    private String userName;
    private int nickname;
    private String email;
    private String role;
    private int authType;
    private Uri profilePicture;
    private int teamId;
    private String teamName;

    public User() {
        userId = 0;
        email = null;
        userName = null;
        role = null;
        authType = 0;
        profilePicture = null;
        teamId = 0;
        teamName = null;
    }

    public User(int userId, String email, String userName, String roleName, int authType, String profilePicturePath, int teamId, String teamName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.role = roleName;
        this.authType = authType;
        this.profilePicture = Uri.parse(profilePicturePath);
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public User(int userId, String email, String userName, String roleName, int authType, Uri profilePicture, int teamId, String teamName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.role = roleName;
        this.authType = authType;
        this.profilePicture = profilePicture;
        this.teamId = teamId;
        this.teamName = teamName;
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

    public static int getUserId() {
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
        return role;
    }

    public void setRoleName(String roleName) {
        this.role = roleName;
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

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setProfilePicture(String profilePicturePath) {
        this.profilePicture = Uri.parse(profilePicturePath);
    }

    public int getNickname() {
        return nickname;
    }

    public void setNickname(int nickname) {
        this.nickname = nickname;
    }
}
