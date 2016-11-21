package com.felix.sensordemo.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Felix
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBeforeSetContent();
        setContentView(getLayoutResID());
        SensorDemoApplication.addActivity(this);
        initView();
        initData(savedInstanceState);
        initListener();
    }

    protected abstract int getLayoutResID();

    protected void onBeforeSetContent() {
    }

    protected void initView() {
    }

    protected void initData(Bundle savedInstanceState) {
    }

    protected void initListener() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SensorDemoApplication.removeActivity(this);
    }
}
