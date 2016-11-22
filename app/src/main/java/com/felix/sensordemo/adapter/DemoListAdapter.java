package com.felix.sensordemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felix.sensordemo.R;
import com.felix.sensordemo.view.AccelerometerActivity;
import com.felix.sensordemo.view.GyroscopeActivity;
import com.felix.sensordemo.view.LightActivity;
import com.felix.sensordemo.view.OrientationActivity;

/**
 * @author Felix
 */
public class DemoListAdapter extends RecyclerView.Adapter<DemoListAdapter.ViewHolder> {

    private Context mContext;

    /**
     * item标题数组
     */
    private final String[] mTitles = {
            "Orientation",
            "Accelerometer",
            "Gyroscope",
            "Light"
    };

    /**
     * 跳转到的{@link Class}数组
     */
    private final Class[] mClasses = {
            OrientationActivity.class,
            AccelerometerActivity.class,
            GyroscopeActivity.class,
            LightActivity.class
    };

    /**
     * 图标资源ID数组
     */
    private final int[] mIconResIDs = {
            R.drawable.ic_orientation,
            R.drawable.ic_accelerometer,
            R.drawable.ic_gyroscope,
            R.drawable.ic_light
    };

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, mClasses[holder.getAdapterPosition()]));
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
