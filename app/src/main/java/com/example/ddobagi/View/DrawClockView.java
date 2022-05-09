package com.example.ddobagi.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ddobagi.Class.Line;
import com.example.ddobagi.R;

import java.util.ArrayList;

public class DrawClockView extends View {
    int originTouchSpace;
    int[] originCoor = new int[2];
    final int clockNum = 12;
    float[][] clockNumCoor = new float[clockNum][2];
    boolean touchCancle = true;
    int startClockNum = -1, endClockNum = -1;

    int drawingCount = 0; //0이면 시침 그릴 차례, 1이면 분침 그릴 차례, 2이면 새로 그리기

    int clockRadius;
    int canvasWidth, canvasHeight;
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint hourHandPaint, minuteHandPaint;

    float startX, startY, curX, curY;

    ArrayList<Line> lineList = new ArrayList<>();

    public ArrayList<Line> getLineList() {
        return lineList;
    }

    public DrawClockView(Context context) {
        super(context);
        init(context);
    }

    public DrawClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        hourHandPaint = new Paint();
        hourHandPaint.setAntiAlias(true);
        hourHandPaint.setColor(Color.BLACK);
        hourHandPaint.setStyle(Paint.Style.STROKE);
        hourHandPaint.setStrokeCap(Paint.Cap.ROUND);
        hourHandPaint.setStrokeWidth(25.0F);

        minuteHandPaint = new Paint();
        minuteHandPaint.setAntiAlias(true);
        minuteHandPaint.setColor(Color.BLACK);
        minuteHandPaint.setStyle(Paint.Style.STROKE);
        minuteHandPaint.setStrokeCap(Paint.Cap.ROUND);
        minuteHandPaint.setStrokeWidth(17.0F);

        this.startX = this.startY = -1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        if(w>h){
            clockRadius = h/2;
            //originCoor[0] = w/2;
            //originCoor[1] = clockRadius;
        }
        else{
            clockRadius = w/2;
            //originCoor[0] = clockRadius;
            //originCoor[1] = h/2;

        }
        originCoor[0] = originCoor[1] = clockRadius;
        Bitmap srcImg = BitmapFactory.decodeResource(getResources(), R.drawable.clock_frame);
        Bitmap resizedImg = Bitmap.createScaledBitmap(srcImg, clockRadius * 2, clockRadius * 2, false);

        //originCoor[0] = originCoor[1] = clockRadius;
        originTouchSpace = clockRadius/5;

        Canvas canvas = new Canvas();
        canvas.setBitmap(resizedImg);
        //canvas.drawColor(Color.WHITE);

        mBitmap = resizedImg;
        mCanvas = canvas;
        canvasWidth = w;
        canvasHeight = h;

        for(int i = 0; i< clockNum; i++){
            clockNumCoor[i][0] = originCoor[0] + (float) (clockRadius * Math.sin(Math.toRadians(30 * (i + 1))));
            clockNumCoor[i][1] = originCoor[1] - (float) (clockRadius * Math.cos(Math.toRadians(30 * (i + 1))));
            //Log.d("onSizeChanged", Integer.toString(i+1) + ": " +Float.toString(clockNumCoor[i][1]));
        }
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }

        //drawChoiceCircle(canvas);
        drawAllLines(canvas);

        Paint paint;
        if(drawingCount == 0){
            paint = hourHandPaint;
        }
        else{
            paint = minuteHandPaint;
        }

        if(startX > 0 && startY > 0){
            canvas.drawLine(startX, startY, curX, curY, paint);
        }
    }

    private void drawAllLines(Canvas canvas){
        Paint paint = hourHandPaint;
        for(Line line : lineList){
            canvas.drawPath(line.path, paint);
            paint = minuteHandPaint;
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

        if(getDistance(originCoor[0], originCoor[1], curX, curY) < originTouchSpace){
            touchCancle = false;
            startX = originCoor[0];
            startY = originCoor[1];

            if(drawingCount == 2){
                drawingCount = 0;
                lineList.clear();
            }
            //Log.d("drawingCount", Integer.toString(drawingCount));
        }
        else{
            touchCancle = true;
        }
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
        int lineLength = (int) getDistance(originCoor[0], originCoor[1], curX, curY);

        if(lineLength > clockRadius/2){
            int curClockNum = findClockNum();
            if(curClockNum == -1){
                curX = startX = curY = startY = -1;
                return;
            }
            Line line;
            float drawXcoor, drawYcoor;
            Path path = new Path();

            drawXcoor = clockNumCoor[curClockNum][0];
            drawYcoor = clockNumCoor[curClockNum][1];
            path.moveTo(originCoor[0], originCoor[1]);


            if(drawingCount == 0){
                drawXcoor = (float) ((originCoor[0] + drawXcoor) * 0.5);
                drawYcoor = (float) ((originCoor[1] + drawYcoor) * 0.5);
            }
            else{
                drawXcoor = (float) (originCoor[0] + (drawXcoor - originCoor[0]) * 0.7);
                drawYcoor = (float) (originCoor[1] + (drawYcoor - originCoor[1]) * 0.7);
            }
            path.lineTo(drawXcoor, drawYcoor);
            line = new Line(path, -1, curClockNum);
            lineList.add(line);

            drawingCount++;
        }
        curX = startX = curY = startY = -1;


        /*for(Line line: lineList){
            Log.d("lineList", Integer.toString(line.startCircleNum) + " --- " + Integer.toString(line.endCircleNum));
        }
        Log.d("lineList", "//");*/
    }

    private int findClockNum(){
        int curClockNum = -1;

        float angle = 180 + getAngle(originCoor[0], originCoor[1], curX, curY);
        //Log.d("angle", Float.toString(angle));
        for(int i=0; i<clockNum; i++){
            if(i == 8){
                if(angle >= 345 || angle <= 15){
                    curClockNum = i;
                    break;
                }
            }
            if(angle >= ((i*30) + 105) % 360 && angle <= ((i*30 + 135)) % 360){
                curClockNum = i;
                break;
            }
        }
        return curClockNum;
    }

    private float getDistance(float x, float y, float x1, float y1) {
        float result;
        float xd, yd;
        yd = (float) Math.pow((y1-y),2);
        xd = (float) Math.pow((x1-x),2);
        result = (float) Math.sqrt(yd+xd);
        return result;
    }

    public float getAngle(float x1, float y1, float x2, float y2) {
        float dy = y2 - y1;
        float dx = x2 - x1;

        return (float) (Math.atan2(dy, dx) * (180.0 / Math.PI));
    }

    private void processMove(MotionEvent event){
        curX = event.getX();
        curY = event.getY();
    }
}
