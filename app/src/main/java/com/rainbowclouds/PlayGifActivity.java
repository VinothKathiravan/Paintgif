package com.rainbowclouds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PlayGifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_gif);

        //TODO set gif url
        GifImageView gifImageView = (GifImageView) findViewById(R.id.gif_Imageview);
//        gifImageView.setGifImageUri();
    }
}
