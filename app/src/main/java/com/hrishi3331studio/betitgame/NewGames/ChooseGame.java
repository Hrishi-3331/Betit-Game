package com.hrishi3331studio.betitgame.NewGames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hrishi3331studio.betitgame.R;

public class ChooseGame extends AppCompatActivity {

    private DatabaseReference mRef;
    private RecyclerView GamesView;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        GamesView = (RecyclerView)findViewById(R.id.games_view);
        manager = new GridLayoutManager(ChooseGame.this, 2);
        GamesView.setLayoutManager(manager);
        mRef = FirebaseDatabase.getInstance().getReference().child("games");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Game, GameViewHolder> adapter = new FirebaseRecyclerAdapter<Game, GameViewHolder>(Game.class, R.layout.game_card_layout, GameViewHolder.class, mRef) {
            @Override
            protected void populateViewHolder(GameViewHolder viewHolder, Game model, int position) {
                viewHolder.setView(model.getImage(), model.getName());
                viewHolder.setOnclickListner(ChooseGame.this, getRef(position).getKey());
            }
        };

        GamesView.setAdapter(adapter);
    }

    public void goBack(View view){
        finish();
    }

}
