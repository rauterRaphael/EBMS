package com.f.ebms.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;

public class PartListActivity extends AppCompatActivity {

    private static final String LOG_TAG = PartListActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        this.ebmsDatabase = new EBMSDatabase(this);
        Log.i(LOG_TAG, "EBMS - PartListActivity - onCreate() - PartListActivity created");
    }
}