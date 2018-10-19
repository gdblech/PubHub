package me.lgbt.pubhub.Users;

public class User {

    int userId;
    String email;
    String userName;
    String roleName;
    int authType;
    String profilePicPath;
    int teamId;
    String teamName;

    public User() {
        userId = 0;
        email = null;
        userName = null;
        roleName = null;
        authType = 0;
        profilePicPath = null;
        teamId = 0;
        teamName = null;
    }

    public User(int userId, String email, String userName, String roleName, int authType, String profilePicPath, int teamId, String teamName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
        this.roleName = roleName;
        this.authType = authType;
        this.profilePicPath = profilePicPath;
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

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }
}
