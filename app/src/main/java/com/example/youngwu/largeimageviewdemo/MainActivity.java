package com.example.youngwu.largeimageviewdemo;

import android.app.Activity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LargeImageView largeImageView= (LargeImageView) findViewById(R.id.img_large);
        try {
            largeImageView.setInputstream(getAssets().open("qm.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
