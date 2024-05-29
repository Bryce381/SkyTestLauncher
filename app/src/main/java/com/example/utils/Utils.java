package com.example.utils;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;

public class Utils {
    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }
    public static int setColor(float val,int mCount) {
        float one = (255 + 255) / (mCount * 2 / 3);
        int r = 0, g = 0, b = 0;
        if (val < (mCount * 1 / 3)) {
            r = (int) (one * val);
            g = 255;
        } else if (val >= (mCount * 1 / 3) && val < (mCount * 2 / 3)) {
            r = 255;
            g = 255 - (int) ((val - (mCount * 1 / 3)) * one);
        } else {
            r = 255;
        }//最后一个三等分
        return Color.rgb(r, g, b);
    }

    public static int Div(int value) {
        return value / 2;
    }

    private Utils(){}

    /**
     * 中点减去偏移，即drawText的 y 轴位置
     */
    public static float getDrawTextOffsetY(Paint paint) {
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        return ((fontMetrics.bottom - fontMetrics.top) / 2f + fontMetrics.bottom) / 2f;
    }

    /**
     * 计算两点的间距
     */
    public static double getDistance(float x, float y, float cx, float cy) {
        return Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
    }

    /**
     * 计算坐标(x, y)到圆心(cx, cy)形成的角度，角度从0-360，360度就是0度，顺时针增加
     * （x轴向右，y轴向下）若2点重合返回-1;
     */
    public static int calcAngle(float x, float y, float cx, float cy) {
        double resultDegree = 0;

        double vectorX = x - cx; // 点到圆心的X轴向量，X轴向右，向量为(0, vectorX)
        double vectorY = cy - y; // 点到圆心的Y轴向量，Y轴向上，向量为(0, vectorY)
        if (vectorX == 0 && vectorY == 0) {
            // 重合？
            return -1;
        }
        // 点落在X,Y轴的情况这里就排除
        if (vectorX == 0) {
            // 点击的点在Y轴上，Y不会为0的
            if (vectorY > 0) {
                resultDegree = 90;
            } else {
                resultDegree = 270;
            }
        } else if (vectorY == 0) {
            // 点击的点在X轴上，X不会为0的
            if (vectorX > 0) {
                resultDegree = 0;
            } else {
                resultDegree = 180;
            }
        } else {
            // 根据形成的正切值算角度
            double tanXY = vectorY / vectorX;
            double arc = Math.atan(tanXY);
            // degree是正数，相当于正切在四个象限的角度的绝对值
            double degree = Math.abs(arc / Math.PI * 180);
            // 将degree换算为对应x正轴开始的0-360的角度
            if (vectorY < 0 && vectorX > 0) {
                // 右下 0-90
                resultDegree = degree;
            } else if (vectorY < 0 && vectorX < 0) {
                // 左下 90-180
                resultDegree = 180 - degree;
            } else if (vectorY > 0 && vectorX < 0) {
                // 左上 180-270
                resultDegree = 180 + degree;
            } else {
                // 右上 270-360
                resultDegree = 360 - degree;
            }
        }
        return (int) resultDegree;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
