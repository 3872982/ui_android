package com.learning.XMode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.learning.ui.R;

/**
 * 刮刮乐效果
 * 思路，3层图层
 * 结果层 —— 放结果，透明层 —— 绘制手指路径，源头层 —— 放置顶部图片
 */
public class XfermodeEraserView extends View {

    private Bitmap mTxtBitmap;
    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;
    private Path mPath;
    private Paint mPaint;

    public XfermodeEraserView(Context context) {
        this(context,null,0);
    }

    public XfermodeEraserView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XfermodeEraserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        //禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mTxtBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.result);
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eraser);
        mDstBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //路径（贝塞尔曲线）
        mPath = new Path();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //将结果先绘制到画布上
        canvas.drawBitmap(mTxtBitmap,0,0,mPaint);

        //离屏绘制
        int layerId = canvas.saveLayer(0,0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        //现将路径画到目标图上
        Canvas tempCanvas = new Canvas(mDstBitmap);
        tempCanvas.drawPath(mPath,mPaint);
//        //目标图
        canvas.drawBitmap(mDstBitmap, 0, 0, mPaint);
//        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//        //源图，重叠区域右下角部分
        canvas.drawBitmap(mSrcBitmap, 0, 0, mPaint);
//        //清除混合模式
        mPaint.setXfermode(null);
//
        canvas.restoreToCount(layerId);
    }

    float startX,startY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                mPath.moveTo(startX,startY);
                break;
            case MotionEvent.ACTION_MOVE:
//                float endX = (event.getX()-startX)/2 + startX;
//                float endY = (event.getY()-startY)/2 + startY;
                float endX = event.getX();
                float endY = event.getY();
                mPath.quadTo(startX,startY,endX,endY);
                startX = event.getX();
                startY = event.getY();
                break;
        }

        //刷新视图，重新调用draw
        invalidate();

        //消费掉事件，不需要继续往下传递了
        return true;
    }
}
