package com.felix.sensordemo.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;

/**
 * @author Felix
 */
public class OrientationActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tvOriMsg;
    private ImageView ivCompass;
    private Button btOpenOrClose;
    private SensorManager mManager;
    /**
     * 加速度传感器
     */
    private Sensor mAccelerometerSensor;
    /**
     * 电磁场传感器
     */
    private Sensor mMagneticFieldSensor;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    mGravity = event.values;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mGeomagnetic = event.values;
                    break;
            }
            getCurrentOrientation();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private boolean isRegister;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_orientation;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivCompass = (ImageView) findViewById(R.id.iv_compass);
        tvOriMsg = (TextView) findViewById(R.id.tv_orientation_message);
        btOpenOrClose = (Button) findViewById(R.id.bt_open_or_close);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometerSensor = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticFieldSensor = mManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btOpenOrClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_open_or_close:
                if (isRegister) unregisterListener();
                else registerListener();
                break;
        }
    }

    /**
     * 获取当前方向
     */
    private void getCurrentOrientation() {
        if (mGravity == null || mGeomagnetic == null) return;
        float[] R = new float[9];
        float[] values = new float[3];
        SensorManager.getRotationMatrix(R, null, mGravity, mGeomagnetic);
        SensorManager.getOrientation(R, values);
        float radians = values[0];//当前方向的弧度值
        double degree = Math.toDegrees(radians);//当前方向的角度值
        ivCompass.setRotation((float) -degree);
        generateOrientationMessage((int) degree);
    }

    /**
     * 生成详细方向信息
     *
     * @param degree 当前方向的角度值
     */
    private void generateOrientationMessage(int degree) {
        String msg;
        if (degree > 95 && degree < 175) {
            msg = "南偏东 " + (180 - degree) + " 度";
        } else if (degree >= 85 && degree <= 95) {
            msg = "正东";
        } else if (degree > 5 && degree < 85) {
            msg = "北偏东 " + degree + "度";
        } else if (degree >= -5 && degree <= 5) {
            msg = "正北";
        } else if (degree > -85 && degree < -5) {
            msg = "北偏西 " + Math.abs(degree) + " 度";
        } else if (degree >= -95 && degree <= -85) {
            msg = "正西";
        } else if (degree > -175 && degree < -95) {
            msg = "南偏西 " + (180 - Math.abs(degree)) + "度";
        } else {
            msg = "正南";
        }
        tvOriMsg.setText(msg);
    }

    /**
     * 注册传感器监听器
     */
    private void registerListener() {
        mManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        mManager.registerListener(mSensorEventListener, mMagneticFieldSensor, SensorManager.SENSOR_DELAY_UI);
        isRegister = true;
        btOpenOrClose.setText(R.string.close);
    }

    /**
     * 注销传感器监听器
     */
    private void unregisterListener() {
        mManager.unregisterListener(mSensorEventListener, mAccelerometerSensor);
        mManager.unregisterListener(mSensorEventListener, mMagneticFieldSensor);
        isRegister = false;
        btOpenOrClose.setText(R.string.open);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterListener();
    }
}
