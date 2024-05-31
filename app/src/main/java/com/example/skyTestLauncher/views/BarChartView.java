package com.example.skyTestLauncher.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.SkyTestLauncher.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class BarChartView extends View {
    private Context mContext;

    private Paint mPaintBar;
    private Paint mPaintLline;
    private Paint mPaintText;
    //柱状条对应的颜色数组
    private int colors;
    private int keduTextSpace = 80;//刻度与文字之间的间距
    private int keduWidth = 40; //坐标轴上横向标识线宽度
    private int keduSpace = 500; //每个刻度之间的间距 px
    private int itemSpace = 200;//柱状条之间的间距
    private int itemWidth = 200;//柱状条的宽度
    //刻度递增的值
    private int valueSpace = 4;
    //绘制柱形图的坐标起点
    private int startX;
    private int startY;
    private int mTextSize = 25;
    private int mMaxTextWidth;
    private int mMaxTextHeight;
    private Rect mXMaxTextRect;
    private Rect mYMaxTextRect;
    //是否要展示柱状条对应的值
    private boolean isShowValueText = true;
    //数据值
    private List<Integer> mData = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();
    private List<String> xAxisList = new ArrayList<>();


    // ... 其他代码 ...

    public Resources getResources() {
        return getContext().getResources();
    }

    private float pxToDp(float px) {
        return px / getResources().getDisplayMetrics().density;
    }

    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        colors = ContextCompat.getColor(context, R.color.light_blue_700);
        init(context, false);
    }

    private void init(Context context, boolean isUpdate) {
        if (!isUpdate) {
            initData();
        }

        keduTextSpace = (int) pxToDp(keduTextSpace);
        keduWidth = (int) pxToDp(keduWidth);
        keduSpace = (int) pxToDp(keduSpace);
        itemSpace = (int) pxToDp(itemSpace);
        itemWidth = (int) pxToDp(itemWidth);

        //设置边缘特殊效果
        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(
                1, BlurMaskFilter.Blur.INNER);
        //绘制柱状图的画笔
        mPaintBar = new Paint();
        mPaintBar.setStyle(Paint.Style.FILL);
        mPaintBar.setStrokeWidth(4);
        mPaintBar.setMaskFilter(PaintBGBlur);
        //绘制直线的画笔
        mPaintLline = new Paint();
        mPaintLline.setColor(ContextCompat.getColor(context, R.color.white));
        mPaintLline.setAntiAlias(true);
        mPaintLline.setStrokeWidth(2);

        //绘制文字的画笔
        mPaintText = new Paint();
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(ContextCompat.getColor(context, R.color.black));
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(1);

        mYMaxTextRect = new Rect();
        mXMaxTextRect = new Rect();
        mPaintText.getTextBounds(Integer.toString(yAxisList.get(yAxisList.size() - 1)), 0, Integer.toString(yAxisList.get(yAxisList.size() - 1)).length(), mYMaxTextRect);
        mPaintText.getTextBounds(xAxisList.get(xAxisList.size() - 1), 0, xAxisList.get(xAxisList.size() - 1).length(), mXMaxTextRect);
        //绘制的刻度文字的最大值所占的宽高
        mMaxTextWidth = mYMaxTextRect.width() > mXMaxTextRect.width() ? mYMaxTextRect.width() : mXMaxTextRect.width();
        mMaxTextHeight = mYMaxTextRect.height() > mXMaxTextRect.height() ? mYMaxTextRect.height() : mXMaxTextRect.height();

        if (yAxisList.size() >= 2) {
            valueSpace = yAxisList.get(1) - yAxisList.get(0);
        }
        //文字+刻度宽度+文字与刻度之间间距
        startX = mMaxTextWidth + keduWidth + keduTextSpace;
        //坐标原点 y轴起点
        startY = keduSpace * (yAxisList.size() - 1) + mMaxTextHeight + (isShowValueText ? keduTextSpace : 0);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        int[] data = {0,4,8,12,16,20,0,4,8,12,16,20,1};
        for (int i = 0; i < data.length; i++) {
            mData.add(data[i]);
        }
        for (int i = 0; i <= 5; i++) {
            yAxisList.add(i * valueSpace);
        }
        String[] xAxis = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        for (int i = 0; i < xAxis.length; i++) {
            xAxisList.add(xAxis[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "onMeasure()");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.AT_MOST) {
            if (keduWidth > mMaxTextHeight + keduTextSpace) {
                heightSize = (yAxisList.size() - 1) * keduSpace + keduWidth + mMaxTextHeight;
            } else {
                heightSize = (yAxisList.size() - 1) * keduSpace + (mMaxTextHeight + keduTextSpace) + mMaxTextHeight;
            }
            heightSize = heightSize + keduTextSpace + (isShowValueText ? keduTextSpace : 0);//x轴刻度对应的文字距离底部的padding:keduTextSpace
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = startX + mData.size() * itemWidth + (mData.size() + 1) * itemSpace;
        }
        Log.e("TAG", "heightSize=" + heightSize + "widthSize=" + widthSize);
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TAG", "onDraw()");

        //从下往上绘制Y 轴
        //canvas.drawLine(startX, startY + keduWidth, startX, startY - (yAxisList.size() - 1) * keduSpace, mPaintLline);

        for (int i = 0; i < yAxisList.size(); i++) {

            //绘制Y轴的文字
            Rect textRect = new Rect();

            //mPaintText.getTextBounds(Integer.toString(yAxisList.get(i)), 0, Integer.toString(yAxisList.get(i)).length(), textRect);
            canvas.drawText(Integer.toString(yAxisList.get(i)), (startX - keduWidth) - textRect.width() - keduTextSpace, startY - (i + 1) * keduSpace + keduSpace, mPaintText);

            //画X轴及上方横向的刻度线
            canvas.drawLine(startX - keduWidth, startY - keduSpace * i, startX + mData.size() * itemWidth + itemSpace * (mData.size() + 1), startY - keduSpace * i, mPaintLline);

        }

        for (int j = 0; j < xAxisList.size(); j++) {
            //绘制X轴的文字
            Rect rect = new Rect();
            mPaintText.getTextBounds(xAxisList.get(j), 0, xAxisList.get(j).length(), rect);
            canvas.drawText(xAxisList.get(j), startX + itemSpace * (j + 1) + itemWidth * j + itemWidth / 2 - rect.width() / 2, startY + rect.height() + keduTextSpace, mPaintText);

            if (isShowValueText) {
                Rect rectText = new Rect();
                mPaintText.getTextBounds(mData.get(j) + "", 0, (mData.get(j) + "").length(), rectText);
                //绘制柱状条上的值
                canvas.drawText(mData.get(j) + "", startX + itemSpace * (j + 1) + itemWidth * j + itemWidth / 2 - rectText.width() / 2, (float) (startY - keduTextSpace - (mData.get(j) * (keduSpace * 1.0 / valueSpace))), mPaintText);
            }
            //绘制柱状条
            mPaintBar.setColor(colors);
            //(mData.get(j) * (keduSpace * 1.0 / valueSpace))：为每个柱状条所占的高度值px
            int initx = startX + itemSpace * (j + 1) + j * itemWidth;
            canvas.drawRect(initx, (float) (startY - (mData.get(j) * (keduSpace * 1.0 / valueSpace))), initx + itemWidth, startY, mPaintBar);
        }
    }

    /**
     * 根据真实的数据刷新界面
     *
     * @param datas
     * @param xList
     * @param yList
     */
    public void updateValueData(@NotNull List<Integer> datas, @NotNull List<String> xList, @NotNull List<Integer> yList) {
        this.mData = datas;
        this.xAxisList = xList;
        this.yAxisList = yList;
        init(mContext, true);
        invalidate();
    }
}

