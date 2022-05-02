package com.example.ddobagi.Class;

import android.graphics.Path;

public class Line {
    public Path path;
    public int start, end;

    public Line(Path path, int start, int end){
        this.path = path;
        this.start = start;
        this.end = end;
    }
}
