package com.example.ddobagi.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.ddobagi.Class.shape.Circle;
import com.example.ddobagi.Class.shape.Rectangle;
import com.example.ddobagi.Class.shape.Shape;
import com.example.ddobagi.Class.shape.Triangle;

import java.util.ArrayList;

public class PaintShapeExample extends View {
    int canvasWidth, canvasHeight;

    Canvas mCanvas;
    Bitmap mBitmap;

    Paint linePaint;
    Paint redPaint, bluePaint, greenPaint, yellowPaint, greyPaint;

    ArrayList<Paint> paintList = new ArrayList<>();
    ArrayList<Shape> outsideShapeList = new ArrayList<>();
    ArrayList<Shape> insideShapeList = new ArrayList<>();

    PaintShapePractice paintShapePractice;

    public PaintShapeExample(Context context) {
        super(context);
        init(context);
    }

    public PaintShapeExample(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(10.0F);

        redPaint = new Paint();
        redPaint.setColor(Color.RED);
        paintList.add(redPaint);

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);
        paintList.add(bluePaint);

        greenPaint = new Paint();
        greenPaint.setColor(Color.GREEN);
        paintList.add(greenPaint);

        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        paintList.add(yellowPaint);

        greyPaint = new Paint();
        greyPaint.setColor(Color.GRAY);
        paintList.add(greyPaint);

        for (Paint paint : paintList) {
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }
    }

    public void setPaintShapePractice(PaintShapePractice paintShapePractice) {
        this.paintShapePractice = paintShapePractice;
    }

    public int commit(){
        int answerCount = 0;
        int result = 0;

        for(Shape shapeExample: insideShapeList){
            for(Shape shapePractice: paintShapePractice.getInsideShapeList()){

                if(shapeExample.getCenterX() == shapePractice.getCenterX()){
                    if(shapeExample.getCenterY() == shapePractice.getCenterY()){
                        if(shapeExample.getPaint().getColor() == shapePractice.getPaint().getColor()){
                            answerCount++;
                            break;
                        }
                    }
                }

            }
        }

        for(Shape shapeExample: outsideShapeList){
            for(Shape shapePractice: paintShapePractice.getOutsideShapeList()){

                if(shapeExample.getCenterX() == shapePractice.getCenterX()){
                    if(shapeExample.getCenterY() == shapePractice.getCenterY()){
                        if(shapeExample.getPaint().getColor() == shapePractice.getPaint().getColor()){
                            answerCount++;
                            break;
                        }
                    }
                }

            }
        }

        if(answerCount == insideShapeList.size() + outsideShapeList.size()){
            result = 1;
        }
        else if(answerCount > 0){
            result = 2;
        }
        else{
            result = 0;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        canvas.drawColor(Color.WHITE);

        mBitmap = img;
        mCanvas = canvas;
        canvasWidth = w;
        canvasHeight = h;
    }

    public void addShape(int centerX, int centerY, int width, int height, String type, String color, int priority){
        Shape shapeExample, shapePractice;
        switch (type){
            case "rectangle":
                shapePractice = new Rectangle(centerX, centerY, width, height);
                shapeExample = new Rectangle(centerX, centerY, width, height);
                break;
            case "triangle":
                shapePractice = new Triangle(centerX, centerY, width, height);
                shapeExample = new Triangle(centerX, centerY, width, height);
                break;
            case "circle":
                shapePractice = new Circle(centerX, centerY, width);
                shapeExample = new Circle(centerX, centerY, width);
                break;
            default:
                return;
        }

        switch (color){
            case "red":
                shapeExample.setPaint(redPaint);
                break;
            case "blue":
                shapeExample.setPaint(bluePaint);
                break;
            case "green":
                shapeExample.setPaint(greenPaint);
                break;
            case "yellow":
                shapeExample.setPaint(yellowPaint);
                break;
            case "grey":
                shapeExample.setPaint(greyPaint);
                break;
            default:
                return;
        }

        switch (priority){
            case 0:
                insideShapeList.add(shapeExample);
                break;
            case 1:
                outsideShapeList.add(shapeExample);
                break;
            default:
                return;
        }
        paintShapePractice.addShapeList(shapePractice, priority);

        invalidate();
        paintShapePractice.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        drawShape(canvas, outsideShapeList);
        drawShape(canvas, insideShapeList);
    }

    private void drawShape(Canvas canvas, ArrayList<Shape> shapeList){
        for(Shape shape: shapeList){
            shape.draw(canvas);
        }
    }

}