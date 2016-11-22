package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.felix.sensordemo.R;
import com.felix.sensordemo.adapter.DemoListAdapter;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

import java.util.List;

/**
 * @author Felix
 */
public class DemoListActivity extends BaseActivity {

    private Toolbar toolbar;
    private RecyclerView rvDemoList;
    private List<Sensor> mSensorList;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_demo_list;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvDemoList = (RecyclerView) findViewById(R.id.rv_demo_list);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Log.d(Constants.APP_TAG, mSensorList.toString());
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvDemoList.setLayoutManager(manager);
        DemoListAdapter adapter = new DemoListAdapter(this);
        rvDemoList.setAdapter(adapter);
    }
}
