package com.f.ebms.ui.partsList;

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
import android.widget.SearchView;
import android.widget.Toast;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.BikePart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartListActivity extends AppCompatActivity  implements PartsRecyclerViewAdapter.ItemClickListener{

    private static final String LOG_TAG = PartListActivity.class.getName();

    private EBMSDatabase ebmsDatabase;

    private RecyclerView partsRecyclerView;
    private PartsRecyclerViewAdapter partsListRVA;

    private SearchView partsSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_list);
        this.ebmsDatabase = new EBMSDatabase(this);
        partsRecyclerView = findViewById(R.id.partsListRV);
        partsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.initPartListRecyclerView();
        this.partsSearchView = findViewById(R.id.partsSearchSV);

        this.partsSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                partsListRVA.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                partsListRVA.filter(newText);
                return true;
            }
        });

        Log.i(LOG_TAG, "EBMS - PartListActivity - onCreate() - PartListActivity created");
    }

    private void initPartListRecyclerView(){
        HashMap<Integer, BikePart> bikePartHashMap = this.ebmsDatabase.getAllBikeParts();
        ArrayList<String> parts = new ArrayList<>();

        for(Map.Entry<Integer, BikePart> e : bikePartHashMap.entrySet()) {
            parts.add((e.getKey()).toString() + ";" + ((e.getValue())).getPartName());
        }

        partsListRVA = new PartsRecyclerViewAdapter(this, parts);
        partsListRVA.setClickListener(this);
        partsRecyclerView.setAdapter(partsListRVA);

    }

    @Override
    public void onItemClick(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText partNameET = new EditText(this);
        partNameET.setInputType(InputType.TYPE_CLASS_TEXT);
        String partsListItem = partsListRVA.getItem(position);
        partNameET.setText(partsListItem.split(";")[1]);
        partNameET.setId(Integer.parseInt(partsListItem.split(";")[0]));
        builder.setView(partNameET);

        builder.setMessage("Edit name:").setCancelable(true)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String updatedName = partNameET.getText().toString();
                        editPartAndUpdateList(partNameET.getId(), updatedName);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePartAndUpdateList(partNameET.getId());
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle(R.string.partslist_dlg_edit_title);
        alert.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void storePartAndUpdateList(String newPartName){
        Toast.makeText(getApplicationContext(),"Storing new part in database: " + newPartName,
                Toast.LENGTH_SHORT).show();
        BikePart newBikePart = new BikePart(newPartName);
        this.ebmsDatabase.addBikePartEntry(newBikePart);
        this.initPartListRecyclerView();
    }

    public void editPartAndUpdateList(int bikePartIdx, String updatedPartName){
        this.ebmsDatabase.editBikePartEntry(bikePartIdx, new BikePart(updatedPartName));
        this.initPartListRecyclerView();
        Toast.makeText(getApplicationContext(),"Part " + updatedPartName + " updated",
                Toast.LENGTH_SHORT).show();
    }

    public void deletePartAndUpdateList(int bikePartIdx){
        this.ebmsDatabase.deleteBikePartEntry(bikePartIdx);
        this.initPartListRecyclerView();
        Toast.makeText(getApplicationContext(),"Part deleted",
                Toast.LENGTH_SHORT).show();
    }


    public void onAddPartBtnClick(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText partNameET = new EditText(this);
        partNameET.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(partNameET);

        builder.setMessage("Part name:").setCancelable(true)
            .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String partName = partNameET.getText().toString();
                    storePartAndUpdateList(partName);
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.setTitle(R.string.partslist_dlg_add_title);
            alert.show();
    }

}