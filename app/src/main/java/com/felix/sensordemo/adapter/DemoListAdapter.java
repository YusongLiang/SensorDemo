package com.felix.sensordemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.view.AccelerometerActivity;
import com.felix.sensordemo.view.GyroscopeActivity;
import com.felix.sensordemo.view.LightActivity;
import com.felix.sensordemo.view.OrientationActivity;
import com.felix.sensordemo.view.PressureActivity;
import com.felix.sensordemo.view.ProximityActivity;
import com.felix.sensordemo.view.TemperatureActivity;

/**
 * @author Felix
 */
public class DemoListAdapter extends RecyclerView.Adapter<DemoListAdapter.ViewHolder> {

    /**
     * item标题数组
     */
    private final String[] mTitles = {
            "Orientation",
            "Accelerometer",
            "Gyroscope",
            "Light",
            "Pressure",
            "Proximity",
            "Temperature"
    };

    /**
     * 跳转到的{@link Class}数组
     */
    private final Class[] mClasses = {
            OrientationActivity.class,
            AccelerometerActivity.class,
            GyroscopeActivity.class,
            LightActivity.class,
            PressureActivity.class,
            ProximityActivity.class,
            TemperatureActivity.class
    };

    /**
     * 图标资源ID数组
     */
    private final int[] mIconResIDs = {
            R.drawable.ic_orientation,
            R.drawable.ic_accelerometer,
            R.drawable.ic_gyroscope,
            R.drawable.ic_light,
            R.drawable.ic_pressure,
            R.drawable.ic_proximity,
            R.drawable.ic_temperature
    };

    private Context mContext;

    public DemoListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DemoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_demo, parent, false));
    }

    @Override
    public void onBindViewHolder(final DemoListAdapter.ViewHolder holder, int position) {
        holder.tvItemName.setText(mTitles[holder.getAdapterPosition()]);
        holder.ivItemIcon.setImageResource(mIconResIDs[holder.getAdapterPosition()]);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        Intent intent = new Intent(mContext, mClasses[holder.getAdapterPosition()]);
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                                v, mContext.getResources().getString(R.string.transition_name));
                        mContext.startActivity(intent, activityOptionsCompat.toBundle());
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvItemName;
        private final ImageView ivItemIcon;

        ViewHolder(View itemView) {
            super(itemView);
            tvItemName = (TextView) itemView.findViewById(R.id.tv_item_name);
            ivItemIcon = (ImageView) itemView.findViewById(R.id.iv_item_icon);
        }
    }
}
