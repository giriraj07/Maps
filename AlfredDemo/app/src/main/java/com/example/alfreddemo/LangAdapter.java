package com.example.alfreddemo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class LangAdapter extends BaseAdapter {
    private Context mContext;
    public String[] mThumbIds = {
           "Madari","Hindi","Santhali","English","Ho","Mandari","Kurukh","Santhali",
            "Hindi","ho","English","Kurukh","Ho","Mandari","Kurukh","Santhali",
            "Hindi","Ho","English","Kurukh","English","Kurukh"
    };
    public LangAdapter(Context con) {
        mContext=con;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
       @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           TextView textView;
        if (convertView == null) {
            textView= new TextView(mContext);
            textView.setLayoutParams(new GridView.LayoutParams(120, 85));
            textView.setPadding(8, 8, 8, 8);
            }
        else
        {
            textView = (TextView) convertView;
        }
        textView.setText(mThumbIds[position]);
        return textView;
     }
}
