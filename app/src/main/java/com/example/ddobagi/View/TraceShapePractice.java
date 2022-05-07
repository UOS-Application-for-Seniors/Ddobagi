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

import com.example.ddobagi.Class.ExtendedLine;
import com.example.ddobagi.Class.Line;

import java.util.ArrayList;

public class TraceShapePractice extends View {
    final int pointCount = 5;
    int pointRadius;
    int pointTouchRadius;
    int pointInterval;
    float[][] pointCoordinate = new float[pointCount * pointCount][2];
    int startPointNum = -1, endPointNum = -1;

    final int toolBoxWidth = 80;
    int toolBoxHeight = toolBoxWidth * 2;
    float[] toolBoxCoor = new float[2];
    final int eraseMargin = 10;

    int canvasWidth, canvasHeight;


    boolean isTouchCancle = true, isEraseMode = false;

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint linePaint, pointPaint, toolBoxPaint;

    float startX, startY, curX, curY;

    ArrayList<ExtendedLine> lineList = new ArrayList<>();

    public TraceShapePractice(Context context) {
        super(context);
        init(context);
    }

    public TraceShapePractice(Context context, @Nullable AttributeSet attrs) {
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

        toolBoxPaint = new Paint();
        toolBoxPaint.setAntiAlias(true);
        toolBoxPaint.setColor(Color.GREEN);
        toolBoxPaint.setStyle(Paint.Style.STROKE);
        toolBoxPaint.setStrokeWidth(10.0F);

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
        if(w - toolBoxWidth < h){
            squareWidth = w - toolBoxWidth;
        }
        else{
            squareWidth = h;
        }
        toolBoxCoor[0] = squareWidth + (toolBoxWidth / 2);
        toolBoxCoor[1] = squareWidth/2;

        pointInterval = squareWidth/(pointCount + 1);
        pointRadius = pointInterval / 8;
        pointTouchRadius = pointRadius * 3;

        for(int i=0; i< pointCount*pointCount; i++){
            int row = i % pointCount;
            int col = i / pointCount;
            pointCoordinate[i][0] = (row + 1) * pointInterval;
            pointCoordinate[i][1] = (col + 1) * pointInterval;
        }
    }

    public ArrayList<ExtendedLine> getLineList(){
        return lineList;
    }

    public void printLineList(){
        for(ExtendedLine line: lineList){
            Log.d("lines", line.start + "-->" + line.end + ": " + line.a + ", " + line.b);
        }
        Log.d("lines", "-----------");
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        drawPoint(canvas);
        drawAllLines(canvas);
        drawToolBox(canvas);
        if(startX > 0 && startY > 0){
            canvas.drawLine(startX, startY, curX, curY, linePaint);
        }
    }

    private void drawToolBox(Canvas canvas){
        canvas.drawRect(toolBoxCoor[0] - toolBoxWidth/2, toolBoxCoor[1] - toolBoxHeight/2,
                toolBoxCoor[0] + toolBoxWidth/2, toolBoxCoor[1] + toolBoxHeight/2, toolBoxPaint);
    }

    private void drawPoint(Canvas canvas){
        for(int i = 0; i< pointCount * pointCount; i++){
            canvas.drawCircle(pointCoordinate[i][0], pointCoordinate[i][1], pointRadius, pointPaint);
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
        if(isInToolBox(curX, curY)){
            isTouchCancle = true;
            if(isEraseMode){
                isEraseMode = false;
                toolBoxPaint.setColor(Color.GREEN);
            }
            else{
                isEraseMode = true;
                toolBoxPaint.setColor(Color.YELLOW);
            }
            return;
        }

        if(isEraseMode){
            eraseLine(curX, curY);
            return;
        }

        int pointNum = findInPoint(curX, curY);

        if(pointNum != -1){
            isTouchCancle = false;
            startX = pointCoordinate[pointNum][0];
            startY = pointCoordinate[pointNum][1];
        }
        else{
            isTouchCancle = true;
        }
        startPointNum = pointNum;
    }

    private void touchMove(MotionEvent event){
        if(isEraseMode){
            eraseLine(event.getX(), event.getY());
            return;
        }

        if(isTouchCancle){
            return;
        }


        processMove(event);
    }

    private void touchUp(MotionEvent event){
        curX = event.getX();
        curY = event.getY();

        if(isEraseMode){
            eraseLine(curX, curY);
            return;
        }

        if(isTouchCancle){
            return;
        }

        int pointNum = findInPoint(curX, curY);

        if(pointNum != -1){
            if(startPointNum != pointNum){
                lineListAdd(pointNum);
            }
        }
        curX = startX = curY = startY = -1;
    }

    private void eraseLine(float x, float y){
        for(Line line: lineList){
            if(isInLine(x, y, line)){
                lineList.remove(line);
                break;
            }
        }
    }

    private boolean isInLine(float x, float y, Line line){
        float startX, startY, endX, endY;
        startX = pointCoordinate[line.start][0];
        startY = pointCoordinate[line.start][1];
        endX = pointCoordinate[line.end][0];
        endY = pointCoordinate[line.end][1];

        if(getDistance(startX, startY, endX, endY) + eraseMargin >
                getDistance(x, y, startX, startY) + getDistance(x, y, endX, endY)){
            return true;
        }
        return false;
    }

    private void lineListAdd(int pointNum){
        //y = ax + b;
        float firstX, firstY, secondX, secondY;
        int firstPointNum, secondPointNum;
        float a, b;
        ArrayList<ExtendedLine> sameStraightLineList = new ArrayList<>();

        if(pointNum < startPointNum){
            firstPointNum = pointNum;
            secondPointNum = startPointNum;
        }
        else{
            firstPointNum = startPointNum;
            secondPointNum = pointNum;
        }
        firstX = pointCoordinate[firstPointNum][0];
        firstY = pointCoordinate[firstPointNum][1];
        secondX = pointCoordinate[secondPointNum][0];
        secondY = pointCoordinate[secondPointNum][1];

        if(secondX - firstX == 0){
            a = firstX;
            b = -1;
        }
        else{
            a = (secondY - firstY) / (secondX - firstX);
            b = firstY - a * firstX;
        }
        ExtendedLine newLine;
        Path path = new Path();

        path.moveTo(firstX, firstY);
        path.lineTo(secondX, secondY);
        newLine = new ExtendedLine(path, firstPointNum, secondPointNum, a, b);
        lineList.add(newLine);

        for(ExtendedLine line: lineList){
            if(line.a == a && line.b == b){
                sameStraightLineList.add(line);
            }
        }

        ArrayList<ExtendedLine> removeList = new ArrayList<>();
        ArrayList<ExtendedLine> addList = new ArrayList<>();

        boolean loop;
        do {
            loop = false;
            for(ExtendedLine line1: sameStraightLineList){
                for(ExtendedLine line2: sameStraightLineList){
                    if(line1.equals(line2)){
                        continue;
                    }
                    if(removeList.contains(line1) || removeList.contains(line2)){
                        continue;
                    }
                    ExtendedLine firstLine, secondLine;
                    if(line1.start < line2.start){
                        firstLine = line1;
                        secondLine = line2;
                    }
                    else if(line1.start > line2.start) {
                        firstLine = line2;
                        secondLine = line1;
                    }
                    else{
                        if(line1.end < line2.end){
                            firstLine = line1;
                            secondLine = line2;
                        }
                        else{
                            firstLine = line2;
                            secondLine = line1;
                        }
                    }
                    if(firstLine.end < secondLine.start){
                        continue;
                    }
                    else{
                        if(!removeList.contains(firstLine)){
                            removeList.add(firstLine);
                        }
                        if(!removeList.contains(secondLine)){
                            removeList.add(secondLine);
                        }
                        //lineList.remove(firstLine);
                        //lineList.remove(secondLine);
                        //sameStraightLineList.remove(firstLine);
                        //sameStraightLineList.remove(secondLine);

                        ExtendedLine mergedLine;
                        Path mergedPath = new Path();

                        mergedPath.moveTo(pointCoordinate[firstLine.start][0], pointCoordinate[firstLine.start][1]);
                        mergedPath.lineTo(pointCoordinate[secondLine.end][0], pointCoordinate[secondLine.end][1]);
                        mergedLine = new ExtendedLine(mergedPath, firstLine.start, secondLine.end, a, b);

                        //lineList.add(mergedLine);
                        //sameStraightLineList.add(mergedLine);
                        if(!addList.contains(mergedLine)){
                            addList.add(mergedLine);
                        }
                        loop = true;
                    }
                }
            }

            for(ExtendedLine line: removeList){
                lineList.remove(line);
                sameStraightLineList.remove(line);
            }
            removeList.clear();

            for(ExtendedLine line: addList){
                lineList.add(line);
                sameStraightLineList.add(line);
            }
            addList.clear();

        } while(loop);
        printLineList();
    }

    private int findInPoint(float x, float y){
        int result = -1;
        for(int i = 0; i< pointCount * pointCount; i++){
            if(getDistance(x, y, pointCoordinate[i][0], pointCoordinate[i][1]) < pointTouchRadius){
                result = i;
                break;
            }
        }

        return result;
    }

    private boolean isInToolBox(float x, float y){
        boolean result = false;
        if(x <= toolBoxCoor[0] + toolBoxWidth/2 && x >= toolBoxCoor[0] - toolBoxWidth/2){
            if(y <= toolBoxCoor[1] + toolBoxHeight/2 && y >= toolBoxCoor[1] - toolBoxHeight/2){
                result = true;
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