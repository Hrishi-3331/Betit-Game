package com.hrishi3331studio.betitgame.General;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrishi3331studio.betitgame.Dialogs.LoaderDialog;
import com.hrishi3331studio.betitgame.NewGames.ChooseGame;
import com.hrishi3331studio.betitgame.Notifications.Notifications;
import com.hrishi3331studio.betitgame.R;
import com.hrishi3331studio.betitgame.Support.ContactUs;
import com.hrishi3331studio.betitgame.Support.Support;
import com.hrishi3331studio.betitgame.User.Profile;
import com.hrishi3331studio.betitgame.UserAuth.SignUp;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mToggle;
    private LoaderDialog dialog;
    private TextView username;
    private TextView usercoins;
    private TextView played_games;
    private TextView won_games;
    private TextView pending_games;
    private RoundedImageView avtar;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My App");
        mDrawer = (DrawerLayout) findViewById(R.id.mDrawer);
        mNavigation = (NavigationView) findViewById(R.id.mNavigation);
        mToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawer, R.string.open, R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        username = (TextView)findViewById(R.id.username_dashboard);
        usercoins = (TextView)findViewById(R.id.user_coins_dashboard);
        played_games = (TextView)findViewById(R.id.dashboard_games_played);
        won_games = (TextView)findViewById(R.id.dashboard_games_won);
        pending_games = (TextView)findViewById(R.id.dashboard_games_pending);
        avtar = (RoundedImageView)findViewById(R.id.user_image_dashboard);

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("users").child(mUser.getUid());

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(MainActivity.this, SignUp.class));
                    finish();
                }
            }
        });

        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.contact:
                        startActivity(new Intent(MainActivity.this, ContactUs.class));
                        break;

                    case R.id.support:
                        startActivity(new Intent(MainActivity.this, Support.class));
                        break;

                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, Profile.class));
                        break;

                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MainActivity.this, SignUp.class));
                        finish();
                        break;

                    default:
                        break;
                }
                mDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });

        View header = mNavigation.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.user_name);
        TextView email = (TextView) header.findViewById(R.id.header_email);
        RoundedImageView image = (RoundedImageView)header.findViewById(R.id.user_header_image);

        if (mUser != null) {
            email.setText(mUser.getEmail());
            name.setText(mUser.getDisplayName());
            try{
                Picasso.get().load(mUser.getPhotoUrl()).into(image);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        dialog = new LoaderDialog(MainActivity.this);
        checkConnectivity();

    }

    private void checkConnectivity() {
        if(!isNetworkAvailable(MainActivity.this)) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View DialogLayout = inflater.inflate(R.layout.connection_dialog, null);
            builder.setView(DialogLayout);

            Button ok = (Button) DialogLayout.findViewById(R.id.btn_ok);
            Button cancel = (Button) DialogLayout.findViewById(R.id.btn_cancel);

            final android.app.AlertDialog exit_dialog = builder.create();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            exit_dialog.show();
        }
        else {
            dialog.showLoader();
            updateDashboard();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View DialogLayout = inflater.inflate(R.layout.exit_dialog, null);
        builder.setView(DialogLayout);

        Button ok = (Button) DialogLayout.findViewById(R.id.btn_ok);
        Button cancel = (Button) DialogLayout.findViewById(R.id.btn_cancel);

        final android.app.AlertDialog exit_dialog = builder.create();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_dialog.dismiss();
            }
        });

        exit_dialog.show();
    }

    public void updateDashboard(){
        mRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    username.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    usercoins.setText(dataSnapshot.getValue().toString());
                    dialog.dismissLoader();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("games_played").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    played_games.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("games_won").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    won_games.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("games_pending").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    pending_games.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("photourl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    try{
                        Picasso.get().load(dataSnapshot.getValue().toString()).into(avtar);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        if (id == R.id.notifications){
            startActivity(new Intent(MainActivity.this, Notifications.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_event_details, menu);
        return true;
    }

    public void PlayNow(View view){
        startActivity(new Intent(MainActivity.this, ChooseGame.class));
    }
}