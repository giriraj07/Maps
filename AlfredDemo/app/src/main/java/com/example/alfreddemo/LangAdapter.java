package com.example.alfreddemo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

public class LangAdapter extends BaseAdapter {
    private Context mContext;
    public String[] mThumbIds = {
            "Madari", "Hindi", "Santhali", "English", "Ho", "Mandari", "Kurukh", "Santhali",
            "Hindi", "ho", "English", "Kurukh", "Ho", "Mandari", "Kurukh", "Santhali",
            "Hindi", "Ho", "English", "Kurukh", "English", "Kurukh"
    };
    public LangAdapter(Context con) {
        mContext = con;
    }
    @Override
    public int getCount() {
        return mThumbIds.length;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View gridView=new View(mContext);
            gridView = inflater.inflate(R.layout.grid_itemrow, null);
            CheckedTextView city = (CheckedTextView) gridView.findViewById(R.id.city);
            return gridView;
        }
}
