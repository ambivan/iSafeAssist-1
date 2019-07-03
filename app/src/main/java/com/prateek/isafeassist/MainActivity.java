package com.prateek.isafeassist;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prateek.isafeassist.fragments.BikesFragment;
import com.prateek.isafeassist.fragments.BookedServiceFragment;
import com.prateek.isafeassist.fragments.CarFragment;
import com.prateek.isafeassist.fragments.DefaultFragment;
import com.prateek.isafeassist.fragments.LandingFragment;
import com.prateek.isafeassist.fragments.OrderHistoryFragment;
import com.prateek.isafeassist.fragments.PaymentFragment;
import com.prateek.isafeassist.fragments.ServicesFragment;
import com.prateek.isafeassist.fragments.UserMembershipFragment;
import com.prateek.isafeassist.fragments.UserProfileFragment;
import com.prateek.isafeassist.welcome.SplashActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button bike_knowmore, car_knowmore;
    ImageButton backbtn;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView textView;
    View header;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*        Toolbar backtoolbar = findViewById(R.id.toolbar_back);
        setSupportActionBar(backtoolbar);
        backtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);*/
        bike_knowmore = findViewById(R.id.knowmore_bike_btn);
        //backbtn = findViewById(R.id.backbutton);
        car_knowmore = findViewById(R.id.know_more_car_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseAuth.getCurrentUser().getUid());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView= findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                //textView.setText(name);
                header= navigationView.getHeaderView(0);
                textView = header.findViewById(R.id.header_name);
                textView.setText(name);
                System.out.println(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_package) {

            loadFragment(new DefaultFragment());
            // Handle the camera action
        } else if (id == R.id.navig_home) {
            loadFragment(new LandingFragment());

        } else if (id == R.id.nav_orderhist) {
            loadFragment(new OrderHistoryFragment());

        } else if (id == R.id.nav_membership) {
            loadFragment(new UserMembershipFragment());

        } else if (id == R.id.nav_bookedservice) {
            loadFragment(new BookedServiceFragment());

        } else if (id == R.id.nav_logout) {
            alertbuilder();
        } else if (id == R.id.profile_section) {
            loadFragment(new UserProfileFragment());

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit(); // save the changes
    }

    private void alertbuilder() {
        new AlertDialog.Builder(this)
                .setTitle("LogOut")
                .setMessage("Are you sure you want to LogOut?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
