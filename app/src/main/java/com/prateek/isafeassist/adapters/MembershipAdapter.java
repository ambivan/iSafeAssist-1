package com.prateek.isafeassist.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prateek.isafeassist.R;
import com.prateek.isafeassist.fragments.UserMembershipFragment;
import com.prateek.isafeassist.model.membermodel;

import java.util.List;

public class MembershipAdapter extends RecyclerView.Adapter<MembershipAdapter.MyViewHolder> {

    private List<membermodel> list;
    Context context;

    public MembershipAdapter(Context context, List<membermodel> list) {
        this.list = list;
        this.context= context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.membership_viewer, viewGroup, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        membermodel model= list.get(i);
        holder.purchase.setText(model.getPurchased());
        holder.exp.setText(model.getExpiry());
        holder.tid.setText(model.getAvailed());
        holder.myid.setText(model.getMid());
        if(model.getAvailed().equals("₹ 240/-")){
            holder.mtype.setText("Bike Membership");
        }else /*if(UserMembershipFragment.ckey == 0)*/{
            holder.mtype.setText("Car Membership");
        }
        //holder.tid.setText(model.getAvailed());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView purchase, exp, tid, mtype, myid;

        public MyViewHolder(View view) {
            super(view);
            purchase= view.findViewById(R.id.purchasedon_text);
            exp= view.findViewById(R.id.expiryon_text);
            mtype= view.findViewById(R.id.typeofmembership);
            tid= view.findViewById(R.id.availed_text);
            myid= view.findViewById(R.id.trasc_text);
        }
    }
}
