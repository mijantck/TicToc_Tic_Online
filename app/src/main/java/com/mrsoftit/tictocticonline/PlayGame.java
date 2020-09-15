package com.mrsoftit.tictocticonline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;


import com.gauravbhola.ripplepulsebackground.RipplePulseLayout;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class PlayGame extends AppCompatActivity {

    int turn = 1;
    int win = 0;
    int gamov = 0;
    int flagEndGame=0;
    int flag;
    String displayTurn;
    GridLayout grid;
    Button playBoard[][] = new Button[3][3];
    TextView playerTurn;
    String player1Name = " player 1";
    String player2Name = " player 2";
    String numberText;
    int number = 9999;
    int counter = 0;
    int player1Win = 0, player2Win = 0, draw = 0;
    int flipValue=0;
    AlertDialog.Builder builder,bulder1;

    TextView play1,drwo,play2;

    RipplePulseLayout layout_ripplepulse,layout_ripplepulseP;

    int ripleWave = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        playerTurn = (TextView) findViewById(R.id.player);
        builder = new AlertDialog.Builder(this);
        bulder1 = new AlertDialog.Builder(this);
        Intent intent = getIntent();
       // player1Name = intent.getExtras().getString("Player 1");
       // player2Name = intent.getExtras().getString("Player 2");
      //  numberText = intent.getExtras().getString("Number");
      //  number = Integer.parseInt(numberText);
        grid = (GridLayout) findViewById(R.id.grid);
        displayTurn=player1Name + "'s turn (X)";
        playerTurn.setText(displayTurn);

        play1 = findViewById(R.id.Tplayer1Win);
        drwo = findViewById(R.id.Tdow1Win);
        play2 = findViewById(R.id.Tplay2Win);

        layout_ripplepulse = findViewById(R.id.layout_ripplepulse);
        layout_ripplepulseP = findViewById(R.id.layout_ripplepulseP);

        layout_ripplepulseP.startRippleAnimation();





        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playBoard[i][j] = (Button) grid.getChildAt(3 * i + j);
            }
        }

    }

    public void playmove(View view) {
        int index = grid.indexOfChild(view);
        int i = index / 3;
        int j = index % 3;

        if (turn == 1){

            if(flipValue==0){

                layout_ripplepulseP.stopRippleAnimation();
                layout_ripplepulse.startRippleAnimation();

            }
            else if(flipValue == 1){
                layout_ripplepulse.stopRippleAnimation();
                layout_ripplepulseP.startRippleAnimation();
            }


            ripleWave = 1;

        }else if (turn == 2){

            if(flipValue==0){

                layout_ripplepulse.stopRippleAnimation();
                layout_ripplepulseP.startRippleAnimation();
            }
            else if(flipValue==1){

                layout_ripplepulseP.stopRippleAnimation();
                layout_ripplepulse.startRippleAnimation();

            }



            ripleWave = 0;
        }
        flag = 0;
        if (turn == 1 && gamov == 0 && !(playBoard[i][j].getText().toString().equals("X")) && !(playBoard[i][j].getText().toString().equals("O"))) {


            if(flipValue==0){
                displayTurn=player2Name + "'s turn (O)";
                playerTurn.setText(displayTurn);

            }
            else if(flipValue==1){
                displayTurn=player1Name + "'s turn (O)";
                playerTurn.setText(displayTurn);

            }

            playBoard[i][j].setText("X");
            turn = 2;

        } else if (turn == 2 && gamov == 0 && !(playBoard[i][j].getText().toString().equals("X")) && !(playBoard[i][j].getText().toString().equals("O"))) {
            if(flipValue==0){
                displayTurn=player1Name + "'s turn (X)";
                playerTurn.setText(displayTurn);
            }
            else if(flipValue==1){
                displayTurn=player2Name + "'s turn (X)";
                playerTurn.setText(displayTurn);

            }
            playBoard[i][j].setText("O");
            turn = 1;

        }
        checkWin();
        if (gamov == 1) {
            if (win == 1) {
                builder.setMessage(player1Name + " wins!").setTitle("Game over");
                if(flagEndGame==0){
                    player1Win++;
                    counter++;
                    play1.setText(player1Win+"");
                }


            } else if (win == 2) {
                builder.setMessage(player2Name + " wins!").setTitle("Game over");
                if(flagEndGame==0){
                    player2Win++;
                    counter++;
                    play2.setText(player2Win+"");
                }

            }
            flagEndGame=1;
            builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    newGame();
                    if (counter == number) {
                        Intent intent = new Intent(getApplicationContext(), SeriesResult.class);
                        intent.putExtra("Player 1 Wins", player1Win);
                        intent.putExtra("Player 2 Wins", player2Win);
                        intent.putExtra("Draws", draw);
                        intent.putExtra("Player 1 Name", player1Name);
                        intent.putExtra("Player 2 Name", player2Name);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                            finish();
                        }

                    }
                }

            });
            AlertDialog dialog = builder.create();
            dialog.show();



        }
        if (gamov == 0) {
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 3; j++) {
                    if (!playBoard[i][j].getText().toString().equals("X") && !playBoard[i][j].getText().toString().equals("O")) {
                        flag = 1;
                        break;

                    }
                }
            }
            if (flag == 0) {
                builder.setMessage("It's a draw!").setTitle("Game over");
                if(flagEndGame==0){
                    counter++;
                    draw++;
                    drwo.setText(draw+"");

                }
                flagEndGame=1;
                builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        newGame();
                        if (counter == number) {
                            Intent intent = new Intent(getApplicationContext(), SeriesResult.class);
                            intent.putExtra("Player 1 Wins", player1Win);
                            intent.putExtra("Player 2 Wins", player2Win);
                            intent.putExtra("Draws", draw);
                            intent.putExtra("Player 1 Name", player1Name);
                            intent.putExtra("Player 2 Name", player2Name);
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                                finish();
                            }

                        }
                    }

                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }


        }


    }

    public void newGame() {

        win = 0;
        gamov = 0;
        turn=1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                playBoard[i][j].setText(" ");
                playBoard[i][j].setTextColor(Color.BLACK);
            }
        }

        if(flipValue==0){
            if(flagEndGame==1){
                flipValue=1;
                displayTurn=player2Name + "'s turn (X)";
                playerTurn.setText(displayTurn);
            }
            else{
                displayTurn=player1Name + "'s turn (X)";
                playerTurn.setText(displayTurn);
            }


        }
        else if(flipValue==1 ){
            if(flagEndGame==1){
                flipValue=0;
                displayTurn=player1Name + "'s turn (X)";
                playerTurn.setText(displayTurn);
            }
            else{
                displayTurn=player2Name + "'s turn (X)";
                playerTurn.setText(displayTurn);
            }



        }
        flagEndGame=0;
    }

    public void checkWin() {
        for (int i = 0; i < 3; i++) {
            if (playBoard[i][0].getText().toString().equals(playBoard[i][1].getText().toString()) && playBoard[i][0].getText().toString().equals(playBoard[i][2].getText().toString())) {
                if (playBoard[i][0].getText().toString().equals("X")) {
                    gamov = 1;
                    if(flipValue==0)
                        win = 1;
                    else if(flipValue==1)
                        win=2;


                } else if (playBoard[i][0].getText().toString().equals("O")) {
                    gamov = 1;
                    if(flipValue==0)
                        win = 2;
                    else if(flipValue==1)
                        win=1;

                }
                if (!playBoard[i][0].getText().toString().equals(" ")) {
                    playBoard[i][0].setTextColor(Color.RED);
                    playBoard[i][1].setTextColor(Color.RED);
                    playBoard[i][2].setTextColor(Color.RED);

                }

            }
            if (playBoard[0][i].getText().toString().equals(playBoard[1][i].getText().toString()) && playBoard[0][i].getText().toString().equals(playBoard[2][i].getText().toString())) {
                if (playBoard[0][i].getText().toString().equals("X")) {
                    gamov = 1;
                    if(flipValue==0)
                        win = 1;
                    else if(flipValue==1)
                        win=2;


                } else if (playBoard[0][i].getText().toString().equals("O")) {
                    gamov = 1;
                    if(flipValue==0)
                        win = 2;
                    else if(flipValue==1)
                        win=1;

                }
                if (!playBoard[0][i].getText().toString().equals(" ")) {
                    playBoard[0][i].setTextColor(Color.RED);
                    playBoard[1][i].setTextColor(Color.RED);
                    playBoard[2][i].setTextColor(Color.RED);
                }

            }


        }
        if (playBoard[0][0].getText().toString().equals(playBoard[1][1].getText().toString()) && playBoard[0][0].getText().toString().equals(playBoard[2][2].getText().toString())) {
            if (playBoard[0][0].getText().toString().equals("X")) {
                gamov = 1;
                if(flipValue==0)
                    win = 1;
                else if(flipValue==1)
                    win=2;


            } else if (playBoard[0][0].getText().toString().equals("O")) {
                gamov = 1;
                if(flipValue==0)
                    win = 2;
                else if(flipValue==1)
                    win=1;

            }
            if (!playBoard[0][0].getText().toString().equals(" ")) {
                playBoard[0][0].setTextColor(Color.RED);
                playBoard[1][1].setTextColor(Color.RED);
                playBoard[2][2].setTextColor(Color.RED);
            }


        }
        if (playBoard[0][2].getText().toString().equals(playBoard[1][1].getText().toString()) && playBoard[0][2].getText().toString().equals(playBoard[2][0].getText().toString())) {
            if (playBoard[0][2].getText().toString().equals("X")) {
                gamov = 1;
                if(flipValue==0)
                    win = 1;
                else if(flipValue==1)
                    win=2;


            } else if (playBoard[0][2].getText().toString().equals("O")) {
                gamov = 1;
                if(flipValue==0)
                    win = 2;
                else if(flipValue==1)
                    win=1;

            }
            if (!playBoard[2][0].getText().toString().equals(" ")) {
                playBoard[2][0].setTextColor(Color.RED);
                playBoard[1][1].setTextColor(Color.RED);
                playBoard[0][2].setTextColor(Color.RED);
            }


        }
    }
    public void newMatch(View view){

        bulder1.setMessage("Are you sure cancel game ");
        bulder1.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                newGame();

            }
        });
        bulder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        bulder1.create();
        bulder1.show();

      /*  Intent intent = new Intent(this,PlayerNameWithComputer.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
            finish();
        }*/
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PlayGame.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
