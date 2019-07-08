package com.prateek.isafeassist.payments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.prateek.isafeassist.MainActivity;
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.fragments.BikesFragment;
import com.prateek.isafeassist.fragments.CarFragment;
import com.prateek.isafeassist.fragments.DefaultFragment;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TransactionActivity extends AppCompatActivity {

    TextView transid, memid, packname, vehname;
    Button button;
    String name;
    String contact;
    String address;
    String vehiclemake;
    String model;
    String year;
    String regno;
    String cat, startdate, enddate;
    private static final int STORAGE_CODE = 1000;
    FirebaseAuth auth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        transid = findViewById(R.id.transid);
        memid = findViewById(R.id.memid);
        packname = findViewById(R.id.packname);
        vehname = findViewById(R.id.vehname);
        button = findViewById(R.id.receipt_btn);
        auth = FirebaseAuth.getInstance();
        Window window = TransactionActivity.this.getWindow();
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(TransactionActivity.this, R.color.mystatus));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());
        Bundle bundle = getIntent().getExtras();
        String tid = bundle.getString("Transactionid");
        String cat = bundle.getString("Cat");
        String vehicle = bundle.getString("vehicle");
        if (cat.equals("1")) {
            packname.setText("Car Membership");
            memid.setText(PaymentActivity.memno);
            //vehname.setText(vehicle);
        } else {
            packname.setText("Bike Membership");
            memid.setText(PaymentActivity.memno);
            //vehname.setText(vehicle);

        }
        transid.setText(tid);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    String permissions[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_CODE);
                } else {

                    savepdf();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    private void savepdf() {

        final Document document = new Document();
        final String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        final String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

        if (DefaultFragment.var == 0) {


            databaseReference.child("Bike Package").child(BikesFragment.key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(dataSnapshot.getValue());
                    System.out.println(BikesFragment.key);
                    //System.out.println(ds.getValue());

                    name = dataSnapshot.child("firstname").getValue(String.class);
                    contact = dataSnapshot.child("mobno").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    vehiclemake = dataSnapshot.child("bikemake").getValue(String.class);
                    model = dataSnapshot.child("bikemodel").getValue(String.class);
                    year = dataSnapshot.child("year").getValue(String.class);
                    startdate = dataSnapshot.child("Payments").child("purchasedate").getValue(String.class);
                    enddate = dataSnapshot.child("Payments").child("expirydate").getValue(String.class);
                    regno = dataSnapshot.child("regno").getValue(String.class);
                    vehname.setText(vehiclemake);
                    cat = "Bike";

                    System.out.println(name + contact + address + vehiclemake + model + year + regno + cat);


                    try {

                        PdfWriter.getInstance(document, new FileOutputStream(filepath));
                        document.open();

                        document.addAuthor("IRSC");

                        Font isafe = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
                        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                        Font miscfont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
                        Font boldf = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
                        Font boldnn = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
                        Font allfo = new Font(Font.FontFamily.TIMES_ROMAN, 11);


                        document.add(new Paragraph("iSAFE ASSIST", isafe));
                        document.add(new Paragraph("262, Lane No. 4, Westend Marg, Saket, Delhi- 110030"));
                        Paragraph p1 = new Paragraph("24x7 Roadside Assistance Membership Certificate", boldnn);
                        //p1.add("iSAFE ASSIST");
                        p1.setAlignment(Element.ALIGN_CENTER);
                        document.add(p1);
                        //document.add(new Paragraph("24x7 Roadside Assistance Membership Certificate",boldFont));
                        document.add(new Paragraph("Dear Member,", allfo));
                        document.add(new Paragraph(" Congratulations & welcome to iSAFE ASSIST.", allfo));
                        document.add(new Paragraph("  Thank you for choosing our 24x7 Roadside Assistance (RSA) membership. This certificate is valid " +
                                "for the period mentioned below and your membership will get activated after 48 hours from the time of enrollment. Please" +
                                " read the Terms and Conditions to understand the services and we recommend you to retain a copy of same for your vehicle" +
                                " for easy reference. ", allfo));
                        document.add(new Paragraph("Customer Details:", boldFont));
                        document.add(new Paragraph(" Name: " + name, allfo));
                        document.add(new Paragraph(" Address: " + address, allfo));
                        document.add(new Paragraph(" Contact No: " + contact, allfo));
                        document.add(new Paragraph(" Vehicle Details: \n ", boldFont));
                        PdfPTable table = new PdfPTable(5/*new float[]{2, 1, 2}*/);
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(" Vehicle category ");
                        table.addCell(" Vehicle Make ");
                        table.addCell(" Vehicle Model ");
                        table.addCell(" Year of Manufacturing ");
                        table.addCell(" Registration No ");
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j = 0; j < cells.length; j++) {
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        for (int i = 1; i < 2; i++) {
                            table.addCell(cat);
                            table.addCell(vehiclemake);
                            table.addCell(model);
                            table.addCell(year);
                            table.addCell(regno);
                        }

                        document.add(table);

                        document.add(new Paragraph("Product, Membership details and Price: \n ", boldFont));

                        PdfPTable table2 = new PdfPTable(7/*new float[]{2, 1, 2}*/);
                        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table2.addCell(" Type of Membership ");
                        table2.addCell("  Membership No  ");
                        table2.addCell("  Membership Start Date  ");
                        table2.addCell("  Membership End Date  ");
                        table2.addCell("Product Price(INR)");
                        table2.addCell("GST(18%) (INR)");
                        table2.addCell("TOTAL (INR)");
                        table2.setHeaderRows(1);
                        PdfPCell[] cells2 = table2.getRow(0).getCells();
                        for (int j = 0; j < cells2.length; j++) {
                            cells2[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        for (int i = 1; i < 2; i++) {
                            table2.addCell("RSA");
                            table2.addCell(PaymentActivity.memno);
                            table2.addCell(startdate);
                            table2.addCell(enddate);
                            table2.addCell("240/-");
                            table2.addCell("43.20");
                            table2.addCell("283.20/-");
                        }

                        document.add(table2);


                        document.add(new Paragraph("\n Customer declaration:", boldFont));

                        document.add(new Paragraph("I certify that I have read and hereby accept the terms and conditions of the Membership.", miscfont));
                        document.add(new Paragraph("I understand that the Service Provider's obligations apply only for the period stated on this Certificate.", miscfont));
                        document.add(new Paragraph("I understand that after 15 days from the date of issue of the Membership Certificate, I cannot seek cancellation  of " +
                                "Membership and the Company shall have no obligation to refund. I also acknowledge that I cannot request for cancellation if " +
                                "I had availed the service within the said period of 15 days.", miscfont));

                        document.add(new Paragraph("I declare that the information provided by me in this validation form is true to the best of my knowledge. I confirm that in the" +
                                "event any information provided by me is found to be untrue or incomplete or violating any Terms and Condition of  this " +
                                "Program, iSAFE ASSIST shall be entitled to terminate my Membership immediately.", miscfont));
                        document.add(new Paragraph("This is a computer-generated membership certificate and does not require any signature.", miscfont));

                        document.add(new Paragraph("\n Terms and Conditions are subject to change without notice. Please visit our website isafeassist.org for the updated Terms and" +
                                " Conditions. PLEASE SAVE the iSAFE TOLL FREE Number 1-800 419 7779 to your phone now.", miscfont));

                        document.add(new Paragraph(" For 24x7 Road Side Assistance, please call our Toll Free No: 1-800 419 7779", miscfont));

                        document.add(new Paragraph("For any queries and enquiries on your Membership, please contact us at info@road-safety.co.in"));


                        document.close();
                        Toast.makeText(TransactionActivity.this, filename + ".pdf \n is saved to \n" + filepath, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(TransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            databaseReference.child("Car Package").child(CarFragment.carkey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    name = dataSnapshot.child("firstname").getValue(String.class);
                    contact = dataSnapshot.child("mobno").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    year = dataSnapshot.child("year").getValue(String.class);
                    vehiclemake = dataSnapshot.child("carmake").getValue(String.class);
                    model = dataSnapshot.child("carmodel").getValue(String.class);
                    regno = dataSnapshot.child("regno").getValue(String.class);
                    startdate = dataSnapshot.child("Payments").child("purchasedate").getValue(String.class);
                    enddate = dataSnapshot.child("Payments").child("expirydate").getValue(String.class);
                    vehname.setText(vehiclemake);

                    cat = "Car";

                    try {

                        PdfWriter.getInstance(document, new FileOutputStream(filepath));
                        document.open();

                        document.addAuthor("IRSC");

                        Font isafe = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
                        Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                        Font miscfont = new Font(Font.FontFamily.TIMES_ROMAN, 11);
                        Font boldf = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
                        Font boldnn = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
                        Font allfo = new Font(Font.FontFamily.TIMES_ROMAN, 11);


                        document.add(new Paragraph("iSAFE ASSIST", isafe));
                        document.add(new Paragraph("262, Lane No. 4, Westend Marg, Saket, Delhi- 110030"));
                        Paragraph p1 = new Paragraph("24x7 Roadside Assistance Membership Certificate", boldnn);
                        //p1.add("iSAFE ASSIST");
                        p1.setAlignment(Element.ALIGN_CENTER);
                        document.add(p1);
                        //document.add(new Paragraph("24x7 Roadside Assistance Membership Certificate",boldFont));
                        document.add(new Paragraph("Dear Member,", allfo));
                        document.add(new Paragraph(" Congratulations & welcome to iSAFE ASSIST.", allfo));
                        document.add(new Paragraph("  Thank you for choosing our 24x7 Roadside Assistance (RSA) membership. This certificate is valid " +
                                "for the period mentioned below and your membership will get activated after 48 hours from the time of enrollment. Please" +
                                " read the Terms and Conditions to understand the services and we recommend you to retain a copy of same for your vehicle" +
                                " for easy reference. ", allfo));
                        document.add(new Paragraph("Customer Details:", boldFont));
                        document.add(new Paragraph(" Name: " + name, allfo));
                        document.add(new Paragraph(" Address: " + address, allfo));
                        document.add(new Paragraph(" Contact No: " + contact, allfo));
                        document.add(new Paragraph(" Vehicle Details: \n ", boldFont));
                        PdfPTable table = new PdfPTable(5/*new float[]{2, 1, 2}*/);
                        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell("Vehicle category");
                        table.addCell("Vehicle Make");
                        table.addCell("Vehicle Model");
                        table.addCell("Year of Manufacturing");
                        table.addCell("Registration No");
                        table.setHeaderRows(1);
                        PdfPCell[] cells = table.getRow(0).getCells();
                        for (int j = 0; j < cells.length; j++) {
                            cells[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        for (int i = 1; i < 2; i++) {
                            table.addCell(cat);
                            table.addCell(vehiclemake);
                            table.addCell(model);
                            table.addCell(year);
                            table.addCell(regno);
                        }

                        document.add(table);

                        document.add(new Paragraph("Product, Membership details and Price: \n ", boldFont));

                        PdfPTable table2 = new PdfPTable(7/*new float[]{2, 1, 2}*/);
                        table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                        table2.addCell("Type of Membership");
                        table2.addCell("Membership No ");
                        table2.addCell("Membership Start Date and Time");
                        table2.addCell("Membership End Date and Time");
                        table2.addCell("Product Price(INR)");
                        table2.addCell("GST(18%) (INR)");
                        table2.addCell("TOTAL (INR)");
                        table2.setHeaderRows(1);
                        PdfPCell[] cells2 = table2.getRow(0).getCells();
                        for (int j = 0; j < cells2.length; j++) {
                            cells2[j].setBackgroundColor(BaseColor.GRAY);
                        }
                        for (int i = 1; i < 2; i++) {
                            table2.addCell("RSA");
                            table2.addCell(PaymentActivity.memno);
                            table2.addCell(startdate);
                            table2.addCell(enddate);
                            table2.addCell("720/-");
                            table2.addCell("129.60");
                            table2.addCell("849.60/-");
                        }

                        document.add(table2);


                        document.add(new Paragraph("\n Customer declaration:", boldFont));

                        document.add(new Paragraph("I certify that I have read and hereby accept the terms and conditions of the Membership.", miscfont));
                        document.add(new Paragraph("I understand that the Service Provider's obligations apply only for the period stated on this Certificate.", miscfont));
                        document.add(new Paragraph("I understand that after 15 days from the date of issue of the Membership Certificate, I cannot seek cancellation  of " +
                                "Membership and the Company shall have no obligation to refund. I also acknowledge that I cannot request for cancellation if " +
                                "I had availed the service within the said period of 15 days.", miscfont));

                        document.add(new Paragraph("I declare that the information provided by me in this validation form is true to the best of my knowledge. I confirm that in the" +
                                "event any information provided by me is found to be untrue or incomplete or violating any Terms and Condition of  this " +
                                "Program, iSAFE ASSIST shall be entitled to terminate my Membership immediately.", miscfont));
                        document.add(new Paragraph("This is a computer-generated membership certificate and does not require any signature.", miscfont));

                        document.add(new Paragraph("\n Terms and Conditions are subject to change without notice. Please visit our website isafeassist.org for the updated Terms and" +
                                " Conditions. PLEASE SAVE the iSAFE TOLL FREE Number 1-800 419 7779 to your phone now.", miscfont));

                        document.add(new Paragraph(" For 24x7 Road Side Assistance, please call our Toll Free No: 1-800 419 7779", miscfont));

                        document.add(new Paragraph("For any queries and enquiries on your Membership, please contact us at info@road-safety.co.in"));


                        document.close();
                        Toast.makeText(TransactionActivity.this, filename + ".pdf \n is saved to \n" + filepath, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(TransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    savepdf();
                } else {
                    Toast.makeText(TransactionActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void addTable(Document layoutDocument, List<Article> articleList) {
        PdfPTable table = new PdfPTable(new float[]{60f, 180f, 50f, 80f, 110f});

        /*// headers
        table.addCell(new Paragraph("S.N.O.").setBold());
        table.addCell(new Paragraph("PARTICULARS").setBold());
        table.addCell(new Paragraph("QTY").setBold());
        table.addCell(new Paragraph("RATE").setBold());
        table.addCell(new Paragraph("AMOUNT IN RS.").setBold());

        // items
        for(Article a : articleList)
        {
            table.addCell(new Paragraph(a.SNO+""));
            table.addCell(new Paragraph(a.description));
            table.addCell(new Paragraph(a.quantity+""));
            table.addCell(new Paragraph(a.unitPrice+""));
            table.addCell(new Paragraph((a.quantity * a.unitPrice)+""));
        }

        layoutDocument.add(table);*/
    }

    static class Article {
        int SNO;
        String description;
        int quantity;
        double unitPrice;

        public Article(int SNO, String description, int quantity, double unitPrice) {
            this.SNO = SNO;
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }
    }

    public void loaddataback() {

    }
}
