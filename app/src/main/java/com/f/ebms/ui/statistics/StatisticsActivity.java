package com.f.ebms.ui.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;
import com.google.android.material.chip.ChipGroup;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StatisticsActivity extends AppCompatActivity {

    private static final String LOG_TAG = StatisticsActivity.class.getName();

    private EBMSDatabase ebmsDatabase;
    private ArrayList<Report> allReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        this.ebmsDatabase = new EBMSDatabase(this);
        this.allReports = new ArrayList<>();

        HashMap<Integer, Report> reportsHashMap = this.ebmsDatabase.getAllReports();

        for(Map.Entry<Integer, Report> e : reportsHashMap.entrySet()) {
            this.allReports.add(e.getValue());
        }

        Log.i(LOG_TAG, "EBMS - StatisticsActivity - onCreate() - StatisticsActivity created");
        initStatisticsView();
    }

    public void initStatisticsView(){
        long oneHourInMilli = 3600000;
        int numReports = 0;
        int numParts = 0;

        // text off - repaired | text on - to be repaired
        ToggleButton partState = findViewById(R.id.partStateTB);
        RadioGroup timeWindow = findViewById(R.id.timeCG);
        RadioButton selectedTime = findViewById(timeWindow.getCheckedRadioButtonId());

        long currentSysTime = System.currentTimeMillis();
        for(Report item : this.allReports){
            long reportCreationTime = item.getReportDateTime();
            long millisToCmp = 0;

            if(selectedTime.getText().toString().equals("hour")){
                millisToCmp = oneHourInMilli;
            }else if(selectedTime.getText().toString().equals("day")){
                millisToCmp = oneHourInMilli * 24;
            }else if(selectedTime.getText().toString().equals("week")){
                millisToCmp = oneHourInMilli * 24 * 7;
            }else if(selectedTime.getText().toString().equals("month")){
                millisToCmp = oneHourInMilli * 24 * 31;
            }else {
                millisToCmp = oneHourInMilli;
            }

            if(currentSysTime - millisToCmp < reportCreationTime) {
                numReports++;

                BikePart[] bikeParts = item.getBikePartList();
                for (int i = 0; i < bikeParts.length; i++) {
                    if (bikeParts[i] != null) {
                        if (partState.isChecked()) {
                            // check to be repaired
                            if (bikeParts[i].isToBeRepaired())
                                numParts++;
                        } else {
                            // check repaired ==> finished
                            if(bikeParts[i].isFinished())
                                numParts++;
                        }
                    }
                }
            }
        }

        ((TextView) findViewById(R.id.reportStatsTV)).setText(String.valueOf(numReports));
        ((TextView) findViewById(R.id.partStatsTV)).setText(String.valueOf(numParts));
    }

    public void onPartStateBtnClicked(View view) {
        initStatisticsView();
    }

    public void onChipGroupClicked(View view) {
        initStatisticsView();
    }
}