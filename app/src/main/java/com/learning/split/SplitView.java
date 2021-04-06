package com.learning.split;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.renderscript.Sampler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.learning.ui.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 实现图片粒子化爆炸效果
 * 思路：获取图片每个像素的颜色，并绘画小球ball，并对小球实现动画效果
 */
public class SplitView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;
    private float d = 3;//粒子直径
    private List<Ball> mBalls = new ArrayList<>();
    private ValueAnimator mValueAnimator;

    public SplitView(Context context) {
        this(context,null,0);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);

        for(int i=0;i<mBitmap.getWidth();i++){
            for(int j=0;j<mBitmap.getHeight();j++){
                Ball ball = new Ball();
                ball.color = mBitmap.getPixel(i, j);
                ball.x = i*d + d/2;
                ball.y = j*d + d/2;
                ball.r = d/2;

                //-20 - 20 之间随机数
                ball.vX = (float) (Math.random()*20*Math.pow(-1,Math.ceil(Math.random()*100)));
                ball.vY = randInt(-15,35);

                ball.aX = 0;
                ball.aY = 0.78f;
                mBalls.add(ball);
            }
        }

        //添加属性动画
        mValueAnimator = ValueAnimator.ofFloat(0,1);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBalls();
                invalidate();
            }
        });
    }

    //更新balls的位置,速度状态
    private void updateBalls() {
        for (Ball ball : mBalls) {
            ball.x = ball.x + ball.vX;
            ball.y = ball.y + ball.vY;

            ball.vX = ball.vX + ball.aX;
            ball.vY = ball.vY + ball.aY;
        }
    }

    private int randInt(int min, int max) {
        int min_value = Math.min(min,max)-1;
        int max_value = Math.max(min,max);

        return (int) (min + Math.ceil(Math.random()*(max - min)));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(500,500);
        //画出所有粒子
        for (Ball ball : mBalls) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x,ball.y,ball.r,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mValueAnimator.start();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
