package com.github.tifezh.kchart;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.tifezh.kchart.chart.KChartAdapter;
import com.github.tifezh.kchart.chart.KLineEntity;
import com.github.tifezh.kchart.databinding.ActivityExampleBinding;
import com.github.tifezh.kchart.databinding.ActivityExampleLightBinding;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;

import java.util.List;


public class ExampleActivity extends AppCompatActivity {

    private KChartAdapter mAdapter;

    private boolean isDarkMode = false;

    private ActivityExampleBinding binding;
    private ActivityExampleLightBinding bindingLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int type = getIntent().getIntExtra("type", 0);
        isDarkMode = (type == 0);

        if (isDarkMode) {
            //setContentView(R.layout.activity_example);
            // 深色模式
            binding = DataBindingUtil.setContentView(this, R.layout.activity_example);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = getWindow();
                window.setFlags(
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        } else {
            //setContentView(R.layout.activity_example_light);
            // 浅色模式
            bindingLight = DataBindingUtil.setContentView(this, R.layout.activity_example_light);
        }

        initView();
        initData();
    }

    private void initView() {
        KChartView kChartView = isDarkMode ? binding.kchartView : bindingLight.kchartView;

        mAdapter = new KChartAdapter();
        kChartView.setAdapter(mAdapter);
        kChartView.setDateTimeFormatter(new DateFormatter());
        kChartView.setGridRows(4);
        kChartView.setGridColumns(4);
        kChartView.setOnSelectedChangedListener(new BaseKChartView.OnSelectedChangedListener() {
            @Override
            public void onSelectedChanged(BaseKChartView view, Object point, int index) {
                KLineEntity data = (KLineEntity) point;
                Log.i("onSelectedChanged", "index:" + index + " closePrice:" + data.getClosePrice());
            }
        });
    }

    private void initData() {
        KChartView kChartView = isDarkMode ? binding.kchartView : bindingLight.kchartView;
        TextView tvPrice = isDarkMode ? binding.tvPrice : bindingLight.tvPrice;
        TextView tvPercent = isDarkMode ? binding.tvPercent : bindingLight.tvPercent;
        LinearLayout llStatus = isDarkMode ? binding.llStatus : bindingLight.llStatus;

        kChartView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLineEntity> data = DataRequest.getALL(ExampleActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addFooterData(data);
                        kChartView.startAnimation();
                        kChartView.refreshEnd();
                    }
                });
            }
        }).start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        KChartView kChartView = isDarkMode ? binding.kchartView : bindingLight.kchartView;
        LinearLayout llStatus = isDarkMode ? binding.llStatus : bindingLight.llStatus;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            llStatus.setVisibility(View.GONE);
            kChartView.setGridRows(3);
            kChartView.setGridColumns(8);
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            llStatus.setVisibility(View.VISIBLE);
            kChartView.setGridRows(4);
            kChartView.setGridColumns(4);
        }
    }
}
