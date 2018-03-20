package com.example.saka.myapplication.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saka.myapplication.R;

/**
 * Created by Saka on 04-Jun-17.
 */

public class OptionAdapter extends BaseAdapter {

    private Context context;
    private String options[];
    private int icons[];

    public OptionAdapter(Context context, String[] options, int[] icons) {
        this.context = context;
        this.options = options;
        this.icons = icons;
    }

    public OptionAdapter(Context context, String[] options) {
        this.context = context;
        this.options = options;
    }

    @Override
    public int getCount() {
        return options.length;
    }

    @Override
    public Object getItem(int position) {
        return options[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.viewholder_option, parent, false);
            TextView txtOption = (TextView) rowView.findViewById(R.id.vh_option_txt);
            ImageView imgOption = (ImageView) rowView.findViewById(R.id.vh_option_img);
            txtOption.setText(options[position]);
            imgOption.setImageResource(icons[position]);
        convertView = rowView;
        return convertView;
    }

}
