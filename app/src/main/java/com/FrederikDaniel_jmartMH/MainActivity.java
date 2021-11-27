package com.FrederikDaniel_jmartMH;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.FrederikDaniel_jmartMH.model.Account;

public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            TextView tv = findViewById(R.id.TextView_mainActivity);
            Account account = LoginActivity.getLoggedAccount();
            tv.setText("Welcome Back " + account.name);
        }

}