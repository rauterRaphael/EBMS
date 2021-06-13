package com.f.ebms.ui.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public void initStatisticsView(){
        int numReports = 0;
        int numParts = 0;
        HashMap<Integer, Report> reportsHashMap = this.ebmsDatabase.getAllReports();

        for(Map.Entry<Integer, Report> e : reportsHashMap.entrySet()) {
            reports.add((e.getKey()).toString() + ";" + ((e.getValue())).getLicensePlate() + " " + dateFormat.format(((e.getValue())).getReportDateTime()));
        }

    }

    public void onPartStateBtnClicked(View view) {

    }

    public void onChipGroupClicked(View view) {

    }
}