package com.mrsoftit.tictocticonline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    LoginButton loginButton;
    CallbackManager callbackManager;
    ImageView imageView;
    TextView txtUsername, txtEmail;

     FirebaseFirestore db;

    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;
    FirebaseAuth firebaseAuth;
    GoogleApiClient mGoogleApiClient;




    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    String first_name;
    String last_name;
    String email;
    String image_url;
    String id;

    ProgressDialog progressDialog;

    private CallbackManager mCallbackManager;
    private static final String EMAIL = "email";
    ArrayList<String> ListOfFriendsIDS ;
    ArrayList<String> ListOfFriendsNames ;

    public LoginActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        loginButton = findViewById(R.id.login_button);
        imageView = findViewById(R.id.imageView);
        txtUsername = findViewById(R.id.txtUsername);
        txtEmail = findViewById(R.id.txtEmail);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);


        //google


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){



           startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        // Views

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);

        // [END initialize_auth]

        ListOfFriendsIDS=new ArrayList<String>();//Creating arraylist
        ListOfFriendsNames=new ArrayList<String>();//Creating arraylist

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile","user_friends"));
        // If you are using in a fragment, call loginButton.setFragment(this);
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                progressDialog.show();
                handleFacebookAccessToken(loginResult.getAccessToken());
                getUserProfile(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }


    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("TAG", object.toString());
                        try {
                           first_name = object.getString("first_name");
                           last_name = object.getString("last_name");
                           email = object.getString("email");
                            id = object.getString("id");
                            image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            txtUsername.setText("First Name: " + first_name + "\nLast Name: " + last_name);
                            txtEmail.setText(email);
                            Picasso.get().load(image_url).into(imageView);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }


    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            UpdateUserInformation(user);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void UpdateUserInformation(FirebaseUser user1) {

        String name = first_name+" "+last_name;
        String uID = user1.getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("name",name );
        user.put("email", email);
        user.put("fbID", id);
        user.put("fbProfiteImageURL", image_url);
        user.put("uID",uID );
        user.put("currentGameID",null );
        user.put("status","online" );
        user.put("winGame",0);
        user.put("how_money_mach",0);

        db = FirebaseFirestore.getInstance();

        db.collection("users").document(user1.getUid()).set(user)
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

    private void updateUI(FirebaseUser user) {
        if (user != null){

          //  startActivity(new Intent(this,MainActivity.class));

        }
    }



    private void signIn() {
        Intent signIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                authWithGoogle(account);
            }
        }
    }

    private void authWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){




                    FirebaseUser UserInfo = firebaseAuth.getCurrentUser();
                    db = FirebaseFirestore.getInstance();

                    String name = UserInfo.getDisplayName();
                    String uID = UserInfo.getUid();
                    String uEmail = UserInfo.getEmail();
                    String uDP = UserInfo.getPhotoUrl().toString();

                    final Map<String, Object> user = new HashMap<>();
                    user.put("name",name );
                    user.put("email", uEmail);
                    user.put("fbID", "null");
                    user.put("fbProfiteImageURL",uDP );
                    user.put("uID",uID );
                    user.put("currentGameID",null );
                    user.put("status","online" );
                    user.put("winGame",0);
                    user.put("how_money_mach",0);


                    db.collection("users").document(uID).set(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();

                                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });


                }
                else{
                    Toast.makeText(getApplicationContext(),"Auth Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

