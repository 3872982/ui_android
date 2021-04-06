package com.learning.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TransformView extends View {

    private Paint mPaint;

    public TransformView(Context context) {
        this(context,null,0);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //======================translate平移===========================
        canvas.drawRect(100,100,500,500,mPaint);
        canvas.translate(200,200);
        mPaint.setColor(Color.RED);
        canvas.drawRect(100,100,500,500,mPaint);

        //============================缩放===============================
        //1.以原点进行缩放
//        canvas.drawRect(0,0,400,400,mPaint);
//        canvas.scale(0.5f,0.5f);
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(0,0,400,400,mPaint);

        //2.以指定点为中心进行缩放
        //canvas.scale(0.5f,0.5f,200,200);
        // 等同于
        // ①先平移（200,200）
        // ②缩小画布0.5f,0.5f
        // ③回移（200,200）的缩放结果(100,100)
//        canvas.drawRect(0,0,400,400,mPaint);
//        canvas.scale(0.5f,0.5f,200,200);  //以坐标200,200为中心进行缩放
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(0,0,400,400,mPaint);


        //============================旋转======================================
//        canvas.translate(200,200);
//        canvas.drawRect(0,0,200,200,mPaint);
//        canvas.rotate(45);  //以原点为中心旋转45度
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(0,0,200,200,mPaint);

//        canvas.translate(200,200);
//        canvas.drawRect(0,0,200,200,mPaint);
//        canvas.rotate(45,100,100);  //以（100,100）为中心旋转45度
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(0,0,200,200,mPaint);


        //============================倾斜======================================
//        canvas.drawRect(0,0, 400, 400, mPaint);
////        canvas.skew(1, 0); //在X方向倾斜45度,Y轴逆时针旋转45
//        canvas.skew(0, 1); //在y方向倾斜45度， X轴顺时针旋转45
//        mPaint.setColor(Color.GRAY);
//        canvas.drawRect(0, 0, 400, 400, mPaint);

        //============================切割======================================
//        canvas.drawRect(200, 200,700, 700, mPaint);
//        mPaint.setColor(Color.GRAY);
//        canvas.drawRect(200, 800,700, 1300, mPaint);
//        canvas.clipRect(200, 200,700, 700); //画布被裁剪
//        canvas.drawCircle(100,100, 100,mPaint); //坐标超出裁剪区域，无法绘制
//        canvas.drawCircle(300, 300, 100, mPaint); //坐标区域在裁剪范围内，绘制成功

//        canvas.drawRect(200, 200,700, 700, mPaint);
//        mPaint.setColor(Color.GRAY);
//        canvas.drawRect(200, 800,700, 1300, mPaint);
//        canvas.clipOutRect(200,200,700,700); //画布裁剪外的区域
//        canvas.drawCircle(100,100,100,mPaint); //坐标区域在裁剪范围内，绘制成功
//        canvas.drawCircle(300, 300, 100, mPaint);//坐标超出裁剪区域，无法绘制

        //============================矩阵======================================
//        canvas.drawRect(0,0,700,700, mPaint);
//        Matrix matrix = new Matrix();
//        matrix.setTranslate(50,50);
////        matrix.setRotate(45);
////        matrix.setScale(0.5f, 0.5f);
//        canvas.setMatrix(matrix);
//        mPaint.setColor(Color.GRAY);
//        canvas.drawRect(0,0,700,700, mPaint);
    }
}
