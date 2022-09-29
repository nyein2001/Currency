package com.neversitup.currency.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.neversitup.currency.R;
import com.neversitup.currency.model.CurrencyData;

import java.util.List;

public class CurrencyAdapter extends BaseAdapter {

    Context context;
    List<CurrencyData> currencyDataList;
    public CurrencyAdapter(Context context, List<CurrencyData> currencyDataList) {
        this.context = context;
        this.currencyDataList = currencyDataList;
    }

    @Override
    public int getCount() {
        return currencyDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return currencyDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.currency_item,null);
            holder = new ViewHolder();
            holder.icon = convertView.findViewById(R.id.currency_icon);
            holder.rateValue = convertView.findViewById(R.id.currency_value);
            holder.updateTime = convertView.findViewById(R.id.update_time);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, currencyDataList.get(i).getIcon()));
        holder.rateValue.setText(String.valueOf(currencyDataList.get(i).getRate()));
        holder.updateTime.setText(String.valueOf(currencyDataList.get(i).getTime()));
        return convertView;
    }

     static class ViewHolder{
        ImageView icon;
        TextView rateValue,updateTime;
    }
}
