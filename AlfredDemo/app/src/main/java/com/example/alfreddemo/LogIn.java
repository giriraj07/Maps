
package com.example.alfreddemo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

        public class LogIn extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login);
        }
        public void btn_sign_up(View view) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
        public void btn_lang_options(View view) {
            startActivity(new Intent(getApplicationContext(),LanguageChoose.class));
        }
      }
