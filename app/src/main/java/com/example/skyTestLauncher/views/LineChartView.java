package com.example.skyTestLauncher.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.SkyTestLauncher.R;
import com.example.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JoJo on 2018/8/3.
 * wechat:18510829974
 * description: 曲线图/折线图
 */

public class LineChartView extends View {
    private Context mContext;
    //绘制坐标轴的画笔
    private Paint mAxisPaint;
    //绘制曲线的画笔
    private Paint mPaint;
    //绘制网格线的画笔
    private Paint gridPaint;
    //绘制X轴上方的画笔
    private Paint mXAxisLinePaint;
    private Paint mPaintText;
    private Paint mShadowPaint;
    //向上的曲线图的绘制起点(px)
    private int startX;
    private int startY;
    //向下的曲线图的绘制起点(px)
    private int downStartX;
    private int downStartY;
    //上方Y轴每单位刻度所占的像素值
    private float YAxisUpUnitValue;
    //下方Y轴每单位刻度所占的像素值
    private float YAxisDownUnitValue;
    //根据具体传入的数据，在坐标轴上绘制点
    private Point[] mPoints;
    //传入的数据，决定绘制的纵坐标值
    private List<Integer> mDatas = new ArrayList<>();
    //Y轴刻度集合
    private List<Integer> mYAxisList = new ArrayList<>();
    //X轴刻度集合
    private List<String> mXAxisList = new ArrayList<>();
    //X轴的绘制距离
    private int mXAxisMaxValue;
    //Y轴的绘制距离
    private int mYAxisMaxValue;
    //Y轴刻度间距(px)
    private int yAxisSpace = 40;
    //X轴刻度间距(px)
    private int xAxisSpace = 40;
    private int mKeduWidth = 10;
    private float keduTextSize = 10;
    //刻度值距离坐标的padding距离
    private int textPadinng = 5;
    //Y轴递增的实际值
    private int yIncreaseValue;
    //true：绘制曲线 false：折线
    private boolean isCurve = true;
    private Rect mYMaxTextRect;
    private Rect mXMaxTextRect;
    private int mMaxTextHeight;
    private int mMaxTextWidth;

    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LineChartView, defStyleAttr, 0);

        // 获取自定义属性 curveColor 的值
        int curveColor = a.getColor(R.styleable.LineChartView_curveColor, ContextCompat.getColor(context, R.color.gray_500)); // 默认颜色需定义
        a.recycle();
        initData();
        initView();

        // 设置曲线颜色
        mPaint.setColor(curveColor);
    }

    /**
     * 测量视图的大小。重写此方法以自定义视图的测量逻辑。
     * @param widthMeasureSpec 宽度的度量规范，包含模式和大小两部分信息。
     * @param heightMeasureSpec 高度的度量规范，同样包含模式和大小两部分信息。
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 解析宽度和高度的度量模式及大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 根据高度的度量模式，计算并调整高度
        if (heightMode == MeasureSpec.AT_MOST) {
            // 当高度模式为AT_MOST时，根据Y轴标签数量、标签高度和间距来计算高度
            heightSize = (mYAxisList.size() - 1) * yAxisSpace + mMaxTextHeight * 2 + textPadinng * 2;
        }

        // 根据宽度的度量模式，计算并调整宽度
        if (widthMode == MeasureSpec.AT_MOST) {
            // 当宽度模式为AT_MOST时，根据数据集大小、X轴间距和最大文本宽度来计算宽度
            widthSize = startX + (mDatas.size() - 1) * xAxisSpace + mMaxTextWidth;
        }

        // 设置测量结果
        setMeasuredDimension(widthSize, heightSize);
    }


    private void initView() {
        //初始化画笔
        mPaint = new Paint();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.light_blue_700));
        //线条的粗细控制
        mPaint.setStrokeWidth(6);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //绘制X,Y轴坐标的画笔
        mAxisPaint = new Paint();
        mAxisPaint.setColor(ContextCompat.getColor(mContext, R.color.color_274782));
        mAxisPaint.setStrokeWidth(2);
        mAxisPaint.setAntiAlias(true);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        //绘制坐标轴上方的横线的画笔
        mXAxisLinePaint = new Paint();
        mXAxisLinePaint.setColor(ContextCompat.getColor(mContext, R.color.color_274782));
        mXAxisLinePaint.setStrokeWidth(1);
        mXAxisLinePaint.setAntiAlias(true);
        mXAxisLinePaint.setStyle(Paint.Style.STROKE);

        // 新增：初始化网格线画笔
        gridPaint = new Paint();
        gridPaint.setColor(ContextCompat.getColor(mContext, R.color.gray_500)); // 选择一个合适的灰色
        gridPaint.setStrokeWidth(1);
        gridPaint.setAntiAlias(true);
        gridPaint.setStyle(Paint.Style.STROKE);

        //绘制刻度值文字的画笔
        mPaintText = new Paint();
        mPaintText.setTextSize(keduTextSize);
        mPaintText.setColor(ContextCompat.getColor(mContext, R.color.color_a9c6d6));
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(1);


        mYMaxTextRect = new Rect();
        mXMaxTextRect = new Rect();
        mPaintText.getTextBounds(Integer.toString(mYAxisList.get(mYAxisList.size() - 1)), 0, Integer.toString(mYAxisList.get(mYAxisList.size() - 1)).length(), mYMaxTextRect);
        mPaintText.getTextBounds(mXAxisList.get(mXAxisList.size() - 1), 0, mXAxisList.get(mXAxisList.size() - 1).length(), mXMaxTextRect);
        //绘制的刻度文字的最大值所占的宽高
        mMaxTextWidth = mYMaxTextRect.width() > mXMaxTextRect.width() ? mYMaxTextRect.width() : mXMaxTextRect.width();
        mMaxTextHeight = mYMaxTextRect.height() > mXMaxTextRect.height() ? mYMaxTextRect.height() : mXMaxTextRect.height();


        //指定绘制的起始位置
        startX = mMaxTextWidth + textPadinng + mKeduWidth;
        //坐标原点Y的位置（+1的原因：X轴画笔的宽度为2 ; +DP2PX.dip2px(mContext, 5)原因：为刻度文字所占的超出的高度 ）——>解决曲线画到最大刻度值时，显示高度不够，曲线显示扁扁的问题
        startY = yAxisSpace * (mYAxisList.size() - 1) + mMaxTextHeight;

        if (mYAxisList.size() >= 2) {
            yIncreaseValue = mYAxisList.get(1) - mYAxisList.get(0);
        }
        //X轴绘制距离
        mXAxisMaxValue = (mDatas.size() - 1) * xAxisSpace;
        //Y轴绘制距离
        mYAxisMaxValue = (mYAxisList.size() - 1) * yAxisSpace;

        //坐标起始点Y轴高度=(startY+mKeduWidth)  下方文字所占高度= DP2PX.dip2px(mContext, keduTextSize)
        int viewHeight = startY + 2 * mKeduWidth + Utils.dip2px(mContext, keduTextSize);
        //viewHeight=121
        Log.e("TAG", "viewHeight=" + viewHeight);
    }

    /**
     * 根据传入的数据，确定绘制的点
     *
     * @return
     */
    private Point[] initPoint() {
        Point[] points = new Point[mDatas.size()];
        for (int i = 0; i < mDatas.size(); i++) {
            Integer ybean = mDatas.get(i);
            int drawHeight = (int) (startY * 1.0 - (ybean * yAxisSpace * 1.0 / yIncreaseValue));
            int startx = startX + xAxisSpace * i;
            points[i] = new Point(startx, drawHeight);
        }
        Log.e("TAG", "startX=" + startX + "---startY=" + startY);
        return points;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPoints = initPoint();

        for (int i = 0; i < mYAxisList.size(); i++) {
            //Y轴方向递增的高度
            int yAxisHeight = startY - yAxisSpace * i;
            //绘制X轴和上方横线
            //canvas.drawLine(startX - mKeduWidth, yAxisHeight, startX + (mDatas.size() - 1) * xAxisSpace, yAxisHeight, mXAxisLinePaint);
            //绘制左边Y轴刻度线
//                canvas.drawLine(startX, yAxisHeight, startX - mKeduWidth, yAxisHeight, mAxisPaint);
            //绘制文字时,Y轴方向递增的高度
            int yTextHeight = startY - yAxisSpace * i;
            //绘制Y轴刻度旁边的刻度文字值,10为刻度线与文字的间距
            mPaintText.setTextAlign(Paint.Align.RIGHT);
            //绘制Y轴的刻度文字
            //canvas.drawText(mYAxisList.get(i) + "", (startX - mKeduWidth) - textPadinng, yTextHeight, mPaintText);
        }
        //绘制Y轴
        //canvas.drawLine(startX, startY, startX, startY - mYAxisMaxValue, mAxisPaint);

        //绘制X轴下面显示的文字
        for (int i = 0; i < mXAxisList.size(); i++) {
            int xTextWidth = startX + xAxisSpace * i - mKeduWidth;
            //设置从起点位置的左边对齐绘制文字
            mPaintText.setTextAlign(Paint.Align.LEFT);
            Rect rect = new Rect();
            mPaintText.getTextBounds(mXAxisList.get(i), 0, mXAxisList.get(i).length(), rect);
            //绘制X轴的刻度文字
            //canvas.drawText(mXAxisList.get(i), startX - rect.width() / 2 + xAxisSpace * i, startY + rect.height() + textPadinng, mPaintText);
        }
        //绘制X轴上每个点的垂直线
        for (Point point : mPoints) {
            //canvas.drawLine(point.x, startY - mYAxisMaxValue, point.x, startY, mAxisPaint);
        }

        // 绘制网格线
        for (int i = 0; i < mYAxisList.size(); i++) {
            // Y轴方向递增的高度
            int yAxisHeight = startY - yAxisSpace * i;
            // 绘制水平网格线，但不绘制最顶部和最底部的线
            if (i != 0 && i != mYAxisList.size() - 1) {
                canvas.drawLine(startX, yAxisHeight, startX + (mDatas.size() - 1) * xAxisSpace, yAxisHeight, gridPaint);
            }
        }
        for (int i = 0; i < mXAxisList.size(); i++) {
            int x = startX + xAxisSpace * i;
            // 绘制垂直网格线，但不绘制最左侧和最右侧的线
            if (i != 0 && i != mXAxisList.size() - 1) {
                canvas.drawLine(x, startY, x, startY - mYAxisMaxValue, gridPaint);
            }
        }

        //连接所有的数据点,画曲线

        if (isCurve) {
            //画曲线
            drawScrollLine(canvas);
        } else {
            //画折线
            drawLine(canvas);
        }
    }

    /**
     * 绘制曲线-曲线图
     *
     * @param canvas
     */
    private void drawScrollLine(Canvas canvas) {
        Point startp;
        Point endp;
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            // 继续绘制原来的曲线
            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 绘制直线-折线图
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        Point startp;
        Point endp;
        for (int i = 0; i < mPoints.length - 1; i++) {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private void initData() {
        //外界传入的数据，即为绘制曲线的每个点
        mDatas.add(0);
        mDatas.add(10);
        mDatas.add(5);
        mDatas.add(20);
        mDatas.add(15);
        mDatas.add(23);
        mDatas.add(33);
        mDatas.add(40);
        mDatas.add(18);
        mDatas.add(15);
        mDatas.add(20);
        mDatas.add(22);
        mDatas.add(32);
        mDatas.add(33);
        mDatas.add(34);
        mDatas.add(34);
        mDatas.add(34);
        mDatas.add(35);
        mDatas.add(36);
        mDatas.add(37);
        mDatas.add(0);
        mDatas.add(10);

        int[] mYAxisData = new int[]{0, 10, 20, 30, 40};
        for (int i = 0; i < mYAxisData.length; i++) {
            mYAxisList.add(mYAxisData[i]);
        }

        //X轴数据
        mXAxisList.add("01");
        mXAxisList.add("02");
        mXAxisList.add("03");
        mXAxisList.add("04");
        mXAxisList.add("05");
        mXAxisList.add("06");
        mXAxisList.add("07");
        mXAxisList.add("08");
        mXAxisList.add("09");
        mXAxisList.add("10");
        mXAxisList.add("11");
        mXAxisList.add("12");
        mXAxisList.add("13");
        mXAxisList.add("14");
        mXAxisList.add("15");
        mXAxisList.add("16");
        mXAxisList.add("17");
        mXAxisList.add("18");
        mXAxisList.add("19");
        mXAxisList.add("20");
        mXAxisList.add("21");
        mXAxisList.add("22");
    }

    /**
     * 传入数据，重新绘制图表
     *
     * @param datas
     * @param yAxisData
     */
    public void updateData(List<Integer> datas, List<String> xAxisData, List<Integer> yAxisData) {
        this.mDatas = datas;
        this.mXAxisList = xAxisData;
        this.mYAxisList = yAxisData;
        initView();
        postInvalidate();
    }
}
