package com.f.ebms.ui.reportsList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;
import com.f.ebms.ui.partsList.PartsRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReportListActivity extends AppCompatActivity implements ReportsRecyclerViewAdapter.ItemClickListener{

    private static final String LOG_TAG = ReportListActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    private RecyclerView reportsRecyclerView;
    private ReportsRecyclerViewAdapter reportsListRVA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        this.ebmsDatabase = new EBMSDatabase(this);
        reportsRecyclerView = findViewById(R.id.reportsListRV);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.initPartListRecyclerView();
        Log.i(LOG_TAG, "EBMS - ReportListActivity - onCreate() - ReportListActivity created");
    }

    private void initPartListRecyclerView() {
        HashMap<Integer, Report> reportsHashMap = this.ebmsDatabase.getAllReports();
        ArrayList<String> reports = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");

        for(Map.Entry<Integer, Report> e : reportsHashMap.entrySet()) {
            reports.add((e.getKey()).toString() + ";" + ((e.getValue())).getLicensePlate() + " " + dateFormat.format(((e.getValue())).getReportDateTime()));
        }

        reportsListRVA = new ReportsRecyclerViewAdapter(this, reports);
        reportsListRVA.setClickListener(this);
        reportsRecyclerView.setAdapter(reportsListRVA);
    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final int reportDBIdx = Integer.parseInt(reportsListRVA.getItem(position).split(";")[0]);

        builder.setMessage("Share or delete report").setCancelable(true)
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(LOG_TAG, "Sharing is caring");
                        // create pdf from report
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteReportAndUpdateList(reportDBIdx);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.reportlist_dlg_edit_title);
        alert.show();
    }

    private void deleteReportAndUpdateList(int reportIdx) {
        this.ebmsDatabase.deleteReportEntry(reportIdx);
        this.initPartListRecyclerView();
        Toast.makeText(getApplicationContext(),"Report deleted",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}