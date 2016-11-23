package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.felix.sensordemo.R;
import com.felix.sensordemo.adapter.DemoListAdapter;
import com.felix.sensordemo.adapter.SensorListAdapter;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

import java.util.List;

/**
 * @author Felix
 */
public class DemoListActivity extends BaseActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private RecyclerView rvDemoList;
    private RecyclerView rvSupportSensors;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_demo_list;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvDemoList = (RecyclerView) findViewById(R.id.rv_demo_list);
        rvSupportSensors = (RecyclerView) findViewById(R.id.rv_support_sensor_list);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        SensorManager manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ALL);
        Log.d(Constants.APP_TAG, sensors.toString());
        loadDemoList();
        loadSensorList(sensors);
    }

    @Override
    protected void initListener() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /**
     * 加载演示列表
     */
    private void loadDemoList() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rvDemoList.setLayoutManager(manager);
        DemoListAdapter adapter = new DemoListAdapter(this);
        rvDemoList.setAdapter(adapter);
    }

    /**
     * 加载设备内传感器列表
     *
     * @param sensors {@link Sensor}传感器对象
     */
    private void loadSensorList(List<Sensor> sensors) {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvSupportSensors.setLayoutManager(manager);
        SensorListAdapter adapter = new SensorListAdapter(this, sensors);
        rvSupportSensors.setAdapter(adapter);
    }
}
