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

public class PaintShapePractice extends View {
    final int toolBoxWidth = 80;
    int toolBoxHeight;
    int canvasWidth, canvasHeight;

    Canvas mCanvas;
    Bitmap mBitmap;

    Paint linePaint;
    Paint redPaint, bluePaint, greenPaint, yellowPaint, greyPaint;
    Shape curTool;
    int curToolOriginX, curToolOriginY;

    ArrayList<Paint> paintList = new ArrayList<>();
    ArrayList<Shape> toolList = new ArrayList<>();
    ArrayList<Shape> outsideShapeList = new ArrayList<>();
    ArrayList<Shape> insideShapeList = new ArrayList<>();


    public PaintShapePractice(Context context) {
        super(context);
        init(context);
    }

    public PaintShapePractice(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
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

        yellowPaint  = new Paint();
        yellowPaint.setColor(Color.YELLOW);
        paintList.add(yellowPaint);

        greyPaint = new Paint();
        greyPaint.setColor(Color.GRAY);
        paintList.add(greyPaint);

        for(Paint paint : paintList){
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }
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

        toolBoxHeight = h;

        int count = 0;
        int paintListSize = paintList.size();
        int toolShapeHeight = h / (paintListSize*2 + 1);
        for(Paint paint : paintList){
            count++;
            Rectangle rectangle = new Rectangle(w - toolBoxWidth/2, count * (toolShapeHeight*2) - toolShapeHeight/2, toolBoxWidth, toolShapeHeight);
            rectangle.setPaint(paint);
            toolList.add(rectangle);
        }

//        Triangle triangle = new Triangle(500, 630, 600, 200);
//        outsideShapeList.add(triangle);
//
//        Rectangle rectangle = new Rectangle(500, 800, 400, 200);
//        outsideShapeList.add(rectangle);
//
//        Circle circle = new Circle(200, 150, 100);
//        outsideShapeList.add(circle);
//
//        Rectangle rectangle2 = new Rectangle(400, 770, 80, 80);
//        insideShapeList.add(rectangle2);
//
//        Rectangle rectangle3 = new Rectangle(600, 770, 80, 80);
//        insideShapeList.add(rectangle3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
        drawShape(canvas, outsideShapeList);
        drawShape(canvas, insideShapeList);
        drawTool(canvas);
    }

    private void drawShape(Canvas canvas, ArrayList<Shape> shapeList){
        for(Shape shape: shapeList){
            shape.draw(canvas);
        }
    }

    private void drawTool(Canvas canvas){
        for(Shape shape: toolList){
            shape.draw(canvas);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_UP:
                touchUp(event);
                break;
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
        }
        invalidate();
        return true;
    }

    private void touchUp(MotionEvent event){
        if(curTool != null){
            float x, y;
            x = event.getX();
            y = event.getY();
            boolean findShape = false;
            for(Shape shape: insideShapeList){
                if(shape.isInShape(x, y)){
                    shape.setPaint(curTool.getPaint());
                    findShape = true;
                    break;
                }
            }
            if(!findShape){
                for(Shape shape: outsideShapeList){
                    if(shape.isInShape(x, y)){
                        shape.setPaint(curTool.getPaint());
                        break;
                    }
                }
            }
            curTool.setCenterX(curToolOriginX);
            curTool.setCenterY(curToolOriginY);
            curTool = null;
            curToolOriginX = curToolOriginY = 0;
        }
    }

    private void touchDown(MotionEvent event){
        for(Shape tool: toolList){
            if(tool.isInShape(event.getX(), event.getY())){
                curTool = tool;
                curToolOriginX = tool.getCenterX();
                curToolOriginY = tool.getCenterY();
                break;
            }
        }
    }

    private void touchMove(MotionEvent event){
        if(curTool != null){
            curTool.setCenterX((int) event.getX());
            curTool.setCenterY((int) event.getY());
        }
    }

    public void addShapeList(Shape shape, int priority) {
        if(priority == 0){
            this.insideShapeList.add(shape);
        }
        else{
            this.outsideShapeList.add(shape);
        }
    }

    public ArrayList<Shape> getOutsideShapeList() {
        return outsideShapeList;
    }

    public ArrayList<Shape> getInsideShapeList() {
        return insideShapeList;
    }
}