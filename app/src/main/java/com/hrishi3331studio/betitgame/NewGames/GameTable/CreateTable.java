package com.hrishi3331studio.betitgame.NewGames.GameTable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrishi3331studio.betitgame.R;

public class CreateTable extends AppCompatActivity {

    private TextView Players;
    private TextView Coins;
    private String game_code;
    private String game_id;
    private String table_id;
    private FirebaseAuth jAuth;
    private FirebaseUser jUser;
    private ProgressDialog jDialog;
    private CoordinatorLayout cord;
    private DatabaseReference mRef;
    int max_players;
    int min_coins;
    private int coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);

        Players = (TextView)findViewById(R.id.players_number);
        Coins = (TextView) findViewById(R.id.coins_number);
        ImageButton coinsadd = (ImageButton) findViewById(R.id.coin_plus);
        ImageButton coinsminus = (ImageButton) findViewById(R.id.coin_minus);
        ImageButton playersadd = (ImageButton) findViewById(R.id.players_plus);
        ImageButton playersminus = (ImageButton) findViewById(R.id.players_minus);
        cord = (CoordinatorLayout)findViewById(R.id.cord1);

        jDialog = new ProgressDialog(CreateTable.this);
        jDialog.setTitle("Creating Table");
        jDialog.setMessage("Please wait");
        jDialog.setCanceledOnTouchOutside(false);
        jDialog.setCancelable(false);

        Intent intent = getIntent();
        game_id = intent.getStringExtra("id");

        jAuth = FirebaseAuth.getInstance();
        jUser = jAuth.getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("games").child(game_id);
        getParameters();

        FirebaseDatabase.getInstance().getReference().child("users").child(jUser.getUid()).child("coins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    coins = Integer.valueOf(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        playersadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jag = Integer.valueOf(Players.getText().toString());
                if (jag < max_players){
                    Players.setText(String.valueOf(jag + 1));
                }
            }
        });

        playersminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jag = Integer.valueOf(Players.getText().toString());
                if (jag > 2){
                    Players.setText(String.valueOf(jag - 1));
                }
            }
        });

        coinsadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jag = Integer.valueOf(Coins.getText().toString());
                Coins.setText(String.valueOf(jag + 10));

            }
        });

        coinsminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jag = Integer.valueOf(Coins.getText().toString());
                if (jag > min_coins){
                    Coins.setText(String.valueOf(jag - 10));
                }
            }
        });
    }

    public void getParameters() {
        mRef.child("game_code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    game_code = dataSnapshot.getValue().toString();
                    switch (game_code){
                        case "pubg":
                            max_players = 4;
                            min_coins = 100;
                            break;

                        case "lk":
                            max_players = 4;
                            min_coins = 100;
                            break;

                        case "pool":
                            max_players = 2;
                            min_coins = 100;
                            break;

                        case "pt":
                            max_players = 2;
                            min_coins = 100;
                            break;

                        case "uno":
                            max_players = 4;
                            min_coins = 100;
                            break;

                        case "tp":
                            max_players = 5;
                            min_coins = 100;
                            break;

                        case "rento":
                            max_players = 4;
                            min_coins = 100;
                            break;

                        case "housie":
                            max_players = 4;
                            min_coins = 100;
                            break;

                        case "cod":
                            max_players = 4;
                            min_coins = 100;
                            break;
                    }
                    Players.setText(String.valueOf(2));
                    Coins.setText(String.valueOf(min_coins));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
