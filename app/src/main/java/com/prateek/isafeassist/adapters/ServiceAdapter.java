package com.prateek.isafeassist.adapters;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prateek.isafeassist.R;
import com.prateek.isafeassist.maps.ServiceMapActivity;
import com.prateek.isafeassist.maps.TowingMapActivity;
import com.prateek.isafeassist.model.service;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context context;
    private List servicelist;

    public ServiceAdapter(Context context, List servicelist) {
        this.context = context;
        this.servicelist = servicelist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.service_adapter, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final service serve = (service) servicelist.get(i);
        myViewHolder.callout.setText(serve.getServicename());
        myViewHolder.price.setText(serve.getPrice());
        myViewHolder.servicecall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.getAdapterPosition() == 0) {


                    context.startActivity(new Intent(context, ServiceMapActivity.class));
                } else if (myViewHolder.getAdapterPosition() == 1) {
                    context.startActivity(new Intent(context, TowingMapActivity.class));


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView callout, price;
        public CardView c1;
        public Button servicecall;


        public MyViewHolder(View view) {
            super(view);

            c1 = view.findViewById(R.id.service_card);
            callout = view.findViewById(R.id.carr_out_service);
            price = view.findViewById(R.id.service_price);
            servicecall = view.findViewById(R.id.book_callservice);
            //c2= view.findViewById(R.id.service_card2);


        }
    }
}
