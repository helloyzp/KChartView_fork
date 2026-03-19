package com.github.tifezh.kchart;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.github.tifezh.kchart.chart.KChartAdapter;
import com.github.tifezh.kchart.chart.KLineEntity;
import com.github.tifezh.kchart.databinding.ActivityExampleLightBinding;
import com.github.tifezh.kchartlib.chart.BaseKChartView;
import com.github.tifezh.kchartlib.chart.KChartView;
import com.github.tifezh.kchartlib.chart.formatter.DateFormatter;

import java.util.List;

/**
 * Created by tifezh on 2017/7/3.
 */

public class LoadMoreActivity extends AppCompatActivity implements KChartView.KChartRefreshListener {


    private KChartAdapter mAdapter;


    private ActivityExampleLightBinding bindingLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_example_light);
        //ButterKnife.bind(this);
        bindingLight = DataBindingUtil.setContentView(this, R.layout.activity_example_light);

        initView();
        initData();
    }

    private void initView() {
        KChartView kChartView = bindingLight.kchartView;

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
        KChartView kChartView = bindingLight.kchartView;

        kChartView.showLoading();
        kChartView.setRefreshListener(this);
        onLoadMoreBegin(kChartView);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        KChartView kChartView = bindingLight.kchartView;
        LinearLayout llStatus =  bindingLight.llStatus;

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

    @Override
    public void onLoadMoreBegin(KChartView chart) {
        KChartView kChartView = bindingLight.kchartView;

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<KLineEntity> data = DataRequest.getData(LoadMoreActivity.this, mAdapter.getCount(), 500);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!data.isEmpty()) {
                    Log.i("onLoadMoreBegin", "start:" + data.get(0).getDatetime() + " stop:" + data.get(data.size() - 1).getDatetime());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //第一次加载时开始动画
                        if (mAdapter.getCount() == 0) {
                            kChartView.startAnimation();
                        }
                        mAdapter.addFooterData(data);
                        //加载完成，还有更多数据
                        if (data.size() > 0) {
                            kChartView.refreshComplete();
                        }
                        //加载完成，没有更多数据
                        else {
                            kChartView.refreshEnd();
                        }
                    }
                });
            }
        }).start();
    }
}
