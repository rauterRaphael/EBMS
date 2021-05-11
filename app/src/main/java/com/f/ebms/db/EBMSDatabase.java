package com.f.ebms.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;

import java.util.HashMap;

public class EBMSDatabase {

    private static final String LOG_TAG = EBMSDatabase.class.getName();

    private SQLiteDatabase ebmsDB;

    private HashMap<Integer, Report> allReports;
    private HashMap<Integer, BikePart> allBikeParts;

    public EBMSDatabase(Context context) {
        try {
            this.ebmsDB = context.openOrCreateDatabase("EBMS", Context.MODE_PRIVATE, null);
            this.allReports = new HashMap<Integer, Report>();
            this.allBikeParts = new HashMap<Integer, BikePart>();

            if (!checkIfPartsTableInitialized())
                loadDefaultPartsData();

            this.ebmsDB.execSQL("CREATE TABLE  IF NOT EXISTS reports (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, report TEXT)");

            getAllBikePartObjects();
            getAllReportObjects();

        }catch (Exception e){
            Log.e(LOG_TAG, "Database() - Exception: " + e.toString());
        }
    }

    private boolean checkIfPartsTableInitialized()
    {
        try {
            this.ebmsDB.execSQL("SELECT * FROM parts;");
        } catch (SQLiteException e) {
            if (e.getMessage().contains("no such table")) {
                return false;
            }
        } catch (Exception e) {
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
        try {

            this.ebmsDB.execSQL("CREATE TABLE IF NOT EXISTS parts (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, bikePart TEXT)");

            for (String part : defaultParts) {
                String bikePartJson = DBObjectConverter.convertBikePartToJson(new BikePart(part));
                this.ebmsDB.execSQL("INSERT INTO parts (bikePart) VALUES ('" + bikePartJson + "');");
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "loadDefaultPartsData() - Exception: " + e.toString());
        }
    }

    public HashMap<Integer, Report> getAllReports() {
        return allReports;
    }

    public HashMap<Integer, BikePart> getAllBikeParts() {
        return allBikeParts;
    }

    private void getAllReportObjects() {
        try {
            Cursor c = this.ebmsDB.rawQuery("SELECT * FROM reports", null);

            int idIdx = c.getColumnIndex("ID");
            int reportIdx = c.getColumnIndex("report");

            c.moveToFirst();

            while (c != null) {
                this.allReports.put(c.getInt(idIdx), DBObjectConverter.getReportFromJson(c.getString(reportIdx)));
                c.moveToNext();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "getAllReportObjects() - Exception: " + e.toString());
        }
    }

    private void getAllBikePartObjects() {
        try {
            Cursor c = this.ebmsDB.rawQuery("SELECT * FROM parts", null);

            int idIdx = c.getColumnIndex("ID");
            int partIdx = c.getColumnIndex("bikePart");

            c.moveToFirst();

            while (c != null) {
                this.allBikeParts.put(c.getInt(idIdx), DBObjectConverter.getBikePartFromJson(c.getString(partIdx)));
                c.moveToNext();
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, "getAllReportObjects() - Exception: " + e.toString());
        }
    }

    public boolean addReportEntry(Report report) {
        try {
            String reportJson = DBObjectConverter.convertReportToJson(report);
            this.ebmsDB.execSQL("INSERT INTO reports (report) VALUES ('" + reportJson + "');");
        } catch (Exception e) {
            Log.e(LOG_TAG, "addReportEntry() - Exception: " + e.toString());
            return false;
        }
        getAllReportObjects();
        return true;
    }

    public boolean editReportEntry(int entryIdx, Report editedReport) {
        try {
            String reportJson = DBObjectConverter.convertReportToJson(editedReport);
            this.ebmsDB.execSQL("UPDATE reports SET report='" + reportJson + "' WHERE ID=" + entryIdx + ";");
        } catch (Exception e) {
            Log.e(LOG_TAG, "editReportEntry() - Exception: " + e.toString());
            return false;
        }
        getAllReportObjects();
        return true;
    }

    public boolean deleteReportEntry(int entryIdx) {
        try {
            this.ebmsDB.execSQL("DELETE FROM reports WHERE ID=" + entryIdx + ";");
        } catch (Exception e) {
            Log.e(LOG_TAG, "deleteReportEntry() - Exception: " + e.toString());
            return false;
        }
        getAllReportObjects();
        return true;
    }

    public boolean addBikePartEntry(BikePart bikePart) {
        try {
            String bikePartJson = DBObjectConverter.convertBikePartToJson(bikePart);
            this.ebmsDB.execSQL("INSERT INTO parts (bikePart) VALUES ('" + bikePartJson + "');");
        } catch (Exception e) {
            Log.e(LOG_TAG, "addBikePartEntry() - Exception: " + e.toString());
            return false;
        }
        getAllBikePartObjects();
        return true;
    }

    public boolean editBikePartEntry(int entryIdx, BikePart editedBikePart) {
        try {
            String bikePartJson = DBObjectConverter.convertBikePartToJson(editedBikePart);
            this.ebmsDB.execSQL("UPDATE parts SET bikePart='" + bikePartJson + "' WHERE ID=" + entryIdx + ";");
        } catch (Exception e) {
            Log.e(LOG_TAG, "editBikePartEntry() - Exception: " + e.toString());
            return false;
        }
        getAllBikePartObjects();
        return true;
    }

    public boolean deleteBikePartEntry(int entryIdx) {
        try {
            this.ebmsDB.execSQL("DELETE FROM parts WHERE ID=" + entryIdx + ";");
        } catch (Exception e) {
            Log.e(LOG_TAG, "deleteBikePartEntry() - Exception: " + e.toString());
            return false;
        }
        getAllBikePartObjects();
        return true;
    }

}
