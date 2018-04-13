package com.ngocbich.gamecaro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Ngoc Bich on 4/9/2018.
 */

public class PlayingWithOther extends AppCompatActivity{
    private static final int port=5010;
    private static final String host="162.168.1.21";

    public static String rowIndex;
    public static String colIndex;
    private  Socket s=null;
    String line1=null,line2=null;

    private TextView title;
    private TextView turn;
    private ImageView imageView;
    private BoardGame boardGame;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BufferedReader is = null;
        PrintWriter os = null;

        imageView=findViewById(R.id.imageView);

        boardGame=new BoardGame(800,800,8,8,this);
        boardGame.init();

        imageView.setImageBitmap(boardGame.drawBoard(2));

        title=findViewById(R.id.tvTitle);
        title.setText("Playing with other");
        turn=findViewById(R.id.tvYourTurn);
        turn.setText("Connecting...");


        try {
         //   InetAddress address=InetAddress.getLocalHost();
            Log.d("status","attempting to connect");
            s=new Socket(host,port);

            is = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = new PrintWriter(s.getOutputStream());

            line1=is.readLine();
            if(line1=="0"){
                turn.setText("Your turn");


            }else if(line1=="1"){
                turn.setText("Enemy turn");
            }


        } catch (IOException e) {
            Log.d("debug",e.getMessage());

        }

    }
}
