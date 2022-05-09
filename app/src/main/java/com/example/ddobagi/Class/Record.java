package com.example.ddobagi.Class;

public class Record {
    public String gameName;
    int difficulty;
    int stars;
    float correctRate;

    public Record(String gameName, int difficulty, int stars, float correctRate){
        this.gameName = gameName;
        this.difficulty = difficulty;
        this.stars = stars;
        this.correctRate = correctRate;
    }
}
