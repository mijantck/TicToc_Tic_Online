package com.mrsoftit.tictocticonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrsoftit.tictocticonline.note.UsersNote;
import com.squareup.picasso.Picasso;

import soup.neumorphism.NeumorphImageView;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity  implements
        View.OnClickListener {


    FirebaseAuth auth;
    FirebaseFirestore db;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient mGoogleSignInClient;


    NeumorphImageView imageView;

    TextView userName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        imageView  = findViewById(R.id.imageView);
        userName  = findViewById(R.id.userName);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]


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

        docRef.update("status","online")
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

        docRef.update("status","online")
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

        Intent intent = new Intent(this, textActivity.class);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);

        }

    }

    public void logout(View view) {


        auth.signOut();

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        revokeAccess();
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
        LoginManager loginManager =LoginManager.getInstance();
        loginManager.logOut();

        startActivity(new Intent(this,LoginActivity.class));
        finish();


    }


    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {

        } else {

        }
    }

    @Override
    public void onClick(View view) {

    }


//    public void exitGame(View view){
//        finish();
//        System.exit(0);
//    }
}
