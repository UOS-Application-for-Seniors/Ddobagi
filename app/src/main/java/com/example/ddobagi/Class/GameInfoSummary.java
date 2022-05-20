package com.example.ddobagi.Class;

public class GameInfoSummary {
    public int gameid;
    public String gamename;
    public String gamedescript;
    public String field;

    public GameInfoSummary(int gameid, String gamename, String gamedescript, String field) {
        this.gameid = gameid;
        this.gamename = gamename;
        this.gamedescript = gamedescript;
        this.field = field;
    }
}