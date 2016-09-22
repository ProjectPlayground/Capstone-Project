package com.village.joinalong.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.village.joinalong.R;
import com.village.joinalong.util.Constants;
import com.village.joinalong.model.User;

import java.util.HashMap;

public class WelcomeActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final String LOG_TAG = WelcomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.GOOGLE_PROVIDER)
                            .setTheme(R.style.SignInTheme)
                            .setLogo(R.drawable.wannajoin)
                            .build(),
                    RC_SIGN_IN);
        }else{
            createUserInFirebase(auth.getCurrentUser());
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                createUserInFirebase(FirebaseAuth.getInstance().getCurrentUser());
                Intent i = new Intent(this,MainActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(this, R.string.sign_in_failed_msg,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUserInFirebase(final FirebaseUser firebaseUser){
        final DatabaseReference userLocation = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).child(firebaseUser.getUid());
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    /* If nothing is there ...*/
                String userPhotoUrl=null;
                if (firebaseUser.getPhotoUrl()!=null)
                    userPhotoUrl = firebaseUser.getPhotoUrl().toString();
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(),firebaseUser.getEmail(), userPhotoUrl,timestampJoined);
                    userLocation.setValue(newUser);
                }else{
                    User user = dataSnapshot.getValue(User.class);
                    User updateUser = new User(firebaseUser.getDisplayName(), firebaseUser.getUid(),firebaseUser.getEmail(), userPhotoUrl,user.getTimestampJoined());
                    userLocation.setValue(updateUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(LOG_TAG, databaseError.getMessage());
            }
        });

    }


    public void startLoginActivity(View view) {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
                                AuthUI.GOOGLE_PROVIDER)
                        .setTheme(R.style.SignInTheme)
                        .setLogo(R.drawable.wannajoin)
                        .build(),
                RC_SIGN_IN);
    }
}
