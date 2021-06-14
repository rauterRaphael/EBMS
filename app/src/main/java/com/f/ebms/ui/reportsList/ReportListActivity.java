package com.f.ebms.ui.reportsList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f.ebms.R;
import com.f.ebms.db.EBMSDatabase;
import com.f.ebms.db.dbObjects.BikePart;
import com.f.ebms.db.dbObjects.Report;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Log.i(LOG_TAG, "Sharing is caring");
                        // create pdf from report
                        Toast.makeText(getApplicationContext(),"Creating the report...",
                                Toast.LENGTH_SHORT).show();
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
                            Report tmp = ebmsDatabase.getReportEntryByIdx(reportDBIdx);
                            if(tmp != null)
                            {
                                String fileName = tmp.getEmployeeName() + "_" + tmp.getLicensePlate() + "_" + dateFormat.format(tmp.getReportDateTime()) + ".pdf";

                                File reportFile = new File(getExternalFilesDir(null), fileName);
                                if(reportFile.exists())
                                    reportFile.delete();

                                Document document = new Document();
                                PdfWriter.getInstance(document, new FileOutputStream(new File(getExternalFilesDir(null), fileName)));

                                document.open();
                                document.addAuthor("Raphael Rauter");
                                document.addCreator("Spongebot");
                                document.addTitle("EBMS - report");

                                PdfPTable headerTable = new PdfPTable(3);
                                headerTable.addCell("Datum: " + dateFormat.format(tmp.getReportDateTime()));
                                headerTable.addCell("Name: " + tmp.getEmployeeName());
                                headerTable.addCell("Kilometer: " + tmp.getKilometerStatus());
                                headerTable.addCell("Kennzeichen: " + tmp.getLicensePlate());
                                headerTable.addCell("QR Code: " + tmp.getQrCode());
                                headerTable.addCell("Name: Spongebob");

                                document.add(headerTable);

                                Chunk linebreak = new Chunk(new DottedLineSeparator());
                                document.add(linebreak);

                                PdfPTable bodyTable = new PdfPTable(4);
                                bodyTable.setPaddingTop(10f);

                                bodyTable.addCell("Name der Teile");
                                bodyTable.addCell("zu reparieren");
                                bodyTable.addCell("erledigt");
                                bodyTable.addCell("Bemerkungen");

                                BikePart[] bikePartList = tmp.getBikePartList();
                                for(int i=0; i<bikePartList.length; i++){
                                    if(bikePartList[i] != null){
                                        bodyTable.addCell(bikePartList[i].getPartName());

                                        if(bikePartList[i].isToBeRepaired())
                                            bodyTable.addCell(getCheckedImage());
                                        else
                                            bodyTable.addCell(getUnCheckedImage());

                                        if(bikePartList[i].isFinished())
                                            bodyTable.addCell(getCheckedImage());
                                        else
                                            bodyTable.addCell(getUnCheckedImage());

                                        bodyTable.addCell(bikePartList[i].getNotes());
                                    }
                                }

                                document.add(bodyTable);

                                document.add(linebreak);

                                PdfPTable footerTable = new PdfPTable(1);
                                footerTable.setPaddingTop(10f);
                                footerTable.addCell("Quality Check:             ");
                                document.add(footerTable);

                                document.close();

                                String toastText = reportFile.getAbsolutePath();
                                Toast.makeText(getApplicationContext(),"Report saved at " + toastText,
                                        Toast.LENGTH_LONG).show();

                                Intent docIntent = new Intent();
                                docIntent.setAction(Intent.ACTION_SEND);
                                docIntent.putExtra(Intent.EXTRA_STREAM, reportFile.toURI());
                                docIntent.setType("application/pdf");

                                Intent shareIntent = Intent.createChooser(docIntent, null);
                                startActivity(shareIntent);

                            }

                        }catch(Exception e){
                            e.printStackTrace();
                        }

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

    private PdfPTable getCheckedImage(){
        try {
            InputStream is = this.getResources().openRawResource(R.raw.checkedbox);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = Image.getInstance(stream.toByteArray());

            PdfPTable checkedImgTable = new PdfPTable(3);

            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(0);

            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);

            PdfPCell imgCell = new PdfPCell(img);
            imgCell.setBorder(0);
            checkedImgTable.addCell(imgCell);

            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);
            checkedImgTable.addCell(emptyCell);

            return checkedImgTable;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private PdfPTable getUnCheckedImage(){
        try {
            InputStream is = this.getResources().openRawResource(R.raw.uncheckedbox);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = Image.getInstance(stream.toByteArray());

            PdfPTable uncheckedImgTable = new PdfPTable(3);

            PdfPCell emptyCell = new PdfPCell();
            emptyCell.setBorder(0);

            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);

            PdfPCell imgCell = new PdfPCell(img);
            imgCell.setBorder(0);
            uncheckedImgTable.addCell(imgCell);

            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);
            uncheckedImgTable.addCell(emptyCell);

            return uncheckedImgTable;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}