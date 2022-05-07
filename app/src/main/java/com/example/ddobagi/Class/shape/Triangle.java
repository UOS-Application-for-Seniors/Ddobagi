package com.example.ddobagi.Class.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class Triangle extends Shape{
    int base, height;
    Path path;

    public Triangle(int x, int y, int base, int height){
        super(x, y);
        this.base = base;
        this.height = height;

        path = new Path();
        path.moveTo(centerX - base/2, centerY + height/3);
        path.lineTo(centerX + base/2, centerY + height/3);
        path.lineTo(centerX, centerY - height/3*2);
        path.lineTo(centerX - base/2, centerY + height/3);
    }

    void drawPaint(Canvas canvas, Paint paint){
        canvas.drawPath(path, paint);
    }

    public boolean isInShape(float x, float y){
        //y = ax + b
        int a1, b1, a2, b2;
        a1 = ((centerY + height/3) - (centerY - height/3*2)) / ((centerX - base/2) - centerX);
        b1 = (centerY - height/3*2) - a1 * (centerX);

        a2 = -a1;
        b2 = (centerY - height/3*2) - a2 * (centerX);

        if(y >= a1 * x + b1){
            if(y >= a2 * x + b2){
                if(y <= centerY + height/3){
                    return true;
                }
            }
        }
        return false;
    }
}
