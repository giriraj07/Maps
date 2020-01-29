package com.example.alfreddemo;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gv1 = findViewById(R.id.gridView);
        final TextView tv=findViewById(R.id.tv);
        gv1.setAdapter(new LangAdapter(getBaseContext()));
        gv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                CheckedTextView textView= (CheckedTextView) parent.getItemAtPosition(position);
                if(textView!=null) {
                    textView.setCheckMarkDrawable(R.drawable.green);
                    tv.setVisibility(View.VISIBLE);
                }
            }
      });
    }
}





