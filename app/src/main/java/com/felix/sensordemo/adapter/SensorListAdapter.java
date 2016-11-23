package com.felix.sensordemo.adapter;

import android.content.Context;
import android.hardware.Sensor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.felix.sensordemo.R;

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
        Sensor sensor = mSensorList.get(position);
        holder.tvSensorName.setText(sensor.getName());
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
