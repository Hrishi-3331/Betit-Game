package com.hrishi3331studio.betitgame.NewGames.GameTable;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hrishi3331studio.betitgame.General.MainActivity;
import com.hrishi3331studio.betitgame.R;
import com.squareup.picasso.Picasso;

public class GameTable extends AppCompatActivity {
    private String id;
    private DatabaseReference mRef;
    private ImageView TableImage;
    private TextView TableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_table);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        TableImage = (ImageView)findViewById(R.id.table_image);
        TableName = (TextView)findViewById(R.id.table_game_name);
        mRef = FirebaseDatabase.getInstance().getReference().child("games").child(id);

        mRef.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    try {
                        Picasso.get().load(dataSnapshot.getValue().toString()).into(TableImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    TableName.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void JoinTable(View view){
        Intent intent = new Intent(GameTable.this, JoinTable.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void CreateTable(View view){
        Intent intent = new Intent(GameTable.this, CreateTable.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
