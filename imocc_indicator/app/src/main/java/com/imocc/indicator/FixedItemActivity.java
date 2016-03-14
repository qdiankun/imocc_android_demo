package com.imocc.indicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.imocc.indicator.view.ViewPagerIndicatorFixed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by diankun on 2016/3/13.
 */
public class FixedItemActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private ViewPagerIndicatorFixed mIndicator;

    private List<String> mTitle = Arrays.asList("短信", "收藏", "推荐");
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
                mIndicator.scroll(position,positionOffset);
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
        for (int i = 0; i < 3; i++) {
            FooFragment fooFragment = FooFragment.newInstance(mTitle.get(i));
            mFragments.add(fooFragment);
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mIndicator = (ViewPagerIndicatorFixed) findViewById(R.id.viewpagerindicator);

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
