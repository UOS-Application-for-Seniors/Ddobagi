package com.example.ddobagi.Class.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Shape{
    int radius;

    public Circle(int x, int y, int radius){
        super(x, y);
        this.radius = radius;
    }

    void drawPaint(Canvas canvas, Paint paint) {
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public boolean isInShape(float x, float y){
        if(getDistance(centerX, centerY, x, y) < radius){
            return true;
        }
        else{
            return false;
        }
    }

    private float getDistance(float x, float y, float x1, float y1) {
        float result;
        float xd, yd;
        yd = (float) Math.pow((y1-y),2);
        xd = (float) Math.pow((x1-x),2);
        result = (float) Math.sqrt(yd+xd);
        return result;
    }
}
