package com.hrishi3331studio.betitgame.NewGames;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.hrishi3331studio.betitgame.R;
import com.squareup.picasso.Picasso;

public class GameViewHolder extends RecyclerView.ViewHolder{
    TextView GameName;
    ImageView GameImage;
    View mView;

    public GameViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        GameName = (TextView)mView.findViewById(R.id.game_card_title);
        GameImage = (ImageView)mView.findViewById(R.id.game_card_image);
    }

    public void setView(String image, String name){
        GameName.setText(name);
        try{
            Picasso.get().load(image).into(GameImage);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
