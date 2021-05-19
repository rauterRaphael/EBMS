package com.f.ebms.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.Report;

import java.util.HashMap;

public class ReportListActivity extends AppCompatActivity {

    private static final String LOG_TAG = ReportListActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        this.ebmsDatabase = new EBMSDatabase(this);
        HashMap<Integer, Report> allReports = this.ebmsDatabase.getAllReports();
        Log.i(LOG_TAG, "EBMS - ReportListActivity - onCreate() - ReportListActivity created");
    }
}