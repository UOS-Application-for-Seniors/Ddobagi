package com.example.ddobagi.Class;

import android.graphics.Path;

public class Line {
    public Path path;
    public int startCircleNum, endCircleNum;

    public Line(Path path, int start, int end){
        this.path = path;
        this.startCircleNum = start;
        this.endCircleNum = end;
    }
}
