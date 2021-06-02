package com.davidread.quizgame.Models;

/**
 * This model class represents a quiz result of some user. It has attributes for the unique id of
 * the user the result belongs to, a timestamp of when the test was completed, the count of correct
 * questions, and the count of total questions.
 */
public class Result {

    // SQLite table and column constants.
    public static final String TABLE_NAME = "results";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_COUNT_QUESTIONS_CORRECT = "count_questions_correct";
    public static final String COLUMN_COUNT_TOTAL_QUESTIONS = "count_total_questions";

    // SQLite create table query constant.
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USER_ID + " INTEGER,"
            + COLUMN_TIMESTAMP + " INTEGER,"
            + COLUMN_COUNT_QUESTIONS_CORRECT + " INTEGER,"
            + COLUMN_COUNT_TOTAL_QUESTIONS + " INTEGER"
            + ")";

    // Model variables.
    private long userId;
    private long timestamp;
    private int countQuestionsCorrect;
    private int countTotalQuestions;

    /**
     * Constructs a result with null attributes.
     */
    public Result() {

    }

    /**
     * Constructs a result with the specified attributes.
     */
    public Result(long userId, long timestamp, int countQuestionsCorrect, int countTotalQuestions) {
        this.userId = userId;
        this.timestamp = timestamp;
        this.countQuestionsCorrect = countQuestionsCorrect;
        this.countTotalQuestions = countTotalQuestions;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCountQuestionsCorrect() {
        return countQuestionsCorrect;
    }

    public void setCountQuestionsCorrect(int countQuestionsCorrect) {
        this.countQuestionsCorrect = countQuestionsCorrect;
    }

    public int getCountTotalQuestions() {
        return countTotalQuestions;
    }

    public void setCountTotalQuestions(int countTotalQuestions) {
        this.countTotalQuestions = countTotalQuestions;
    }

    @Override
    public String toString() {
        return "Result{" +
                "userId=" + userId +
                ", timestamp=" + timestamp +
                ", countQuestionsCorrect=" + countQuestionsCorrect +
                ", countTotalQuestions=" + countTotalQuestions +
                '}';
    }
}