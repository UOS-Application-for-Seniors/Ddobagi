package com.example.ddobagi.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Line;

import java.util.ArrayList;

public class TraceShapeExample extends View {
    final int pointCount = 5;
    int pointRadius;
    int pointInterval;
    float[][] pointCoordinate = new float[pointCount * pointCount][2];

    int canvasWidth, canvasHeight;

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint linePaint, pointPaint;

    float startX, startY, curX, curY;

    ArrayList<Line> lineList;

    public TraceShapeExample(Context context) {
        super(context);
        init(context);
    }

    public TraceShapeExample(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(10.0F);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.GRAY);
        pointPaint.setStyle(Paint.Style.FILL);

        this.startX = this.startY = -1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        canvas.drawColor(Color.WHITE);

        mBitmap = img;
        mCanvas = canvas;
        canvasWidth = w;
        canvasHeight = h;

        int squareWidth;
        if(w < h){
            squareWidth = w;
        }
        else{
            squareWidth = h;
        }

        pointInterval = squareWidth/(pointCount + 1);
        pointRadius = pointInterval / 8;

        for(int i=0; i< pointCount*pointCount; i++){
            int row = i % pointCount;
            int col = i / pointCount;
            pointCoordinate[i][0] = (row + 1) * pointInterval;
            pointCoordinate[i][1] = (col + 1) * pointInterval;
        }

        if(lineList != null){
            assignPath();
        }
    }

    public void assignPath(){
        for(Line line: lineList){
            Path path = new Path();
            path.moveTo(pointCoordinate[line.start][0], pointCoordinate[line.start][1]);
            path.lineTo(pointCoordinate[line.end][0], pointCoordinate[line.end][1]);
            line.path = path;
        }
    }

    public void printLineList(){
        if(lineList != null){
            for(Line line: lineList){
                Log.d("lines", line.start + "-->" + line.end);
            }
            Log.d("lines", "-----------");
        }
    }

    public void setLineList(ArrayList<Line> lineList){
        this.lineList = lineList;
        assignPath();
        invalidate();
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        drawPoint(canvas);
        drawAllLines(canvas);
        printLineList();
    }

    private void drawPoint(Canvas canvas){
        for(int i = 0; i< pointCount * pointCount; i++){
            canvas.drawCircle(pointCoordinate[i][0], pointCoordinate[i][1], pointRadius, pointPaint);
        }
    }

    private void drawAllLines(Canvas canvas){
        if(lineList != null){
            for(Line line : lineList){
                canvas.drawPath(line.path, linePaint);
            }
        }
    }
}