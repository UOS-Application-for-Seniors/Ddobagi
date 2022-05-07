package com.example.ddobagi.Class.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Rectangle extends Shape{
    int width, height;
    public Rectangle(int x, int y, int width, int height){
        super(x, y);
        this.width = width;
        this.height = height;
    }

    void drawPaint(Canvas canvas, Paint paint){
        canvas.drawRect(centerX - width/2, centerY - height/2, centerX + width/2, centerY + height/2, paint);
    }

    public boolean isInShape(float x, float y){
        if(centerX - width/2 <= x && x <= centerX + width/2){
            if(centerY - width/2 <= y && y <= centerY + width/2){
                return true;
            }
        }
        return false;
    }
}
