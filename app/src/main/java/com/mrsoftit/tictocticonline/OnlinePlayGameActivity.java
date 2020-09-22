package com.mrsoftit.tictocticonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.tictocticonline.note.PlayGameNote;
import com.mrsoftit.tictocticonline.note.UsersNote;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphImageView;

public class OnlinePlayGameActivity extends AppCompatActivity implements View.OnClickListener {



    FirebaseAuth auth;
    FirebaseFirestore db;

    FirebaseUser currentUser;





    NeumorphButton btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8;
    TextView headerText,Tplayer1Win,Tdow1Win,Tplay2Win;

    int PLAYER_O = 0;
    int PLAYER_X = 1;

    int activePlayer = PLAYER_O;

    int[] filledPos = {-1,-1,-1,-1,-1,-1,-1,-1,-1};
    int count = 9;
    int winChacklastNumber ;

    ProgressDialog progressDialog;
    boolean isGameActive = true;

    String currentGameID;

    String gameType,randomCode ;

    String gameID;
    String gameRandorCode;


    String playerName;
    String playerImageURL;

    String player1ID;
    String player1Name;
    int player1Wine;
    String player1ImageURL;


    String player2ID;
    String player2Name;
    int player2Wine;

    String player2ImageURL;

    CircleImageView profile_image2,profile_image1;
    int machCount;

    int drow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_play_game);


        progressDialog = new ProgressDialog(this);
        // Setting Message
        progressDialog.setMessage("Loading..."); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        headerText = findViewById(R.id.playerturn);
        profile_image1 = findViewById(R.id.profile_image1);
        profile_image2 = findViewById(R.id.profile_image2);


        Tplayer1Win = findViewById(R.id.Tplayer1Win);
        Tdow1Win = findViewById(R.id.Tdow1Win);
        Tplay2Win = findViewById(R.id.Tplay2Win);


        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);

        headerText.setText("O turn");
        winChacklastNumber = 0;


        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            gameType = bundle.getString("gameType");
            randomCode = bundle.getString("randomCode");

        }

        if (auth != null){

            String user_id = currentUser.getUid();

            CollectionReference myInfo = FirebaseFirestore.getInstance()
                    .collection("users");

            Query query =  myInfo.whereEqualTo("uID",user_id);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        String id = task.getResult().toString();

                        Toast.makeText(OnlinePlayGameActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            UsersNote usersNote = document.toObject(UsersNote.class);

                            playerName = usersNote.getName();
                            playerImageURL = usersNote.getFbProfiteImageURL();

                        }

                        Picasso.get().load(playerImageURL)
                                .into(profile_image1);

                    }

                    else {

                        Toast.makeText(OnlinePlayGameActivity.this, "not ordk", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }


        if (gameType.equals("inviting")){

           // creatinGame();

            UpdateUserInformation();

        }
    }



    private void UpdateUserInformation() {


        String uid = currentUser.getUid();

        Map<String, Object> games = new HashMap<>();

        games.put("gameID",randomCode );
        games.put("gameRandorCode",randomCode );
        games.put("activePlayer",activePlayer);
        games.put("player1ID",uid);
        games.put("player1Name",playerName);
        games.put("player1ImageURL",playerImageURL);
        games.put("player1Wine",0);

        games.put("player2ID",null);
        games.put("player2Name",null);
        games.put("player2ImageURL",null);
        games.put("player2Wine",0);

        games.put("machCount",1);
        games.put("drow",0);

        games.put("btn0",-1);
        games.put("btn1",-1);
        games.put("btn2",-1);
        games.put("btn3",-1);
        games.put("btn4",-1);
        games.put("btn5",-1);
        games.put("btn6",-1);
        games.put("btn7",-1);
        games.put("btn8",-1);


        db = FirebaseFirestore.getInstance();

        db.collection("games").document(randomCode).set(games)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        progressDialog.dismiss();
                        //  startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


    }


    @Override
    public void onClick(View view) {


        // logic for button press

        if(!isGameActive)
            return;

        Button clickedBtn = findViewById(view.getId());


        Toast.makeText(this,  clickedBtn.getTag()+"", Toast.LENGTH_SHORT).show();

        int clickedTag = Integer.parseInt(view.getTag().toString());

        if(filledPos[clickedTag] != -1){
            return;
        }


        filledPos[clickedTag] = activePlayer;

        updateDataBase(clickedTag,activePlayer);

        if(activePlayer == PLAYER_O){

            clickedBtn.setText("O");
            clickedBtn.setBackground(getDrawable(android.R.color.holo_blue_bright));
            activePlayer = PLAYER_X;
            headerText.setText("X turn");

            updateDataBase(PLAYER_X);

        }else{
            clickedBtn.setText("X");
            clickedBtn.setBackground(getDrawable(android.R.color.holo_orange_light));
            activePlayer = PLAYER_O;
            headerText.setText("O turn");
            updateDataBase(PLAYER_O);

        }


        count -= 1;
        checkForWin();


    }

    public void  updateDataBase(int buttonNumber,int activePlayerNumber){

        final DocumentReference gameRef = db.collection("games").document(randomCode);

        String fildName = "btn"+buttonNumber;
        gameRef.update(fildName,activePlayerNumber).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public void  updateDataBaseWint(){

        final DocumentReference gameRef = db.collection("games").document(randomCode);

        gameRef.update("activePlayer",0,"btn0",-1,"btn1",-1,"btn2",-1,"btn3",-1,"btn4",-1,"btn5",-1,"btn6",-1, "btn7",-1,"btn8",-1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public void  updateDataBase(int activePlayerUpdate){

        final DocumentReference gameRef = db.collection("games").document(randomCode);

        gameRef.update("activePlayer",activePlayerUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

    }

    public void  updateDataWin(int choosPlayr){

        final DocumentReference gameRef = db.collection("games").document(randomCode);


        if (choosPlayr == 1){

            int win = player1Wine +1;

            gameRef.update("player1Wine",win).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        if (choosPlayr == 2){
            int win = player2Wine +1;

            gameRef.update("player2Wine",win).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }
        if (choosPlayr == 3){

            int drow1 = drow +1;

            gameRef.update("drow",drow1).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
        }


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
                        updateDataBaseWint();
                        updateDataWin(1);

                    }
                    else if (filledPos[val0] == PLAYER_X){

                        count = 9;
                        restartGame();
                        showDialog("X is winner");
                        updateDataBaseWint();
                        updateDataWin(2);
                    }
                }
            }

           else if (count == 0 ){
                Toast.makeText(this, count+"", Toast.LENGTH_SHORT).show();

                count = 9;
                restartGame();
                showDialog("draw");
                updateDataBaseWint();

                updateDataWin(3);

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

        final DocumentReference gameRef = db.collection("games").document(randomCode);


        docRef.update("status","playing","currentGameID",currentGameID)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

        gameRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(OnlinePlayGameActivity.this, e+"", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    String player1WineS  = snapshot.getData().get("player1Wine").toString();
                    player1Wine = Integer.parseInt(player1WineS);
                    Tplayer1Win.setText(player1WineS);


                    String player2WineS  = snapshot.getData().get("player2Wine").toString();

                    player2Wine = Integer.parseInt(player2WineS);

                    Tplay2Win.setText(player2WineS);

                    String drowS  = snapshot.getData().get("drow").toString();

                    drow = Integer.parseInt(drowS);
                    Tdow1Win.setText(drowS);

                    String headerTextS  = snapshot.getData().get("activePlayer").toString();

                    headerText.setText(headerTextS);

                    Toast.makeText(OnlinePlayGameActivity.this,  snapshot.getData().get("activePlayer")+"", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(OnlinePlayGameActivity.this,  "Current data: null", Toast.LENGTH_SHORT).show();

                }
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


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to quit game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OnlinePlayGameActivity.this.finish();

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    public  boolean isInternetConnection()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else {
            return false;
        }
    }
}
