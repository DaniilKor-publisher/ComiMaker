package com.example.comimakerv2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comimakerv2.R;
import com.example.comimakerv2.myClasses.MyAdjusts;

public class SettingAdapter extends ArrayAdapter<MyAdjusts> {

    public SettingAdapter(Context context, MyAdjusts[] arr) {
        super(context, R.layout.settings_item, arr);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MyAdjusts adjust = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.settings_item, null);
        }

        ((TextView) convertView.findViewById(R.id.settingTitle)).setText(adjust.name);
        ((ImageView) convertView.findViewById(R.id.settingIcon)).setImageResource(adjust.drawableId);

        return convertView;
    }
}
