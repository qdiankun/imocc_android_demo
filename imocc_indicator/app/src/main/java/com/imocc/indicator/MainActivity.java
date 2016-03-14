package com.imocc.indicator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * ViewPagerIndicator的实现方法
 * 慕课：http://www.imooc.com/learn/615
 */
public class MainActivity extends AppCompatActivity {

    private Context context = this;

    private final String[] items = {
            "固定个数Indicator实现", "xml中设置Tab的Indicator实现", "动态设置Tab的Indicator实现"

    };
    private final Class<?>[] classes = {
            FixedItemActivity.class, UnFixedItemActivity.class,DynamicItemActivity.class
    };
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListView lv = new ListView(context);
        mAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        lv.setCacheColorHint(Color.TRANSPARENT);
        lv.setFadingEdgeLength(0);
        lv.setAdapter(mAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, classes[position]);
                startActivity(intent);
            }
        });

        setContentView(lv);
    }
}
