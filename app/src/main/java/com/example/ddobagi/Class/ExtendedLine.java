package com.example.ddobagi.Class;

import android.graphics.Path;

public class ExtendedLine extends Line{
    public float a, b;
    //y = ax + b

    public ExtendedLine(Path path, int start, int end, float a, float b){
        super(path, start, end);
        this.a = a;
        this.b = b;
    }
}
