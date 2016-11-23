package com.felix.sensordemo.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

/**
 * @author Felix
 */
public class SensorDetailActivity extends BaseActivity {

    private TextView tvName;
    private TextView tvVendor;
    private TextView tvResolution;
    private TextView tvMaxRange;
    private TextView tvPower;
    private Toolbar toolbar;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_sensor_detail;
    }

    @Override
    protected void initView() {
        setFinishOnTouchOutside(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tvName = (TextView) findViewById(R.id.tv_dialog_name);
        tvVendor = (TextView) findViewById(R.id.tv_dialog_vendor);
        tvResolution = (TextView) findViewById(R.id.tv_dialog_resolution);
        tvMaxRange = (TextView) findViewById(R.id.tv_dialog_maximum_range);
        tvPower = (TextView) findViewById(R.id.tv_dialog_power);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.SENSOR_NAME);
        String vendor = intent.getStringExtra(Constants.SENSOR_VENDOR);
        float maxRange = intent.getFloatExtra(Constants.SENSOR_MAXIMUM_RANGE, -1);
        float resolution = intent.getFloatExtra(Constants.SENSOR_RESOLUTION, -1);
        float power = intent.getFloatExtra(Constants.SENSOR_POWER, -1);
        tvName.setText(name);
        tvVendor.setText(vendor);
        tvMaxRange.setText(String.valueOf(maxRange));
        tvResolution.setText(String.valueOf(resolution));
        tvPower.setText(String.format("%s mA", power));
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(SensorDetailActivity.this);
            }
        });
    }
}
