package com.ngocbich.gamecaro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.ExecutionException;

/**
 * Created by Ngoc Bich on 4/7/2018.
 */

public class PlayingWithComputer extends AppCompatActivity {
    private ImageView imageView;
    private BoardGame boardGame;

    private int width=800;
    private int height=800;
    private int col=8;
    private int row=8;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        boardGame=new BoardGame(width,height,col,row,this);
        boardGame.init();
        imageView.setImageBitmap(boardGame.drawBoard(2));

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    return boardGame.onTouch(v,event);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }
}
