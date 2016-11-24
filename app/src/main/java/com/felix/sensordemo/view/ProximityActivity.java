package com.felix.sensordemo.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.felix.sensordemo.R;
import com.felix.sensordemo.app.BaseActivity;
import com.felix.sensordemo.util.Constants;

/**
 * @author Felix
 */
public class ProximityActivity extends BaseActivity {

    /**
     * 扑克牌旋转用时
     */
    private static final long ROTATE_DURATION = 350;
    private Toolbar toolbar;
    private RelativeLayout rlPoker;
    private ImageView ivPokerBg;
    private ImageView ivPokerFg;
    private AnimatorSet mAnimatorSet;
    private SensorManager mManager;
    private Sensor mProximitySensor;
    private float lastProximity = -1;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_PROXIMITY:
                    float proximity = event.values[0];
                    Log.d(Constants.APP_TAG, "proximity = " + proximity);
                    float change = proximity - lastProximity;
                    if (Math.abs(change) > event.sensor.getResolution() && lastProximity != -1) {
                        onProximityChange(change);
                    }
                    lastProximity = proximity;
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void onProximityChange(float change) {
        executeAnimation(change < 0);
    }

    /**
     * 执行翻转动画
     *
     * @param isOpen 是否为掀开动画
     */
    private void executeAnimation(final boolean isOpen) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
        mAnimatorSet = new AnimatorSet();
        Animator anim1 = createRotateAnimator(isOpen);
        Animator anim2 = createPokerBgAnimator(isOpen);
        Animator anim3 = createPokerFgAnimator(isOpen);
        if (isOpen) {
            mAnimatorSet.play(anim1);
            mAnimatorSet.play(anim2).before(anim3);
        } else {
            mAnimatorSet.play(anim1);
            mAnimatorSet.play(anim3).before(anim2);
        }
        mAnimatorSet.start();
    }

    /**
     * 创建扑克牌旋转动画
     *
     * @param isOpen 是否为掀开动画
     * @return {@link Animator}对象
     */
    private Animator createRotateAnimator(boolean isOpen) {
        float from = isOpen ? 0 : 180;
        float to = isOpen ? 180 : 0;
        ObjectAnimator anim = ObjectAnimator.ofFloat(rlPoker, "rotationY", from, to);
        anim.setDuration(ROTATE_DURATION);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        return anim;
    }

    /**
     * 创建扑克牌背面动画
     *
     * @param isOpen 是否为掀开动画
     * @return {@link Animator}对象
     */
    private Animator createPokerBgAnimator(boolean isOpen) {
        float alphaFrom = isOpen ? 1 : 0;
        float alphaTo = isOpen ? 0 : 1;
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivPokerBg, "alpha", alphaFrom, alphaTo);
        Interpolator interpolator1;
        if (isOpen) interpolator1 = new DecelerateInterpolator();
        else interpolator1 = new AccelerateInterpolator();
        anim.setInterpolator(interpolator1);
        anim.setDuration(ROTATE_DURATION / 2);
        return anim;
    }

    /**
     * 创建扑克牌正面动画
     *
     * @param isOpen 是否为掀开动画
     * @return {@link Animator}对象
     */
    private Animator createPokerFgAnimator(boolean isOpen) {
        float alphaFrom = isOpen ? 0 : 1;
        float alphaTo = isOpen ? 1 : 0;
        Interpolator interpolator2;
        if (isOpen) interpolator2 = new DecelerateInterpolator();
        else interpolator2 = new AccelerateInterpolator();
        ObjectAnimator anim = ObjectAnimator.ofFloat(ivPokerFg, "alpha", alphaFrom, alphaTo);
        anim.setInterpolator(interpolator2);
        anim.setDuration(ROTATE_DURATION / 2);
        return anim;
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_proximity;
    }

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rlPoker = (RelativeLayout) findViewById(R.id.rl_reveal);
        ivPokerBg = (ImageView) findViewById(R.id.iv_poker_bg);
        ivPokerFg = (ImageView) findViewById(R.id.iv_poker_fg);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mProximitySensor = mManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.finishAfterTransition(ProximityActivity.this);
            }
        });
    }

    /**
     * 注册传感器监听器
     */
    private void registerListener() {
        mManager.registerListener(mSensorEventListener, mProximitySensor, SensorManager.SENSOR_DELAY_UI);
    }

    /**
     * 注销传感器监听器
     */
    private void unregisterListener() {
        mManager.unregisterListener(mSensorEventListener, mProximitySensor);
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
