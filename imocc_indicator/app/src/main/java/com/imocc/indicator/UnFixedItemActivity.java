package com.imocc.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.imocc.indicator.view.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by diankun on 2016/3/14.
 */
public class UnFixedItemActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private ViewPagerIndicator mIndicator;

    private List<String> mTitle = Arrays.asList("短信1", "收藏2", "推荐3", "短信4", "收藏5", "推荐6", "短信7", "收藏8", "推荐9");
    private List<FooFragment> mFragments = new ArrayList<FooFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fixeditem);

        initDatas();
        initView();

        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //tabWidth*positionOffset + position*tabWidth
                mIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDatas() {
        for (int i = 0; i < mTitle.size(); i++) {
            FooFragment fooFragment = FooFragment.newInstance(mTitle.get(i));
            mFragments.add(fooFragment);
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.viewpagerindicator);

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
    }
}
