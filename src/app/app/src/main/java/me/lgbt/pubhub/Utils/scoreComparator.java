package me.lgbt.pubhub.Utils;

import java.util.Comparator;

public class scoreComparator implements Comparator<ScoreObject> {
    @Override
    public int compare(ScoreObject scoreObject, ScoreObject t1) {
        return t1.getScore() -scoreObject.getScore();
    }
}
