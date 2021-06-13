package com.f.ebms.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.f.ebms.R;

import java.io.File;

public class ReportViewActivity extends AppCompatActivity {

    private File reportFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        String fileName;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            fileName = extras.getString("FILE");
            this.reportFile = new File(getExternalFilesDir(null), fileName);
        }
    }

    public void shareBtnPressed(View view) {

    }
}