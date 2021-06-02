package com.davidread.quizgame.Models;

/**
 * This model class represents a single question of the quiz. It has attributes for the question,
 * four options for answers, and four booleans representing whether each option is a correct answer
 * or incorrect answer.
 */
public class Question {

    // Model variables.
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private boolean isOption1Correct;
    private boolean isOption2Correct;
    private boolean isOption3Correct;
    private boolean isOption4Correct;

    /**
     * Constructs a question with the specified attributes.
     */
    public Question(String question, String option1, String option2, String option3, String option4, boolean isOption1Correct, boolean isOption2Correct, boolean isOption3Correct, boolean isOption4Correct) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.isOption1Correct = isOption1Correct;
        this.isOption2Correct = isOption2Correct;
        this.isOption3Correct = isOption3Correct;
        this.isOption4Correct = isOption4Correct;
    }

    /**
     * Returns the number of correct answers.
     */
    public int getNumberOfCorrectAnswers() {
        int numberOfCorrectAnswers = 0;

        if (isOption1Correct)
            numberOfCorrectAnswers++;
        if (isOption2Correct)
            numberOfCorrectAnswers++;
        if (isOption3Correct)
            numberOfCorrectAnswers++;
        if (isOption4Correct)
            numberOfCorrectAnswers++;

        return numberOfCorrectAnswers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public boolean isOption1Correct() {
        return isOption1Correct;
    }

    public void setOption1Correct(boolean option1Correct) {
        isOption1Correct = option1Correct;
    }

    public boolean isOption2Correct() {
        return isOption2Correct;
    }

    public void setOption2Correct(boolean option2Correct) {
        isOption2Correct = option2Correct;
    }

    public boolean isOption3Correct() {
        return isOption3Correct;
    }

    public void setOption3Correct(boolean option3Correct) {
        isOption3Correct = option3Correct;
    }

    public boolean isOption4Correct() {
        return isOption4Correct;
    }

    public void setOption4Correct(boolean option4Correct) {
        isOption4Correct = option4Correct;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", isOption1Correct=" + isOption1Correct +
                ", isOption2Correct=" + isOption2Correct +
                ", isOption3Correct=" + isOption3Correct +
                ", isOption4Correct=" + isOption4Correct +
                '}';
    }
}
