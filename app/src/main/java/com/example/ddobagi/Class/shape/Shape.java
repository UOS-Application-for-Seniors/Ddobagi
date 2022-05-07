package com.example.ddobagi.Class.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public abstract class Shape {
    int centerX, centerY;
    Paint paint, linePaint;

    public Shape(int x, int y) {
        centerX = x;
        centerY = y;

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(5.0F);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

    }

    public void setPaint(Paint paint){
        this.paint = paint;
    }

    public Paint getPaint(){
        return this.paint;
    }

    public int getCenterX(){
        return this.centerX;
    }

    public int getCenterY(){
        return this.centerY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public void draw(Canvas canvas){
        drawPaint(canvas, paint);
        drawPaint(canvas, linePaint);
    }

    abstract void drawPaint(Canvas canvas, Paint paint);
    abstract public boolean isInShape(float x, float y);
}
