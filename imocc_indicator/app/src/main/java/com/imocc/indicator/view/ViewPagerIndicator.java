package com.imocc.indicator.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by diankun on 2016/3/13.
 */
public class ViewPagerIndicator extends LinearLayout {

    /**
     * 绘制三角形的画笔
     */
    private Paint mPaint;
    /**
     * path构成一个三角形
     */
    private Path mPath;
    /**
     * 三角形的宽度
     */
    private int mTriangleWidth;
    /**
     * 三角形的高度
     */
    private int mTriangleHeight;

    /**
     * 三角形的宽度为单个Tab的1/6
     */
    private static final float RADIO_TRIANGEL = 1.0f / 6;

    /**
     * 初始时，三角形指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;

    private static final String TAG = ViewPagerIndicator.class.getSimpleName();

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));//设置线段不是太尖锐
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        Log.i(TAG, "dispatchDraw");

        canvas.save();
        // 画笔平移到正确的位置，是三角形的左边位置
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        Log.i(TAG, "onSizeChanged");
        //每一个Item的宽度
        int tabWidth = w / 3;
        mTriangleWidth = (int) (tabWidth * RADIO_TRIANGEL);
        mTriangleHeight = (int) (mTriangleWidth / 2 / Math.sqrt(2));
        // 初始化三角形
        initTriangle();

        mInitTranslationX = tabWidth / 2 - mTriangleWidth / 2;

        //Log.i(TAG, "tabWidth = " + tabWidth + "\t mTriangleWidth = " + mTriangleWidth + "\t mInitTranslationX = " + mInitTranslationX);
        super.onSizeChanged(w, h, oldw, oldh);

    }

    /**
     * 初始化三角形指示器
     */
    private void initTriangle() {
        mPath = new Path();
        //先移动到(0,0)坐标，然后在x轴移动了三角形的长度，然后移动到三角形的顶点位置
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();
    }

    public void scroll(int position, float positionOffset) {

        Log.i(TAG, "position = " + position + " \t positionOffset = " + positionOffset);
        // 不断改变偏移量，invalidate

        //每一个Item的宽度
        int tabWidth = getWidth() / 3;
        mTranslationX = tabWidth * position + tabWidth * positionOffset;
        invalidate();
    }
}
