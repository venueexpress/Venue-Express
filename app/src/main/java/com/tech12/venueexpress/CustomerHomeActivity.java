package com.tech12.venueexpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
