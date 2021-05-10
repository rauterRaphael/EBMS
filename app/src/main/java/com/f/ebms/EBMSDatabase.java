package com.f.ebms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import javax.xml.transform.Result;

public class EBMSDatabase {

    private static final String LOG_TAG = EBMSDatabase.class.getName();

    private SQLiteDatabase ebmsDB;

    public EBMSDatabase(Context context)
    {
        try{
            this.ebmsDB    = context.openOrCreateDatabase("EBMS", Context.MODE_PRIVATE, null);

            if(!checkIfPartsTableInitialized())
                loadDefaultPartsData();

        }catch (Exception e){
            Log.e(LOG_TAG, "Database() - Exception: " + e.toString());
        }
    }

    private boolean checkIfPartsTableInitialized()
    {
        try{
            Log.i(LOG_TAG, "Check if initialized");
        }catch (Exception e){
            Log.e(LOG_TAG, "checkIfPartsTableInitialized() - Exception: " + e.toString());
        }
        return true;
    }


    private void loadDefaultPartsData()
    {
        String[] defaultParts = {
                "Front Suspension Assy",
                "Rear cushion Assembly",
                "Motoröl wechseln",
                "Bremsflüssigkeit kontrollieren und wechseln",
                "Kette saubermachen+Einstellen",
                "Primary Battery",
                "Throttle Cable",
                "Throttle Pipe Body",
                "Left Handle Grip",
                "Dashboard",
                "Right Brake Lever",
                "Left Brake Lever",
                "Hook",
                "Hook Rubber",
                "Front Fender",
                "Rear Fender",
                "Upper Handlebar Cover",
                "Back Handlebar Cover",
                "Front Handlebar Cover",
                "Front Panel",
                "Front Cover Rubber",
                "Right Handle Switch Assy",
                "Left Handle Switch Assy",
                "Front Under Spoiler Assy",
                "Floor Cover",
                "Upper Inner Cover",
                "Lower Front Inner Cover",
                "Right Lower Side Cover",
                "Left Lower Side Cover",
                "Right Lower Cap",
                "Left Lower Cap",
                "Rear Brake Pads",
                "Front Brake Pads",
                "Right Step Bar",
                "Left Step Bar",
                "Rear Cover",
                "Central under cover A",
                "Rear Under Cover B",
                "Rear Light Combination",
                "Front Headlight Assy",
                "Kick Stand",
                "Main Stand",
                "Kennzeichen"
        };
        try{
            this.ebmsDB.execSQL("CREATE TABLE parts (name VARCHAR)");
            for(String part:defaultParts)
                this.ebmsDB.execSQL("INSERT INTO parts (name) VALUES (" + part + ");");

        }catch (Exception e){
            Log.e(LOG_TAG, "loadDefaultPartsData() - Exception: " + e.toString());
        }
    }

}
