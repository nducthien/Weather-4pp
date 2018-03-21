package com.tnd.weather4pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tnd.weather4pp.R;
import com.tnd.weather4pp.model.ForeCastDay;

import java.util.ArrayList;

public class ForeCastAdapter extends BaseAdapter {
    Context context;
    ArrayList<ForeCastDay> data;

    public ForeCastAdapter(Context context, ArrayList<ForeCastDay> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_forecast, null);

        TextView tvDay = (TextView) view.findViewById(R.id.tv_fc_day);
        TextView tvCodition = (TextView) view.findViewById(R.id.tv_fc_conditon);
        TextView tvHigh = (TextView) view.findViewById(R.id.tv_fc_high);
        TextView tvLow = (TextView) view.findViewById(R.id.tv_fc_low);
        ImageView imgWeatherIcon = (ImageView) view.findViewById(R.id.img_fc_weather_iocn);

        ForeCastDay p = data.get(position);

        tvCodition.setText(p.getText());
        tvDay.setText(p.getDay());
        tvHigh.setText(p.getHigh() + "\u00B0");
        tvLow.setText(p.getLow() + "\u00B0");
        int icon = view.getResources().getIdentifier("drawable/item_" + p.getCode(), null, view.getContext().getPackageName());
        imgWeatherIcon.setImageResource(icon);

        return view;
    }
}
