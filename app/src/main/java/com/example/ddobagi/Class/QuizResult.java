package com.example.ddobagi.Class;

public class QuizResult {
    int quizIndex;
    String quizName;
    int difficulty;
    int coin;

    public QuizResult(int quizIndex, String quizName, int difficulty, int coin){
        this.quizIndex = quizIndex;
        this.quizName = quizName;
        this.difficulty = difficulty;
        this.coin = coin;
    }
}
