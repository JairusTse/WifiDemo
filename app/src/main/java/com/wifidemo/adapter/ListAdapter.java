package com.wifidemo.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wifidemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者： xy
 * 日期： 18/12/14
 */

public class ListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ScanResult> wifiList = new ArrayList<>();

    public ListAdapter(Context context, List<ScanResult> wifiList) {
        this.mContext = context;
        this.wifiList = wifiList;
    }

    @Override
    public int getCount() {
        return wifiList.size();
    }

    @Override
    public Object getItem(int position) {
        return wifiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wifi_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ScanResult scanResult = wifiList.get(position);
        Log.i("WifiDemo", scanResult.SSID);

        holder.tvName.setText("\n地址" + scanResult.BSSID + "\n设备名字" + scanResult.SSID +
                "\n加密方式" + scanResult.capabilities + "\n接入频率" + scanResult.frequency + "\n信号强度" +
                scanResult.level + "\n");
        return convertView;
    }

    static class ViewHolder {
        TextView tvName;

        ViewHolder(View view) {
            tvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }
}

