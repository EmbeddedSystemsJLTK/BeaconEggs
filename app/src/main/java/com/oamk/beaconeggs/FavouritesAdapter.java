package com.oamk.beaconeggs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 14.11.2017.
 */

public class FavouritesAdapter extends ArrayAdapter<FavouritesItem>{
    Context context;

    public FavouritesAdapter(Context context, int resourceId, List<FavouritesItem> favouritesItems){
        super(context, resourceId, favouritesItems);
        //Log.d(TAG, "constructor");
        this.context = context;
    }

    private class ViewHolder {
        TextView favouriteText;
        ImageButton deleteButton;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        FavouritesAdapter.ViewHolder holder;
        final FavouritesItem favouritesItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.favourite_item, null);
            holder = new FavouritesAdapter.ViewHolder();
            holder.favouriteText = (TextView) convertView.findViewById(R.id.favouriteText);
            holder.deleteButton = (ImageButton) convertView.findViewById(R.id.favouriteDeleteButton);
            convertView.setTag(holder);
        } else
            holder = (FavouritesAdapter.ViewHolder) convertView.getTag();

        holder.favouriteText.setText(""+favouritesItem.getName());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(getItem(position));
            }
        });

        return convertView;
    }
}
