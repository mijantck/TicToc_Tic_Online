package com.mrsoftit.tictocticonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrsoftit.tictocticonline.note.UsersNote;

import java.util.Random;

import soup.neumorphism.NeumorphButton;

public class OnlinePlayerCreateGameID extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    String id;
    String player1name;
    String userProfileImageURL;
    boolean check = false;


    NeumorphButton random_pason,enterroomidButttom,with_my_friend;

    EditText roomidcode;

    String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    String randomStr ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_player_create_game_id);


        random_pason = findViewById(R.id.random_pason);
        enterroomidButttom = findViewById(R.id.enterroomidButttom);
        roomidcode = findViewById(R.id.roomidcode);
        with_my_friend = findViewById(R.id.with_my_friend);


        mAuth = FirebaseAuth.getInstance();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String user_id = currentUser.getUid();

        FirebaseFirestore product = FirebaseFirestore.getInstance();



        random_pason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                randomStr =generateRandom(candidateChars);

                if (mAuth != null){

                    String user_id = currentUser.getUid();

                    CollectionReference myInfo = FirebaseFirestore.getInstance()
                            .collection("users");

                    Query query =  myInfo.whereEqualTo("uID",user_id);
                    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().toString();

                                Toast.makeText(OnlinePlayerCreateGameID.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();

                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    UsersNote usersNote = document.toObject(UsersNote.class);

                                    player1name = usersNote.getName();
                                    userProfileImageURL = usersNote.getFbProfiteImageURL();

                                }


                                String ImageLink = "https://iili.io/2I8Aue.jpg";
                                String sharelinktext = "https://tictocticonline.page.link/?" +
                                        "link=https://tictocticonline.page.link/jofZ?" +
                                        "proID=" + "-" + "gameCode" +
                                        "-" + randomStr +
                                        "&st=" + player1name +
                                        "&sd=" + " play with me" +
                                        "&si=" + ImageLink +
                                        "&apn=" + getPackageName();

                                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        // .setLongLink(dynamicLink.getUri())
                                        .setLongLink(Uri.parse(sharelinktext))// manually
                                        .buildShortDynamicLink()
                                        .addOnCompleteListener(OnlinePlayerCreateGameID.this, new OnCompleteListener<ShortDynamicLink>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                                if (task.isSuccessful()) {
                                                    // Short link created
                                                    Uri shortLink = task.getResult().getShortLink();
                                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                                    Log.e("main ", "short link " + shortLink.toString());
                                                    // share app dialog
                                                    Intent intent = new Intent();
                                                    intent.setAction(Intent.ACTION_SEND);
                                                    intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                                                    intent.setType("text/plain");
                                                    startActivity(intent);

                                                } else {
                                                    Log.e("main", " error " + task.getException());
                                                }
                                            }
                                        });




                            }

                            else {

                                Toast.makeText(OnlinePlayerCreateGameID.this, "not ordk", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });

                    if ( check == true)
                    startActivity(new Intent(OnlinePlayerCreateGameID.this,OnlinePlayGameActivity.class));







                }

                if (isInternetConnection() == true){


                }else {

                    Toast.makeText(OnlinePlayerCreateGameID.this, "Not internet connection", Toast.LENGTH_SHORT).show();

                }
            }
        });

        with_my_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();



            if (mAuth != null){

                String user_id = currentUser.getUid();

                CollectionReference myInfo = FirebaseFirestore.getInstance()
                        .collection("users");

                Query query =  myInfo.whereEqualTo("uID",user_id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                UsersNote usersNote = document.toObject(UsersNote.class);

                                Toast.makeText(OnlinePlayerCreateGameID.this, usersNote.getuID() + "\n" + usersNote.getName() + "", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                });


        }
            else {

                Toast.makeText(OnlinePlayerCreateGameID.this, "not ordk", Toast.LENGTH_SHORT).show();
            }


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

    private static String generateRandom(String aToZ) {
        Random rand=new Random();
        StringBuilder res=new StringBuilder();
        for (int i = 0; i < 5 ; i++) {
            int randIndex=rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        return res.toString();
    }
}
