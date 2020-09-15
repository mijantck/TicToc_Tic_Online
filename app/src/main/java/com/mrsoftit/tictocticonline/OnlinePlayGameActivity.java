package com.mrsoftit.tictocticonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrsoftit.tictocticonline.note.UsersNote;
import com.squareup.picasso.Picasso;

import soup.neumorphism.NeumorphButton;

public class OnlinePlayGameActivity extends AppCompatActivity implements View.OnClickListener {

    NeumorphButton btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
    TextView headerText;

    int PLAYER_O = 0;
    int PLAYER_X = 1;

    int activePlayer = PLAYER_O;

    int[] filledPos = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
    int count = 9;
    int winChacklastNumber ;


    boolean isGameActive = true;

    FirebaseAuth auth;
    FirebaseFirestore db;



    String currentGameID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play_game);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        headerText = findViewById(R.id.playerturn);
        headerText.setText("O turn");
        winChacklastNumber = 0;


        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        // logic for button press

        if(!isGameActive)
            return;

        Button clickedBtn = findViewById(view.getId());

        int clickedTag = Integer.parseInt(view.getTag().toString());

        if(filledPos[clickedTag] != -1){
            return;
        }

        filledPos[clickedTag] = activePlayer;

        if(activePlayer == PLAYER_O){

            clickedBtn.setText("O");
            clickedBtn.setBackground(getDrawable(android.R.color.holo_blue_bright));
            activePlayer = PLAYER_X;
            headerText.setText("X turn");

        }else{
            clickedBtn.setText("X");
            clickedBtn.setBackground(getDrawable(android.R.color.holo_orange_light));
            activePlayer = PLAYER_O;
            headerText.setText("O turn");
        }


        count -= 1;
        checkForWin();
    }

    private void checkForWin(){
        //we will check who is winner and show
        int[][] winningPos = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};


        for(int i =0 ;i<8;i++){
            int val0  = winningPos[i][0];
            int val1  = winningPos[i][1];
            int val2  = winningPos[i][2];

            if(filledPos[val0] == filledPos[val1] && filledPos[val1] == filledPos[val2]){
                if(filledPos[val0] != -1){
                    //winner declare
                    isGameActive = false;
                    if(filledPos[val0] == PLAYER_O) {
                        count = 9;
                        restartGame();
                        showDialog("O is winner");
                        Toast.makeText(this, count+"", Toast.LENGTH_SHORT).show();

                    }
                    else if (filledPos[val0] == PLAYER_X){

                        count = 9;
                        restartGame();
                        showDialog("X is winner");
                        Toast.makeText(this, count+"", Toast.LENGTH_SHORT).show();
                    }
                }
            }

           else if (count == 0 ){
                Toast.makeText(this, count+"", Toast.LENGTH_SHORT).show();

                count = 9;
                restartGame();
                showDialog("draw");

            }
        }
    }

    private void showDialog(String winnerText){
        new AlertDialog.Builder(this)
                .setTitle(winnerText)
                .setCancelable(false)
                .setPositiveButton("Restart game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count = 9;

                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void restartGame(){
        activePlayer = PLAYER_O;
        headerText.setText("O turn");
        filledPos = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1};
        btn0.setText("");
        btn1.setText("");
        btn2.setText("");
        btn3.setText("");
        btn4.setText("");
        btn5.setText("");
        btn6.setText("");
        btn7.setText("");
        btn8.setText("");

        btn0.setBackground(getDrawable(android.R.color.darker_gray));
        btn1.setBackground(getDrawable(android.R.color.darker_gray));
        btn2.setBackground(getDrawable(android.R.color.darker_gray));
        btn3.setBackground(getDrawable(android.R.color.darker_gray));
        btn4.setBackground(getDrawable(android.R.color.darker_gray));
        btn5.setBackground(getDrawable(android.R.color.darker_gray));
        btn6.setBackground(getDrawable(android.R.color.darker_gray));
        btn7.setBackground(getDrawable(android.R.color.darker_gray));
        btn8.setBackground(getDrawable(android.R.color.darker_gray));
        isGameActive = true;
    }




    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.update("status","playing","currentGameID",currentGameID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.update("status","playing","currentGameID",currentGameID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.update("status","offline")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                    }
                });
    }

}
