package me.lgbt.pubhub.interfaces;

import me.lgbt.pubhub.trivia.utils.Answer;

/**
 * @author Geoffrey Blech
 */
public interface GradingListener {
    void answerGraded(Answer[] answers);
}
