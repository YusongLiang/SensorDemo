package com.felix.sensordemo.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.util.Constants;
import com.felix.sensordemo.view.SensorDetailActivity;

import java.util.List;

/**
 * @author Felix
 */
public class SensorListAdapter extends RecyclerView.Adapter<SensorListAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Sensor> mSensorList;

    public SensorListAdapter(Context context, List<Sensor> sensors) {
        mContext = context;
        mSensorList = sensors;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_sensor, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sensor sensor = mSensorList.get(position);
        holder.tvSensorName.setText(sensor.getName());
        holder.tvSensorName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        createDialog(v, sensor);
                        break;
                }
                return true;
            }
        });
    }

    private void createDialog(View v, Sensor sensor) {
        Intent intent = new Intent(mContext, SensorDetailActivity.class);
        intent.putExtra(Constants.SENSOR_NAME, sensor.getName());
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,
                v, mContext.getResources().getString(R.string.transition_name));
        mContext.startActivity(intent, activityOptionsCompat.toBundle());
    }

    @Override
    public int getItemCount() {
        return mSensorList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvSensorName;

        ViewHolder(View itemView) {
            super(itemView);
            tvSensorName = (TextView) itemView.findViewById(R.id.tv_item_sensor);
        }
    }
}
