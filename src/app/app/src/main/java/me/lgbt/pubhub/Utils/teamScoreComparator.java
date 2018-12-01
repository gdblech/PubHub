package me.lgbt.pubhub.Utils;

import java.util.Comparator;

/**
 * @author Geoffrey Blech
 */
public class teamScoreComparator implements Comparator<teamScoreObject> {
    @Override
    public int compare(teamScoreObject teamScoreObject, teamScoreObject t1) {
        return t1.getScore() - teamScoreObject.getScore();
    }
}
