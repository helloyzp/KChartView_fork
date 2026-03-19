package com.github.tifezh.kchart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.tifezh.kchart.databinding.ActivityMainBinding;


/**
 * Created by tifezh on 2017/6/30.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

/*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_style1:
                intent.setClass(this, ExampleActivity.class);
                intent.putExtra("type", 0);
                break;
            case R.id.btn_style2:
                intent.setClass(this, ExampleActivity.class);
                intent.putExtra("type", 1);
                break;
            case R.id.btn_loadmore:
                intent.setClass(this, LoadMoreActivity.class);
                break;
            case R.id.btn_minute:
                intent.setClass(this, MinuteChartActivity.class);
                break;
        }
        startActivity(intent);
    }*/


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // 设置点击监听
        binding.btnStyle1.setOnClickListener(this);
        binding.btnStyle2.setOnClickListener(this);
        binding.btnLoadmore.setOnClickListener(this);
        binding.btnMinute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int id = v.getId();

        if (id == R.id.btn_style1) {
            intent.setClass(this, ExampleActivity.class);
            intent.putExtra("type", 0);
        } else if (id == R.id.btn_style2) {
            intent.setClass(this, ExampleActivity.class);
            intent.putExtra("type", 1);
        } else if (id == R.id.btn_loadmore) {
            intent.setClass(this, LoadMoreActivity.class);
        } else if (id == R.id.btn_minute) {
            intent.setClass(this, MinuteChartActivity.class);
        }

        if (intent.getComponent() != null) {
            startActivity(intent);
        }
    }


}
