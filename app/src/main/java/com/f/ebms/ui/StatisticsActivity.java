package com.f.ebms.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;

public class StatisticsActivity extends AppCompatActivity {

    private static final String LOG_TAG = StatisticsActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        this.ebmsDatabase = new EBMSDatabase(this);
        Log.i(LOG_TAG, "EBMS - StatisticsActivity - onCreate() - StatisticsActivity created");
    }
}