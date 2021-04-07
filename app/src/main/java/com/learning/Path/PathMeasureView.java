package com.learning.Path;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.learning.ui.R;

public class PathMeasureView extends View {

    private Path mPath;
    private Paint mPaint;
    private Paint mHelpPaint;
    private Paint mDistPaint;
    private PathMeasure mPathMeasure;
    private Path mdistPath;

    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作

    public PathMeasureView(Context context) {
        this(context,null);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPath = new Path();  //源path
        mdistPath = new Path();  //目标path

        //辅助线画笔
        mHelpPaint = new Paint();
        mHelpPaint.setColor(Color.GRAY);
        mHelpPaint.setStyle(Paint.Style.STROKE);
        mHelpPaint.setStrokeWidth(3);
        mHelpPaint.setAntiAlias(true);

        //源路径画笔
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        mPaint.setAntiAlias(true);

        //目标路径画笔
        mDistPaint = new Paint();
        mDistPaint.setColor(Color.BLUE);
        mDistPaint.setStyle(Paint.Style.STROKE);
        mDistPaint.setStrokeWidth(4);
        mDistPaint.setAntiAlias(true);

        mPathMeasure = new PathMeasure();

        //环形运动箭头相关变量初始化
        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow, options);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        mPath.addRect(50,50,300,300, Path.Direction.CW);
//        canvas.drawPath(mPath,mPaint);

//        mPathMeasure.setPath(mPath,false);
//        Log.e("mPathMeasure.getLength","length="+mPathMeasure.getLength());

        //先画个辅助线坐标轴
        canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,mHelpPaint);
        canvas.drawLine(getWidth()/2,0,getWidth()/2,getHeight(),mHelpPaint);

        //平移画布，让坐标原点处于中心位置
        canvas.translate(getWidth()/2,getHeight()/2);

        //==================================getSegement使用======================================================
//        mPath.addRect(-200,-200,200,200, Path.Direction.CW);
//
//        mPathMeasure.setPath(mPath,false);
//        mPathMeasure.getSegment(200,600,mdistPath,true);
//        canvas.drawPath(mPath,mPaint);
//        canvas.drawPath(mdistPath,mDistPaint);

        //=================================getPosTan实现飞机绕圆============================================
        mPath.addCircle(0, 0, 200, Path.Direction.CW);           // 添加一个圆形

        mPathMeasure.setPath(mPath,false);     // 创建 PathMeasure

        currentValue += 0.005;               // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }


        /**
        mPathMeasure.getPosTan(mPathMeasure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势

        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度

        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2f, mBitmap.getHeight() / 2f);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2f, pos[1] - mBitmap.getHeight() / 2f);   // 将图片绘制中心调整到与当前点重合

        canvas.drawPath(mPath, mPaint);                 // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);          // 绘制箭头

         invalidate();             // 重绘页面

         **/

        //使用getMatrix实现飞机绕飞，使用getMatrix相当于把上面的计算角度的过程给封装了
        // 获取当前位置的坐标以及趋势的矩阵
        mPathMeasure.getMatrix(mPathMeasure.getLength() * currentValue, mMatrix, PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);

        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);   // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)

        canvas.drawPath(mPath, mPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);                     // 绘制箭头

        invalidate();                                                           // 重绘页面

    }
}
