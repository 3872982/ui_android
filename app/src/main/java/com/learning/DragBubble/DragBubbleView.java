package com.learning.DragBubble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.learning.ui.R;

/**
 * provided by Richen.Xu
 * 类似于QQ的气泡效果
 * 思路：两个圆球以及两个圆球切点之间的二阶贝塞尔曲线
 * 主要分为4个状态：默认状态，链接状态，分离状态，消失状态
 */
public class DragBubbleView extends View {

    /**
     * 气泡默认状态--静止
     */
    private final int BUBBLE_STATE_DEFAULT = 0;
    /**
     * 气泡相连
     */
    private final int BUBBLE_STATE_CONNECT = 1;
    /**
     * 气泡分离
     */
    private final int BUBBLE_STATE_APART = 2;
    /**
     * 气泡消失
     */
    private final int BUBBLE_STATE_DISMISS = 3;

    /**
     * 气泡半径
     */
    private float mBubbleRadius;
    /**
     * 气泡颜色
     */
    private int mBubbleColor;
    /**
     * 气泡消息文字
     */
    private String mTextStr;
    /**
     * 气泡消息文字颜色
     */
    private int mTextColor;
    /**
     * 气泡消息文字大小
     */
    private float mTextSize;
    /**
     * 不动气泡的半径
     */
    private float mBubFixedRadius;
    /**
     * 可动气泡的半径
     */
    private float mBubMovableRadius;
    /**
     * 不动气泡的圆心
     */
    private PointF mBubFixedCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMovableCenter;
    /**
     * 气泡的画笔
     */
    private Paint mBubblePaint;
    /**
     * 贝塞尔曲线path
     */
    private Path mBezierPath;

    private Paint mTextPaint;

    //文本绘制区域
    private Rect mTextRect;

    private Paint mBurstPaint;

    //爆炸绘制区域
    private Rect mBurstRect;

    /**
     * 气泡状态标志
     */
    private int mBubbleState = BUBBLE_STATE_DEFAULT;
    /**
     * 两气泡圆心距离
     */
    private float mDist;
    /**
     * 气泡相连状态最大圆心距离
     */
    private float mMaxDist;
    /**
     * 手指触摸偏移量
     */
    private final float MOVE_OFFSET;

    /**
     * 气泡爆炸的bitmap数组
     */
    private Bitmap[] mBurstBitmapsArray;
    /**
     * 是否在执行气泡爆炸动画
     */
    private boolean mIsBurstAnimStart = false;

    /**
     * 当前气泡爆炸图片index
     */
    private int mCurDrawableIndex;

    /**
     * 气泡爆炸的图片id数组
     */
    private int[] mBurstDrawablesArray = {R.mipmap.burst_1, R.mipmap.burst_2, R.mipmap.burst_3, R.mipmap.burst_4, R.mipmap.burst_5};


    public DragBubbleView(Context context) {
        this(context,null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragBubbleView, defStyleAttr, 0);
        mBubbleRadius = DisplayUtils.dip2px(context,array.getDimension(R.styleable.DragBubbleView_bubble_radius, mBubbleRadius));
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text);
        mTextSize = DisplayUtils.sp2px(context,array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize));
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();

        this.setClickable(true);

        //两个圆半径大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;

        MOVE_OFFSET = mMaxDist / 4;

        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBubblePaint.setStyle(Paint.Style.FILL);
        mBezierPath = new Path();

        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextRect = new Rect();

        //爆炸画笔
        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBurstPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstDrawablesArray.length; i++) {
            //将气泡爆炸的drawable转为bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
            mBurstBitmapsArray[i] = bitmap;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w, h);
    }

    //初始化宽高，从而设定固定圆，移动圆的初始坐标
    private void init(int w, int h) {
        mBubbleState = BUBBLE_STATE_DEFAULT;

        //Log.e("test","W:"+w+"h:"+h);

        //设置固定气泡圆心初始坐标
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w / 2, h / 2);
        } else {
            mBubFixedCenter.set(w / 2, h / 2);
        }
        //设置可动气泡圆心初始坐标
        if (mBubMovableCenter == null) {
            mBubMovableCenter = new PointF(w / 2, h / 2);
        } else {
            mBubMovableCenter.set(w / 2, h / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //1. 连接情况绘制贝塞尔曲线  2.绘制圆背景以及文本 3.另外端点绘制一个圆
        //1. 静止状态  2，连接状态 3，分离状态  4，消失
        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制静止的气泡
            canvas.drawCircle(mBubFixedCenter.x, mBubFixedCenter.y, mBubFixedRadius, mBubblePaint);
            //计算控制点的坐标
            int iAnchorX = (int) ((mBubMovableCenter.x + mBubFixedCenter.x) / 2);
            int iAnchorY = (int) ((mBubMovableCenter.y + mBubFixedCenter.y) / 2);

            float sinTheta = (mBubMovableCenter.y - mBubFixedCenter.y) / mDist;
            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;

            //D
            float iBubFixedStartX = mBubFixedCenter.x - mBubFixedRadius * sinTheta;
            float iBubFixedStartY = mBubFixedCenter.y + mBubFixedRadius * cosTheta;
            //C
            float iBubMovableEndX = mBubMovableCenter.x - mBubMovableRadius * sinTheta;
            float iBubMovableEndY = mBubMovableCenter.y + mBubMovableRadius * cosTheta;

            //A
            float iBubFixedEndX = mBubFixedCenter.x + mBubFixedRadius * sinTheta;
            float iBubFixedEndY = mBubFixedCenter.y - mBubFixedRadius * cosTheta;
            //B
            float iBubMovableStartX = mBubMovableCenter.x + mBubMovableRadius * sinTheta;
            float iBubMovableStartY = mBubMovableCenter.y - mBubMovableRadius * cosTheta;

            mBezierPath.reset();
            mBezierPath.moveTo(iBubFixedStartX, iBubFixedStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubMovableEndX, iBubMovableEndY);

            mBezierPath.lineTo(iBubMovableStartX, iBubMovableStartY);
            mBezierPath.quadTo(iAnchorX, iAnchorY, iBubFixedEndX, iBubFixedEndY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath, mBubblePaint);
        }

        //这个写在后面，保证移动圆处在上方不会被遮盖
        if(mBubbleState != BUBBLE_STATE_DISMISS){
            canvas.drawCircle(mBubMovableCenter.x,mBubMovableCenter.y,mBubMovableRadius,mBubblePaint);
            mTextPaint.getTextBounds(mTextStr,0,mTextStr.length(),mTextRect);
            canvas.drawText(mTextStr,mBubMovableCenter.x,mBubMovableCenter.y+mTextRect.height()/2,mTextPaint);
        }

        //在爆炸消失过程中
        if(mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length){

            mBurstRect.set((int)(mBubMovableCenter.x - mBubMovableRadius),
                    (int)(mBubMovableCenter.y - mBubMovableRadius),
                    (int)(mBubMovableCenter.x + mBubMovableRadius),
                    (int)(mBubMovableCenter.y + mBubMovableRadius));
            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex],null,mBurstRect,mBurstPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mBubbleState != BUBBLE_STATE_DISMISS){
                    mDist = (float) Math.hypot(event.getX()-mBubFixedCenter.x,event.getY()-mBubFixedCenter.y);
                    if(mDist <= mBubFixedRadius + MOVE_OFFSET){
                        //如果在这个范围内，则认为是可以拖拽的
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    }else{
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(mBubbleState != BUBBLE_STATE_DEFAULT){
                    mDist = (float) Math.hypot(event.getX()-mBubFixedCenter.x,event.getY()-mBubFixedCenter.y);
                    mBubMovableCenter.x = event.getX();
                    mBubMovableCenter.y = event.getY();
                    if(mBubbleState == BUBBLE_STATE_CONNECT){
                        if(mDist < mMaxDist - MOVE_OFFSET){
                            mBubFixedRadius = mBubbleRadius - mDist/8;
                        }else{
                            mBubbleState = BUBBLE_STATE_APART;
                        }
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mBubbleState == BUBBLE_STATE_CONNECT){
                    //橡皮筋回弹动画
                    startBubbleRestAnim();
                }else if(mBubbleState == BUBBLE_STATE_APART){
                    if(mDist <= mBubbleRadius*2){
                        startBubbleRestAnim();
                    }else{
                        //爆炸效果
                        startBubbleBurstAnim();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 爆炸效果:就是4张图片的按续播放
     */
    private void startBubbleBurstAnim() {
        mBubbleState = BUBBLE_STATE_DISMISS;

        ValueAnimator anim = ValueAnimator.ofInt(0,mBurstDrawablesArray.length);
        anim.setDuration(500);
        anim.setInterpolator(new LinearInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurDrawableIndex = (int) animation.getAnimatedValue();
                //Log.e("test","mCurDrawableIndex"+mCurDrawableIndex);
                invalidate();
            }
        });

        anim.start();
    }

    /**
     * 回弹效果：从A点移动到B点的动画效果
     */
    private void startBubbleRestAnim() {
        ValueAnimator anim = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubMovableCenter.x,mBubMovableCenter.y),
                new PointF(mBubFixedCenter.x,mBubFixedCenter.y));
        anim.setDuration(200);
        anim.setInterpolator(new OvershootInterpolator(3f));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBubMovableCenter = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBubbleState = BUBBLE_STATE_DEFAULT;
            }
        });
        anim.start();
    }
}
