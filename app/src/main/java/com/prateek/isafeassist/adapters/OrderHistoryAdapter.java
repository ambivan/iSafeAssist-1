package com.prateek.isafeassist.adapters;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.model.membermodel;
import com.prateek.isafeassist.payments.PaymentActivity;
import com.prateek.isafeassist.payments.TransactionActivity;
import com.prateek.isafeassist.payments.paymentdao.PayDao;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {

    private List<membermodel> list;
    Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(auth.getCurrentUser().getUid());

    public OrderHistoryAdapter(Context context, List<membermodel> list) {

        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.historybinder, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {

        final membermodel model = list.get(i);
        holder.purchase.setText(model.getPurchased());
        holder.exp.setText(model.getExpiry());
        holder.tid.setText(model.getAvailed());
        holder.myid.setText(model.getMid());
        if (model.getAvailed().equals("₹283.20/-")) {
            holder.mtype.setText("Bike Membership");
        } else {
            holder.mtype.setText("Car Membership");
        }

        holder.downloadrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = model.getMid();

                String type = holder.mtype.toString();
                getpdf(id, type);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView purchase, exp, tid, mtype, myid;
        Button downloadrec;

        public MyViewHolder(View view) {
            super(view);
            purchase = view.findViewById(R.id.hist_purchasedon_text);
            exp = view.findViewById(R.id.hist_expiryon_text);
            mtype = view.findViewById(R.id.hist_typeofmembership);
            tid = view.findViewById(R.id.hist_availed_text);
            myid = view.findViewById(R.id.hist_trasc_text);
            downloadrec = view.findViewById(R.id.downrecipt);
        }
    }

    private void getpdf(final String id, String type) {

        if (type.equals("Car Membership")) {
            databaseReference.child("Car Package").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String trid = "";
                    final Document document = new Document();
                    final String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    final String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

                    String name, address, contact, vehmake, vehmodel, year, regno, memid, start, end;


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        System.out.println("CKey" + ds.getKey());
                        System.out.println(id);
                        trid = ds.child("Payments").child("transactid").getValue(String.class);

                        if(trid.equals(id)){

                            name = ds.child("firstname").getValue(String.class);
                            address = ds.child("address").getValue(String.class);
                            contact = ds.child("mobno").getValue(String.class);
                            vehmake = ds.child("carmake").getValue(String.class);
                            vehmodel = ds.child("carmodel").getValue(String.class);
                            year = ds.child("year").getValue(String.class);
                            regno = ds.child("regno").getValue(String.class);
                            memid = ds.child("Payments").child("membershipid").getValue(String.class);
                            start = ds.child("Payments").child("purchasedate").getValue(String.class);
                            end = ds.child("Payments").child("expirydate").getValue(String.class);
                            System.out.println("Jackpot");
                            System.out.println(contact + " " + address + " " + name);


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
                                document.add(new Paragraph(" Thank you for choosing our 24x7 Roadside Assistance (RSA) membership. This certificate is valid " +
                                        "for the period mentioned below and your membership will get activated after 48 hours from the time of enrollment. Please" +
                                        " read the Terms and Conditions to understand the services and we recommend you to retain a copy of same for your vehicle" +
                                        " for easy reference. ", allfo));
                                document.add(new Paragraph("Customer Details:", boldFont));
                                document.add(new Paragraph(" Name: " + name, allfo));
                                document.add(new Paragraph(" Address: " + address, allfo));
                                document.add(new Paragraph(" Contact No: " + contact, allfo));
                                document.add(new Paragraph(" Vehicle Details: \n ", boldFont));
                                PdfPTable table = new PdfPTable(5);
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
                                    table.addCell("Car");
                                    table.addCell(vehmake);
                                    table.addCell(vehmodel);
                                    table.addCell(year);
                                    table.addCell(regno);
                                }

                                document.add(table);

                                document.add(new Paragraph("Product, Membership details and Price: \n ", boldFont));

                                PdfPTable table2 = new PdfPTable(7);
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
                                    table2.addCell(memid);
                                    table2.addCell(start);
                                    table2.addCell(end);
                                    table2.addCell("720/-");
                                    table2.addCell("129.60/-");
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
                                Toast.makeText(context, filename + ".pdf \n is saved to the device\n" , Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

            databaseReference.child("Bike Package").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String trid = "";
                    final Document document = new Document();
                    final String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
                    final String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";

                    String name, address, contact, vehmake, vehmodel, year, regno, memid, start, end;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //System.out.println("BKey"+ds.getKey());
                        System.out.println(id);


                        trid = ds.child("Payments").child("transactid").getValue(String.class);
                        System.out.println(trid);
                        if (trid.equals(id)) {

                            name = ds.child("firstname").getValue(String.class);
                            address = ds.child("address").getValue(String.class);
                            contact = ds.child("mobno").getValue(String.class);
                            vehmake = ds.child("bikemake").getValue(String.class);
                            vehmodel = ds.child("bikemodel").getValue(String.class);
                            year = ds.child("year").getValue(String.class);
                            regno = ds.child("regno").getValue(String.class);
                            memid = ds.child("Payments").child("membershipid").getValue(String.class);
                            start = ds.child("Payments").child("purchasedate").getValue(String.class);
                            end = ds.child("Payments").child("expirydate").getValue(String.class);
                            System.out.println("Jackpot");
                            System.out.println(contact + " " + address + " " + name);
                           // Toast.makeText(context, contact + " " + address + " " + name, Toast.LENGTH_SHORT).show();

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
                                    table.addCell("Bike/Scooter");
                                    table.addCell(vehmake);
                                    table.addCell(vehmodel);
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
                                    table2.addCell(memid);
                                    table2.addCell(start);
                                    table2.addCell(end);
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
                                Toast.makeText(context, filename + ".pdf \n is saved to your device\n" , Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            System.out.println("Boooo");

                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


    }
}
