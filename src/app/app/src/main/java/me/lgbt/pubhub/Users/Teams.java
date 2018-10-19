package me.lgbt.pubhub.Users;

public class Teams {

    int MAX_TEAMS = 10;
    Team[] teams = new Team[MAX_TEAMS]; //TODO Make max number of teams

    public class Team extends Exception {

        int MAX_PLAYERS = 10;
        User[] players = new User[MAX_PLAYERS];
        int numPlayers = 0;
        String teamName;

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
    }

}
