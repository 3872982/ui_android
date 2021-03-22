package com.learning.XMode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class XModeView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    public XModeView(Context context) {
        this(context,null,0);
    }

    public XModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE); //描边+填充
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //======PorterDuff.Mode使用场景===========
        //1.ComposeShader
        //2.画笔Paint.setXfermode()
        //3.PorterDuffColorFilter

        //禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        setBackgroundColor(Color.GRAY);

        //离屏绘制
        //不使用离屏绘制，它会考虑到canvas背景层，导致运算结果不正确
        int layerId = canvas.saveLayer(0,0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

//        //目标图
        canvas.drawBitmap(createRectBitmap(mWidth, mHeight), 0, 0, mPaint);
//        //设置混合模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
//        //源图，重叠区域右下角部分
        canvas.drawBitmap(createCircleBitmap(mWidth, mHeight), 0, 0, mPaint);
//        //清除混合模式
        mPaint.setXfermode(null);
//
        canvas.restoreToCount(layerId);
    }


    //画矩形dst
    public Bitmap createRectBitmap(int width,int height){
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint dstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dstPaint.setColor(0xFF66AAFF);
        canvas.drawRect(new Rect(width / 20, height / 3, 2 * width / 3, 19 * height / 20), dstPaint);
        return bitmap;
    }

    //画圆src
    public Bitmap createCircleBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint scrPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scrPaint.setColor(0xFFFFCC44);
        canvas.drawCircle(width * 2 / 3, height / 3, height / 4, scrPaint);
        return bitmap;
    }
}
