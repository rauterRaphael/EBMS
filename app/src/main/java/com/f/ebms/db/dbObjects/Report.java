package com.f.ebms.db.dbObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;

public class Report {
    private String employeeName;
    private String qrCode;
    private String licensePlate;

    private int kilometerStatus;

    private long reportDateTime;

    private BikePart[] bikePartList;


    public Report(String employeeName, String qrCode, String licensePlate, int kilometerStatus, BikePart[] storedBikeParts) {
        this.qrCode = qrCode;
        this.employeeName = employeeName;
        this.licensePlate = licensePlate;
        this.kilometerStatus = kilometerStatus;
        this.reportDateTime = System.currentTimeMillis();
        this.bikePartList = storedBikeParts;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getKilometerStatus() {
        return kilometerStatus;
    }

    public void setKilometerStatus(int kilometerStatus) {
        this.kilometerStatus = kilometerStatus;
    }

    public long getReportDateTime() {
        return reportDateTime;
    }

    public BikePart[] getBikePartList() {
        return bikePartList;
    }

    @Override
    public String toString() {
        return "Report{" +
                "employeeName='" + employeeName + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", kilometerStatus=" + kilometerStatus +
                ", reportDateTime=" + reportDateTime +
                ", bikePartList=" + Arrays.toString(bikePartList) +
                '}';
    }
}
