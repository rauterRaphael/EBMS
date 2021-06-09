package com.f.ebms.ui.newReport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;

public class NewReportActivity extends AppCompatActivity {

    private static final String LOG_TAG = NewReportActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        this.ebmsDatabase = new EBMSDatabase(this);
        Log.i(LOG_TAG, "EBMS - EBMSDatabase - onCreate() - NewReportActivity created");
    }
}