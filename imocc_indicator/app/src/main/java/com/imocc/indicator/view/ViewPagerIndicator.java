package com.imocc.indicator.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imocc.indicator.R;
import com.imocc.indicator.ScreenSizeUtil;

import java.util.List;

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
     * 三角形的最大宽度
     */
    private final int DIMENSION_TRIANGEL_WIDTH_MAX = (int) ((ScreenSizeUtil.getScreenWidth(getContext()) / 3) * RADIO_TRIANGEL);

    /**
     * 初始时，三角形指示器的偏移量
     */
    private int mInitTranslationX;
    /**
     * 手指滑动时的偏移量
     */
    private float mTranslationX;

    /**
     * 默认的Tab数量
     */
    private static final int COUNT_DEFAULT_TAB = 4;
    /**
     * tab的数量
     */
    private int mTabVisiableCount = COUNT_DEFAULT_TAB;

    /**
     * Tab的名称
     */
    private List<String> mTitles;
    /**
     * 对Indicator起作用的ViewPager
     */
    private ViewPager mViewPager;

    /**
     * 对外的ViewPager的回调接口
     */
    private OnPageChangeListener mListener;

    /**
     * TextView默认的颜色
     */
    private static final int COLOR_TEXT_NORMAL = 0x77FFFFFF;
    /**
     * TextView高亮的颜色
     */
    private static final int COLOR_TEXT_HIGHLIGHT = 0xFFFFFFFF;


    private static final String TAG = ViewPagerIndicator.class.getSimpleName();

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisiableCount = a.getInt(R.styleable.ViewPagerIndicator_visiable_tab_count, COUNT_DEFAULT_TAB);
        if (mTabVisiableCount < 0) {
            mTabVisiableCount = COUNT_DEFAULT_TAB;
        }
        a.recycle();

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));//设置线段不是太尖锐
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        //Log.i(TAG, "dispatchDraw");

        canvas.save();
        // 画笔平移到正确的位置，是三角形的左边位置
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

        super.dispatchDraw(canvas);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        //Log.i(TAG, "onSizeChanged");
        //每一个Item的宽度
        int tabWidth = w / mTabVisiableCount;
        mTriangleWidth = (int) (tabWidth * RADIO_TRIANGEL);
        //设置不超过最大宽度
        mTriangleWidth = Math.min(mTriangleWidth, DIMENSION_TRIANGEL_WIDTH_MAX);
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


    /**
     * 布局加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int childCount = getChildCount();

        if (childCount == 0) return;

        //子元素的个数大于要显示的个数
        for (int i = 0; i < childCount; i++) {
            TextView textView = (TextView) getChildAt(i);
            LinearLayout.LayoutParams params = (LayoutParams) textView.getLayoutParams();
            //设置weight为0
            params.weight = 0;
            params.width = ScreenSizeUtil.getScreenWidth(getContext()) / mTabVisiableCount;
        }

        //设置点击事件
        setItemClickEvent();
    }

    public void scroll(int position, float positionOffset) {

        //Log.i(TAG, "position = " + position + " \t positionOffset = " + positionOffset);
        // 不断改变偏移量，invalidate

        //每一个Item的宽度
        int tabWidth = getWidth() / mTabVisiableCount;
        mTranslationX = tabWidth * position + (int) (positionOffset * tabWidth);

        /**
         * 注意：使用 (int) (positionOffset * tabWidth) 而不是 (int) positionOffset * tabWidth
         */

        //容器移动，在tab移动到可见个数时外
        if (position >= (mTabVisiableCount - 2) && positionOffset > 0 && getChildCount() > mTabVisiableCount) {

            //要考虑到mTabVisiableCount ==1 特殊处理
            if (mTabVisiableCount == 1) {

                this.scrollTo(position * tabWidth + (int) (positionOffset * tabWidth), 0);

            } else {

                //移动到指定位置
                this.scrollTo((position - (mTabVisiableCount - 2)) * tabWidth + (int) (positionOffset * tabWidth), 0);

            }
        }

        invalidate();
    }


    /**
     * 设置可见Tab的个数，注意 要在setTabItemTitles之前调用，生成TextView时用到了mTabVisiableCount
     *
     * @param count
     */
    public void setVisiableTabCount(int count) {
        this.mTabVisiableCount = count;
    }

    /**
     * 占用了ViewPager的接口，重新对外的ViewPager的回调接口
     */
    public interface OnPageChangeListener {

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    /**
     * 对外的ViewPager的回调接口的设置
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置ViewPager,并且移动到指定位置
     *
     * @param viewPager
     * @param position
     */
    public void setViewPager(ViewPager viewPager, int position) {

        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset + position*tabWidth
                scroll(position, positionOffset);

                if (mListener != null) {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {

                if (mListener != null) {
                    mListener.onPageSelected(position);
                }
                //高亮选中文本
                highLightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                if (mListener != null) {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        //移动到指定位置
        mViewPager.setCurrentItem(position);
        //高亮选中文本
        highLightTextView(position);
    }


    /**
     * 设置tab的标题内容 可选，可以自己在布局文件中写死
     *
     * @param titles
     */
    public void setTabItemTitles(List<String> titles) {

        // 如果传入的list有值，则移除布局文件中设置的view
        if (titles != null && titles.size() > 0) {

            removeAllViews();
            this.mTitles = titles;

            for (String title : titles) {
                // 添加view
                addView(generateTextView(title));
            }

        }
        //设置点击事件
        setItemClickEvent();

    }


    /**
     * 根据Title创建Tab
     *
     * @param title
     * @return
     */
    public TextView generateTextView(String title) {

        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = ScreenSizeUtil.getScreenWidth(getContext()) / mTabVisiableCount;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(COLOR_TEXT_NORMAL);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 重置Tab文本颜色
     */
    private void resetText() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 高亮某个Tab的颜色
     *
     * @param position
     */
    private void highLightTextView(int position) {
        resetText();
        View view = getChildAt(position);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }


    /**
     * 设置点击事件
     */
    public void setItemClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }


    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }
}
