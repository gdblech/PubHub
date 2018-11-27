package me.lgbt.pubhub.interfaces;

import me.lgbt.pubhub.trivia.utils.Answer;

public interface GradingListener {
    void answerGraded(Answer[] answers);
}
