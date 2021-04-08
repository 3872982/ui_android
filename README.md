# ui_android

## 综合案例

#### ====================Paint相关========================

#### Paint常用的方法
```
mPaint.setColor(Color.RED);// 设置颜色
mPaint.setARGB(255, 255, 255, 0); // 设置 Paint对象颜色,范围为0~255
mPaint.setAlpha(200); // 设置alpha不透明度,范围为0~255
mPaint.setAntiAlias(true); // 抗锯齿
mPaint.setStyle(Paint.Style.FILL); //描边效果
mPaint.setStrokeWidth(4);//描边宽度
mPaint.setStrokeCap(Paint.Cap.ROUND); //圆角效果
mPaint.setStrokeJoin(Paint.Join.MITER);//拐角风格
mPaint.setShader(new SweepGradient(200, 200, Color.BLUE, Color.RED)); //设置环形渲染器
mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DARKEN)); //设置图层混合模式
mPaint.setColorFilter(new LightingColorFilter(0x00ffff, 0x000000)); //设置颜色过滤器
mPaint.setFilterBitmap(true); //设置双线性过滤
mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));//设置画笔遮罩滤镜 ,传入度数和样式
mPaint.setTextScaleX(2);// 设置文本缩放倍数
mPaint.setTextSize(38);// 设置字体大小
mPaint.setTextAlign(Paint.Align.LEFT);//对其方式
mPaint.setUnderlineText(true);// 设置下划线
```
##### Paint中Shader的使用
1.LinearGradient 线性渲染
2.RadialGradient 环形渲染
3.SweepGradient 扫描渲染
4.BitmapShader 位图渲染
5.ComposeShader 组合渲染

##### Paint图层混合
> 它将所绘制图形的像素与Canvas中对应位置的像素按照一定规则进行混合，形成新的像素值,从而更新Canvas中最终的像素颜色值。
相关案例：自定义控件-刮刮乐

##### Paint中ColorFilter使用
1. LightingColorFilter
2. PorterDuffColorFilter
3. ColorMatrixColorFilter  通过颜色矩阵我们可以实现很多类似图片复古风、电影风格的效果

#### ====================Canvas相关========================

##### Canvas绘制基础图形
绘制几何图形，文本，位图

##### Canvas位置形状变换
1.平移 translate
2.缩放 scale
3.旋转 rotate
4.倾斜 skew
5.切割 clip
6.矩阵 matrix

相关案例：图片粒子化分解发散效果、自定义Splash过渡动画效果

##### Canvas状态保存与恢复
canvas调用translate，rotate，scale，skew，clip等变换后，后续的操作都是基于变换后的画布进行的，这对后续的操作很不方便，我们可以通过save，saveLayer（离屏绘制），restore，restoreToCount方法来保存恢复状态
 
#### ====================Path相关==========================

##### Path的常用方法
1.点操作
2.线操作
3.绘制矩形
4.绘制圆和椭圆
5.绘制圆弧

案例：QQ弹性小球 - 贝塞尔曲线
##### Direction类
CW：clockwise 顺时针
CCW：counter-clockwise 逆时针

##### Path.op方法
用来运算两个路径的关系
DIFFERENCE，path1减去(path1和path2相交部分)
REVERSE_DIFFERENCE，path2减去(path1和path2相交部分)
INTERSECT，path1和path2相交部分
UNION，path1和path2并集
XOR，与INTERSECT刚好相反

###### 其他方法
reset() 重置Path到初始状态
close() 闭合当前路径
offset(dx,dy) 平移当前路径

#### ====================PathMeasure相关========================

##### 构造方法：
PathMeasure() 创建一个空的PathMeasure
PathMeasure(Path path, boolean forceClosed) 创建 PathMeasure 并关联一个指定的Path

##### 公共方法：
setPath() 关联一个Path
getLength() 获取Path的长度，设置闭合的话长度可能会比实际的长
nextContour() 跳到下一个轮廓
getSegment() 截取片段
getPosTan() 获取指定长度的位置坐标及该点切线值
getMatrix() 获取指定长度的位置坐标及该点Matrix

案例：环飞的纸飞机、搜索图标动效
