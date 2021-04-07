package com.learning.Path;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView extends View {

    private Paint mPaint;
    private Path mPath;
    private Paint mTextPaint;

    public PathView(Context context) {
        this(context,null,0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(15);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize(50);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //移动点到 (50 50)
        mPath.moveTo(50, 50);
        //设置线的终点并连线到（200,100）
        mPath.lineTo(200, 100);

        //相对移动（50,0）
        mPath.rMoveTo(50, 0);
        //设置线的终点为相对路径（100,100）并连线
        mPath.rLineTo(100, 100);

        //cw clockwise顺时针   ccw counter-clockwise 逆时针
        mPath.addRect(50, 250, 300, 350, Path.Direction.CW);
        mPath.addRoundRect(50, 400, 300, 500, 20, 10, Path.Direction.CW);
        mPath.addRoundRect(400, 400, 650, 500, new float[]{10, 20, 10, 20, 20, 10, 20, 10},
                Path.Direction.CW);

        mPath.addOval(50, 550, 300, 700, Path.Direction.CW);
        mPath.addCircle(525, 625, 75, Path.Direction.CW);

        mPath.addArc(50, 750, 300, 900, 0, 180);
        mPath.addArc(400, 750, 650, 900, 90, 225);

        mPath.arcTo(50, 950, 300, 1100, 0, 180, true);
        mPath.arcTo(400, 950, 650, 1100, 90, 225, false);

        canvas.drawPath(mPath,mPaint);

        //试验CW CCW
        mPath.reset();
        mPath.addOval(50,1150,300,1300, Path.Direction.CW);
        canvas.drawPath(mPath,mPaint);
        canvas.drawTextOnPath("Richen.Xu",mPath,0,0,mTextPaint);

        mPath.reset();
        mPath.addOval(400,1150,650,1300, Path.Direction.CCW);
        canvas.drawPath(mPath,mPaint);
        canvas.drawTextOnPath("Richen.Xu",mPath,0,0,mTextPaint);

        //==========================Path.op==================================

        mPaint.setStyle(Paint.Style.FILL);
        //DIFFERENCE   path1-path2
        Path path1 = new Path();
        path1.addCircle(150, 1500, 75, Path.Direction.CW);

        Path path2 = new Path();
        path2.addCircle(250, 1500, 75, Path.Direction.CW);

        path1.op(path2, Path.Op.DIFFERENCE);
        canvas.drawPath(path1, mPaint);


        //REVERSE_DIFFERENCE  path2-path1
        path1.reset();
        path2.reset();
        path1.addCircle(450, 1500, 75, Path.Direction.CW);
        path2.addCircle(550, 1500, 75, Path.Direction.CW);

        path1.op(path2, Path.Op.REVERSE_DIFFERENCE);
        canvas.drawPath(path1, mPaint);

        //UNION  path1+path2
        path1.reset();
        path2.reset();
        path1.addCircle(750, 1500, 75, Path.Direction.CW);
        path2.addCircle(850, 1500, 75, Path.Direction.CW);

        path1.op(path2, Path.Op.UNION);
        canvas.drawPath(path1, mPaint);

        //INTERSECT  path1&&path2相交部分
        path1.reset();
        path2.reset();
        path1.addCircle(150, 1700, 75, Path.Direction.CW);
        path2.addCircle(250, 1700, 75, Path.Direction.CW);

        path1.op(path2, Path.Op.INTERSECT);
        canvas.drawPath(path1, mPaint);

        //XOR path1+path2-相交部分
        path1.reset();
        path2.reset();
        path1.addCircle(450, 1700, 75, Path.Direction.CW);
        path2.addCircle(550, 1700, 75, Path.Direction.CW);

        path1.op(path2, Path.Op.XOR);
        canvas.drawPath(path1, mPaint);
    }
}
