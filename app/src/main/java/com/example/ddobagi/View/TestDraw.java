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

public class TestDraw extends View {
    public boolean changed = false;

    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;

    float lastX, lastY;

    Path mPath = new Path();

    float mCurveEndX, mCurveEndY;

    int mInvalidateExtraBorder = 10;

    static final float TOUCH_TOLERANCE = 8;

    public TestDraw(Context context) {
        super(context);
        init(context);
    }

    public TestDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(3.0F);

        this.lastX = this.lastY = -1;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(img);
        canvas.drawColor(Color.WHITE);

        mBitmap = img;
        mCanvas = canvas;
    }

    protected void onDraw(Canvas canvas){
        if(mBitmap != null){
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_UP:
                changed = true;

                touchUp(event, false);
                invalidate();
                mPath.rewind();

                return true;

            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();

            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                invalidate();
                return true;
        }

        return false;
    }

    private void touchDown(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        lastX = x;
        lastY = y;

        mCanvas.drawLine(lastX, lastY, x, y, mPaint);
    }

    private void touchMove(MotionEvent event){
        //mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mBitmap.eraseColor(Color.TRANSPARENT);
        processMove(event);
    }

    private void touchUp(MotionEvent event, boolean cancel){
        processMove(event);
    }

    private void processMove(MotionEvent event){
        final float x = event.getX();
        final float y = event.getY();

        mCanvas.drawLine(lastX, lastY, x, y, mPaint);
    }
}
