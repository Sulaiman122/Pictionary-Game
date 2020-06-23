package com.example.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ViewerPaint extends View {


    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    private Paint mPaint;
    public ArrayList<FingerPath> paths = new ArrayList<>();
    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    DatabaseReference dt;
    Integer action = 0;
    float x = 0;
    float y = 0;
    float xx;
    float yy;
    float tmp1;
    float tmp2;
    float xstart;
    float ystart;
    boolean first = true;
    boolean last = true;


    public ViewerPaint(Context context) {
        this(context, null);
    }

    public ViewerPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void init(DisplayMetrics metrics) {
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;

        dt = FirebaseDatabase.getInstance().getReference("fingerpath");
        dt.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                action = dataSnapshot.child("action").getValue(Integer.class);
                xx = dataSnapshot.child("xx").getValue(Float.class);
                yy = dataSnapshot.child("yy").getValue(Float.class);
                x = dataSnapshot.child("x").getValue(Float.class);
                y = dataSnapshot.child("y").getValue(Float.class);

                switch (action) {
                    case 0:
                        if((xx != xstart) && (yy != ystart)) {
                            touchStart(xx, yy);
                            Log.d("a", "rrrrrrrrrrrrrrrrrrrrrrrrrrrrr  x:" + x + "    getX:" + xx);
                            invalidate();
                        }
                        break;
                    case 2:
                        if(first){
                            first=false;
                            touchStart(xx, yy);
                            invalidate();
                        }
                        if((x != tmp1) && (y != tmp2)) {
                            Log.d("a", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  x:" + x + "    getX:" + xx);
                            touchMove(x, y);
                            invalidate();
                        }
                        break;
                    case 1:
                        tmp1 = x;
                        tmp2 = y;
                        xstart = xx;
                        ystart = yy;
                        touchUp();
                        invalidate();
                        first=true;
                        Log.d("a", "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE x:"+x);
                        break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });



    }



    public void setStrokeWidth(int stroke){
        strokeWidth = stroke;
    }

    public void removeLast() {
        if(!paths.isEmpty()){
            paths.remove(paths.size()-1);
            invalidate();
        }
    }

    public void color(String s){
        currentColor = Color.parseColor(s);
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;
        paths.clear();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        mCanvas.drawColor(backgroundColor);



        for (FingerPath fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(fp.path, mPaint);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }




    private void touchStart(float x, float y) {
        mPath = new Path();
        FingerPath fp = new FingerPath(currentColor, strokeWidth, mPath);
        paths.add(fp);
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= 4 || dy >= 4) {
            if (mPath != null)
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        if (mPath != null) {
            mPath.lineTo(mX, mY);
        }
    }


}
