package me.lgbt.pubhub.users;

public class Teams {

    int MAX_TEAMS = 10;
    Team[] teams = new Team[MAX_TEAMS]; //TODO Make max number of teams

    public class Team extends Exception {

        int MAX_PLAYERS = 10;
        User[] players = new User[MAX_PLAYERS];
        int numPlayers = 0;
        String teamName;
        int teamId;

        public Team(String message, Throwable cause, User[] players, int numPlayers, String teamName, int identification) {
            super(message, cause);
            this.players = players;
            this.numPlayers = numPlayers;
            this.teamName = teamName;
            this.teamId = identification;
        }

        public Team(String teamName) {
            this.teamName = teamName;
        }

        public void addUser(User user) {
            int teamPosition = getNextEmpty(numPlayers);
            players[teamPosition] = user;
            numPlayers++;
        }

        public void removeUser(User user) {
            int teamPosition = getPosition(user);
            players[teamPosition] = null;
            for (int i=teamPosition; i< players.length; i++) {
                players[i] = players[i+1];
                // call method to update user position
            }
        }

        public int getNextEmpty(int nextEmpty) {

            for (int i=0; i<players.length; i++) {
                if (players[nextEmpty] == null) {
                    return nextEmpty;
                }
                else {
                    getNextEmpty(nextEmpty+1);
                }
            }
            return nextEmpty;
        }

        public int getPosition(User user) {
            for (int i=0; i<players.length; i++) {
                if (user == players[i]) {
                    return i;
                }
            }
            return -1; // player not found
        }

        public int getMAX_PLAYERS() {
            return MAX_PLAYERS;
        }

        public void setMAX_PLAYERS(int MAX_PLAYERS) {
            this.MAX_PLAYERS = MAX_PLAYERS;
        }

        public User[] getPlayers() {
            return players;
        }

        public void setPlayers(User[] players) {
            this.players = players;
        }

        public int getNumPlayers() {
            return numPlayers;
        }

        public void setNumPlayers(int numPlayers) {
            this.numPlayers = numPlayers;
        }

        public String getTeamName() {
            return teamName;
        }

        public void setTeamName(String teamName) {
            this.teamName = teamName;
        }

        public int getIdentification() {
            return teamId;
        }

        public void setIdentification(int identification) {
            this.teamId = identification;
        }
    }

}
