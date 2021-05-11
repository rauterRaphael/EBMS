package com.f.ebms.db;

import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DBObjectConverter {

    public static String convertReportToJson(Report report) {
        return new GsonBuilder().create().toJson(report, Report.class);
    }

    public static Report getReportFromJson(String jsonObject)
    {
        return new Gson().fromJson(jsonObject, Report.class);
    }

    public static String convertBikePartToJson(BikePart bikePart) {
        return new GsonBuilder().create().toJson(bikePart, BikePart.class);
    }

    public static BikePart getBikePartFromJson(String jsonObject)
    {
        return new Gson().fromJson(jsonObject, BikePart.class);
    }
}
