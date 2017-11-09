package com.oamk.beaconeggs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 8.11.2017.
 */

public class LunchMenuItemAdapter extends ArrayAdapter<LunchMenuItem> {

    Context context;

    public LunchMenuItemAdapter(Context context, int resourceId, List<LunchMenuItem> lunchMenuItems){
        super(context, resourceId, lunchMenuItems);
        //Log.d(TAG, "constructor");
        this.context = context;
    }

    private class ViewHolder {
        TextView foodTitle;
        TextView foodName;
        TextView foodInfo;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LunchMenuItem lunchMenuItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.menu_item, null);
            holder = new ViewHolder();
            holder.foodTitle = (TextView) convertView.findViewById(R.id.foodTitle);
            holder.foodName = (TextView) convertView.findViewById(R.id.foodName);
            holder.foodInfo = (TextView) convertView.findViewById(R.id.foodInfo);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.foodTitle.setText(lunchMenuItem.getTitle());
        holder.foodName.setText("" + lunchMenuItem.getName());
        holder.foodInfo.setText("" + lunchMenuItem.getInfo());

        return convertView;
    }

}
