package com.example.maps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public CustomInfoWindowAdapter(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.custom_info_win, null);
    }

    private void rendowWindowText(Marker marker, View view){
        if(marker.getTitle()==null)
            return;
        String part1 = marker.getTitle().split(" ")[0];
        TextView tvTitle =  view.findViewById(R.id.tvArrival);
        tvTitle.setText("Arrival Time: "+ part1);
        String part2 = marker.getTitle().split(" ")[1];
        TextView tvSnippet =  view.findViewById(R.id.tvDistance);
        tvSnippet.setText("Remaining Distance: "+part2+"m");
    }

    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker, mWindow);
        return mWindow;
    }
}