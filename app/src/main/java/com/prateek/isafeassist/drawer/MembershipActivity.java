package com.prateek.isafeassist.drawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.prateek.isafeassist.R;
import com.prateek.isafeassist.adapters.MembershipAdapter;
import com.prateek.isafeassist.model.membermodel;

import java.util.ArrayList;
import java.util.List;

public class MembershipActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MembershipAdapter adapter;
    private List<membermodel> membermodels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        recyclerView= findViewById(R.id.membership_recycler);

/*        adapter= new MembershipAdapter(membermodels);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);*/
    }
}
