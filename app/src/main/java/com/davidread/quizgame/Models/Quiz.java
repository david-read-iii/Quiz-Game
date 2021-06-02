package com.davidread.quizgame.Models;

import android.content.Context;

import com.davidread.quizgame.Utilities.DatabaseHelper;

import java.util.ArrayList;

/**
 * This model class represents a quiz. Each question is stored in a question object in an array
 * list. This array list can only be determined when the object is initially constructed. The state
 * of the quiz is managed by this class. Such state includes the index of the current question, the
 * count of questions correct, and count of total questions. Functions are available to manipulate
 * the state of this object.
 */
public class Quiz {

    // Model variables.
    private ArrayList<Question> questions;
    private int indexCurrentQuestion;
    private int countQuestionsCorrect;
    private int countTotalQuestions;
    private long userId;

    // Model helper objects.
    private DatabaseHelper db;

    /**
     * Constructs a quiz with an array list of question objects specified.
     */
    public Quiz(ArrayList<Question> questions, long userId, Context context) {
        this.questions = questions;
        indexCurrentQuestion = 0;
        countQuestionsCorrect = 0;
        countTotalQuestions = questions.size();
        this.userId = userId;
        db = new DatabaseHelper(context);
    }

    /**
     * Returns a question object containing the current question.
     */
    public Question getCurrentQuestion() {
        return questions.get(indexCurrentQuestion);
    }

    /**
     * Grades the current question. It does this by comparing the given user selections with the
     * current question's correct/incorrect booleans.
     */
    public void gradeCurrentQuestion(boolean userSelectionOption1, boolean userSelectionOption2, boolean userSelectionOption3, boolean userSelectionOption4) {
        Question currentQuestion = questions.get(indexCurrentQuestion);
        if (userSelectionOption1 == currentQuestion.isOption1Correct() && userSelectionOption2 == currentQuestion.isOption2Correct() && userSelectionOption3 == currentQuestion.isOption3Correct() && userSelectionOption4 == currentQuestion.isOption4Correct())
            countQuestionsCorrect++;
    }

    /**
     * Increments the current question.
     */
    public void incrementCurrentQuestion() {
        indexCurrentQuestion++;
    }

    /**
     * Returns true if the quiz is complete.
     */
    public boolean isQuizComplete() {
        if (indexCurrentQuestion >= countTotalQuestions)
            return true;
        else
            return false;
    }

    /**
     * Records the quiz result in a result object and inserts it into the database.
     */
    public void recordQuizResult() {
        db.insertResult(userId, countQuestionsCorrect, countTotalQuestions);
    }

    /**
     * Resets the quiz. Specifically, it resets the index of the current question and resets the
     * number of questions correct.
     */
    public void resetQuiz() {
        indexCurrentQuestion = 0;
        countQuestionsCorrect = 0;
    }

    public int getIndexCurrentQuestion() {
        return indexCurrentQuestion;
    }

    public int getCountTotalQuestions() {
        return countTotalQuestions;
    }
}
