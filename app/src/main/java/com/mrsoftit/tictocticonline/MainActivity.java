package com.mrsoftit.tictocticonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrsoftit.tictocticonline.note.UsersNote;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseFirestore db;

    ImageView imageView;
    TextView userName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        imageView  = findViewById(R.id.imageView);
        userName  = findViewById(R.id.userName);



    }


    @Override
    protected void onStart() {
        super.onStart();

        DocumentReference docRef = db.collection("users").document(auth.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UsersNote usersNote = documentSnapshot.toObject(UsersNote.class);
                userName.setText(usersNote.getName());

                Picasso.get().load(usersNote.getFbProfiteImageURL())
                        .into(imageView);

            }
        });
        
        
    }

    public void playTwoPGame(View view) {
        Intent intent = new Intent(this,PlayerName.class);
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }

    }

    public void playSinglePGame(View view) {
        Intent intent = new Intent(this, PlayerNameWithComputer.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }
    }

    public void OnlieplayTwoPGame(View view) {
        Intent intent = new Intent(this, OnlinePlayGameActivity.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }

    }

    public void logout(View view) {

        LoginManager loginManager =LoginManager.getInstance();
        loginManager.logOut();

        auth.signOut();
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }




//    public void exitGame(View view){
//        finish();
//        System.exit(0);
//    }
}
