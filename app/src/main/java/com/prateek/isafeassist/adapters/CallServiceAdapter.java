package com.prateek.isafeassist.adapters;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
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
import com.prateek.isafeassist.R;
import com.prateek.isafeassist.model.servicemodel;

import java.util.HashMap;
import java.util.List;

public class CallServiceAdapter extends RecyclerView.Adapter<CallServiceAdapter.MyViewHolder> {

    Context context;
    List<servicemodel> list;
    FirebaseAuth auth;

    public CallServiceAdapter(Context context, List<servicemodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.serviceviewer, viewGroup, false);

        auth = FirebaseAuth.getInstance();

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {

        final servicemodel model = list.get(i);
        holder.t1.setText(model.getDname());
        holder.t2.setText(model.getDcontact());
        if (model.getPrice().equals("₹ 1200/-")) {
            holder.t3.setText(model.getPrice());
        } else {
            holder.t3.setText(model.getPrice());

        }
        holder.otpview.setText(model.getDotp());
        Toast.makeText(context, "Match OTP with Driver to confirm Service once he arrives", Toast.LENGTH_SHORT).show();
        holder.endservicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("End Service")
                        .setMessage("Are you sure you want to end this service?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                list.clear();
                                final double otp;

                                //int y= (int)otp;
                                otp = Math.round(Math.random() * 100000);

                                ProgressDialog dialog1 = new ProgressDialog(context);
                                dialog1.setMessage("Please wait...");
                                dialog1.setCancelable(false);
                                dialog1.show();
                                holder.serviceway.setText(String.valueOf((int) otp));

                                notificaticationCall(String.valueOf(otp));
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                if (model.getPrice().equals(" ₹ 1200/-")) {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("endrequest", "1");
                                    hashMap.put("endotp", (String.valueOf((int) otp)));
                                    reference.child("Towing Requests").child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                                    dialog1.dismiss();

                                } else {
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("endrequest", "1");
                                    hashMap.put("endotp", (String.valueOf((int) otp)));
                                    //holder.serviceway.setText(String.valueOf((int) otp));
                                    reference.child("Requests").child(auth.getCurrentUser().getUid()).updateChildren(hashMap);
                                    dialog1.dismiss();

                                }
                                /*reference.child("Requests").child(auth.getCurrentUser().getUid()).child("drivername").removeValue();
                                reference.child("Requests").child(auth.getCurrentUser().getUid()).child("driverphone").removeValue();
*/

                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        System.out.println(list.size());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView t1, t2, t3, otpview, serviceway;
        Button endservicebtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            t1 = itemView.findViewById(R.id.driver_text);
            t3 = itemView.findViewById(R.id.driverprice_text);
            t2 = itemView.findViewById(R.id.drivercontact_text);
            serviceway = itemView.findViewById(R.id.serviceonway);
            otpview = itemView.findViewById(R.id.otpview);
            endservicebtn = itemView.findViewById(R.id.endservicebtn);
        }
    }

    public void notificaticationCall(String req) {
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("End Service")
                .setSmallIcon(R.drawable.isafe_assist_logo)
                .setContentText("Verify this OTP with Driver to end Service " + req);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());

    }
}
