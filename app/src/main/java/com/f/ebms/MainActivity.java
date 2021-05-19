package com.f.ebms;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.ui.NewReportActivity;
import com.f.ebms.ui.PartListActivity;
import com.f.ebms.ui.ReportListActivity;
import com.f.ebms.ui.StatisticsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "EBMS - MainActivity - onCreate() - Activity created");
    }

    public void createNewReport(View view) {
        Log.i(LOG_TAG, "EBMS - MainActivity - createNewReport() - Starting NewReportActivity");
        Intent createNewReportIntent = new Intent(this, NewReportActivity.class);
        startActivity(createNewReportIntent);
    }

    public void showAllReports(View view) {
        Log.i(LOG_TAG, "EBMS - MainActivity - showAllReports() - Starting ReportListActivity");
        Intent showAllReportsIntent = new Intent(this, ReportListActivity.class);
        startActivity(showAllReportsIntent);
    }

    public void showAllParts(View view) {
        Log.i(LOG_TAG, "EBMS - MainActivity - showAllParts() - Starting PartListActivity");
        Intent showAllPartsIntent = new Intent(this, PartListActivity.class);
        startActivity(showAllPartsIntent);
    }

    public void showStatistics(View view) {
        Log.i(LOG_TAG, "EBMS - MainActivity - showStatistics() - Starting StatisticsActivity");
        Intent showStatisticsIntent = new Intent(this, StatisticsActivity.class);
        startActivity(showStatisticsIntent);
    }
}