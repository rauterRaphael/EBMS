package com.f.ebms.ui.newReport;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;
import com.f.ebms.ui.partsList.PartsRecyclerViewAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewReportActivity extends AppCompatActivity implements NewReportPartsRecyclerViewAdapter.ItemClickListener{

    private static final String LOG_TAG = NewReportActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    private RecyclerView partsRecyclerView;
    private NewReportPartsRecyclerViewAdapter newReportPartsRVA;

    private HashMap<Integer, BikePart> bikePartHashMap;

    private SearchView partsSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        this.ebmsDatabase = new EBMSDatabase(this);
        this.bikePartHashMap = this.ebmsDatabase.getAllBikeParts();
        this.partsSearchView = findViewById(R.id.searchET);

        partsRecyclerView = findViewById(R.id.newReportPartsListRV);
        partsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.initPartListRecyclerView();

        this.partsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                newReportPartsRVA.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newReportPartsRVA.filter(newText);
                return true;
            }
        });

        Log.i(LOG_TAG, "EBMS - EBMSDatabase - onCreate() - NewReportActivity created");
    }

    public void addReport(View view) {
        Toast.makeText(getApplicationContext(),"Storing report in database: ",
                Toast.LENGTH_SHORT).show();
        String employeeName     = ((EditText) findViewById(R.id.employeeNameET)).getText().toString();
        String licensePlate     = ((EditText) findViewById(R.id.licensePlateET)).getText().toString();
        String qrCode           = ((EditText) findViewById(R.id.qrCodeET)).getText().toString();
        String kilometerStatus  = ((EditText) findViewById(R.id.kilometerStatusET)).getText().toString();

        if(kilometerStatus.equals(""))
            kilometerStatus = "0";

        BikePart[] parts = new BikePart[this.bikePartHashMap.size()];

        for(int i=0; i<this.bikePartHashMap.size(); i++){
            parts[i] = this.bikePartHashMap.get(i);
        }

        Report newReport = new Report(employeeName,
                qrCode,
                licensePlate,
                Integer.parseInt(kilometerStatus),
                parts);
        this.ebmsDatabase.addReportEntry(newReport);
        Toast.makeText(getApplicationContext(),"Report added",
                Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initPartListRecyclerView(){
        ArrayList<String> parts = new ArrayList<>();

        for(Map.Entry<Integer, BikePart> e : this.bikePartHashMap.entrySet()) {
            parts.add((e.getKey()).toString() + ";" + ((e.getValue())).getPartName());
        }

        newReportPartsRVA = new NewReportPartsRecyclerViewAdapter(this, parts);
        newReportPartsRVA.setClickListener(this);
        partsRecyclerView.setAdapter(newReportPartsRVA);

    }

    @Override
    public void onItemClick(View view, int position) {

        String partListItem = newReportPartsRVA.getItem(position);
        int partIdx     = Integer.parseInt(partListItem.split(";")[0]);
        String partName = partListItem.split(";")[1];

        Context context = view.getContext();
        LinearLayout alertLayout = new LinearLayout(context);
        alertLayout.setOrientation(LinearLayout.VERTICAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final TextView partNameTV = new TextView(this);
        partNameTV.setText(partName);
        partNameTV.setId(partIdx);
        partNameTV.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24.f);
        alertLayout.addView(partNameTV);

        final TextView toBeRepairedTBLabel = new TextView(this);
        toBeRepairedTBLabel.setText(R.string.newreport_aldlg_toBeRepaired);
        toBeRepairedTBLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.f);
        alertLayout.addView(toBeRepairedTBLabel);

        final ToggleButton toBeRepairedTB = new ToggleButton(this);
        toBeRepairedTB.setTextOff("NO");
        toBeRepairedTB.setTextOn("YES");
        toBeRepairedTB.setChecked(false);
        alertLayout.addView(toBeRepairedTB);

        final TextView finishedTBLabel = new TextView(this);
        finishedTBLabel.setText(R.string.newreport_aldlg_finished);
        finishedTBLabel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.f);
        alertLayout.addView(finishedTBLabel);

        final ToggleButton finishedTB = new ToggleButton(this);
        finishedTB.setTextOff("NO");
        finishedTB.setTextOn("YES");
        finishedTB.setChecked(false);
        alertLayout.addView(finishedTB);

        final EditText notesET = new EditText(this);
        notesET.setInputType(InputType.TYPE_CLASS_TEXT);
        notesET.setHint(R.string.newreport_aldlg_notes);
        alertLayout.addView(notesET);

        builder.setView(alertLayout);

        builder.setMessage("").setCancelable(true)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int partIdx = partNameTV.getId();

                        boolean toBeRepaired = toBeRepairedTB.isChecked();
                        boolean finished     = finishedTB.isChecked();
                        String  notes        = notesET.getText().toString();

                        setBikePartProperties(partIdx, toBeRepaired, finished, notes);
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.newreport_aldlg_title);
        alert.show();
    }

    private void setBikePartProperties(int partIdx, boolean toBeRepaired, boolean finished, String notes) {
        BikePart tempBikePart = this.bikePartHashMap.get(partIdx);
        if(tempBikePart != null){
            this.bikePartHashMap.get(partIdx).setToBeRepaired(toBeRepaired);
            this.bikePartHashMap.get(partIdx).setFinished(finished);
            this.bikePartHashMap.get(partIdx).setNotes(notes);
        }else{
            Log.i(LOG_TAG, "Couldnt find bike part");
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}