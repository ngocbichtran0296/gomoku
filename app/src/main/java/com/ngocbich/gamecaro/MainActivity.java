package com.ngocbich.gamecaro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        Button playWithOther=findViewById(R.id.btPlayingWithOther);
        Button playWithComputer=findViewById(R.id.btPlayingWithComputer);

        playWithComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PlayingWithComputer.class);
                startActivity(intent);
            }
        });

        playWithOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,PlayingWithOther.class);
                startActivity(intent);
            }
        });
    }
}
