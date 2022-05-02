package com.example.ddobagi.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Line;

import java.util.ArrayList;

public class LineConnectionView extends View {
    final int circleRadius = 20;
    final int touchSpace = 50;
    final int circleHeightMargin = 10;
    int choiceCount = 4;
    float[][] circleCoordinate = new float[choiceCount * 2][2];
    Line[] connectedLine = new Line[choiceCount * 2];
    boolean touchCancle = true;
    int startCircleNum = -1, endCircleNum = -1;

    int canvasWidth, canvasHeight;
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint linePaint, circlePaint;

    float startX, startY, curX, curY;

    ArrayList<Line> lineList = new ArrayList<>();

    public LineConnectionView(Context context) {
        super(context);
        init(context);
    }

    public LineConnectionView(Context context, @Nullable AttributeSet attrs) {
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

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.FILL);

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

        for(int i=0;i<choiceCount; i++){
            circleCoordinate[i][0] = canvasWidth / (choiceCount*2) * (i*2 + 1);
            circleCoordinate[i][1] = circleHeightMargin;
            circleCoordinate[i + choiceCount][0] = circleCoordinate[i][0];
            circleCoordinate[i + choiceCount][1] = canvasHeight - circleHeightMargin;
        }
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        drawChoiceCircle(canvas);
        drawAllLines(canvas);
        if(startX > 0 && startY > 0){
            canvas.drawLine(startX, startY, curX, curY, linePaint);
        }
    }

    private void drawChoiceCircle(Canvas canvas){
        for(int i=0;i<choiceCount*2;i++){
            canvas.drawCircle(circleCoordinate[i][0], circleCoordinate[i][1], circleRadius, circlePaint);
        }
    }

    private void drawAllLines(Canvas canvas){
        for(Line line : lineList){
            canvas.drawPath(line.path, linePaint);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_UP:
                touchUp(event);
                invalidate();
                return true;

            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                invalidate();
                return true;
        }

        return false;
    }

    private void touchDown(MotionEvent event){
        curX = event.getX();
        curY = event.getY();
        int circleNum = findInCircle(curX, curY);

        if(circleNum != -1){
            Line line = connectedLine[circleNum];
            if(line != null){
                lineList.remove(line);
                connectedLine[line.startCircleNum] = null;
                connectedLine[line.endCircleNum] = null;
            }

            touchCancle = false;
            startX = circleCoordinate[circleNum][0];
            startY = circleCoordinate[circleNum][1];
        }
        else{
            touchCancle = true;
        }
        startCircleNum = circleNum;
    }

    private void touchMove(MotionEvent event){
        //mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //mBitmap.eraseColor(Color.TRANSPARENT);
        if(touchCancle == true){
            return;
        }
        processMove(event);
    }

    private void touchUp(MotionEvent event){
        if(touchCancle == true){
            return;
        }
        curX = event.getX();
        curY = event.getY();
        int circleNum = findInCircle(curX, curY);

        if(circleNum != -1){
            if(circleCoordinate[startCircleNum][1] != circleCoordinate[circleNum][1]){
                Line delLine = connectedLine[circleNum];
                if(delLine != null){
                    lineList.remove(delLine);
                    connectedLine[delLine.startCircleNum] = null;
                    connectedLine[delLine.endCircleNum] = null;
                }
                Line line;
                Path path = new Path();

                path.moveTo(startX, startY);
                path.lineTo(circleCoordinate[circleNum][0], circleCoordinate[circleNum][1]);
                line = new Line(path, startCircleNum, circleNum);
                lineList.add(line);
                connectedLine[startCircleNum] = line;
                connectedLine[circleNum] = line;
            }
        }
        curX = startX = curY = startY = -1;
        /*for(Line line: lineList){
            Log.d("lineList", Integer.toString(line.startCircleNum) + " --- " + Integer.toString(line.endCircleNum));
        }
        Log.d("lineList", "//");*/
    }

    private int findInCircle(float x, float y){
        int result = -1;
        for(int i=0;i<choiceCount*2;i++){
            if(getDistance(x, y, circleCoordinate[i][0], circleCoordinate[i][1]) < touchSpace){
                result = i;
                break;
            }
        }

        return result;
    }

    private float getDistance(float x, float y, float x1, float y1) {
        float result;
        float xd, yd;
        yd = (float) Math.pow((y1-y),2);
        xd = (float) Math.pow((x1-x),2);
        result = (float) Math.sqrt(yd+xd);
        return result;
    }

    private void processMove(MotionEvent event){
        curX = event.getX();
        curY = event.getY();
    }
}