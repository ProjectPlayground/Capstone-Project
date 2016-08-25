package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Member;
import com.village.wannajoin.model.User;
import com.village.wannajoin.util.Constants;

import java.util.HashMap;

/**
 * Created by richa on 6/16/16.
 */
public class NewContactDialogFragment extends DialogFragment{

    private EditText mEditTextContact;
    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static NewContactDialogFragment newInstance() {
        NewContactDialogFragment newContactDialogFragment = new NewContactDialogFragment();
        return newContactDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.new_contact_dialog_title);
        View v = inflater.inflate(R.layout.dialog_fragment_new_contact, container, false);
        mEditTextContact = (EditText) v.findViewById(R.id.edit_text_contact);
        mEditTextContact.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addContact();
                }
                return true;
            }
        });


        return v;
    }

    private void addContact(){
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String emailString = mEditTextContact.getText().toString();
        Query fbUser = dbRef.child(Constants.FIREBASE_LOCATION_USERS).orderByChild("email").equalTo(emailString);
        fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getContext(),"User doesn't exist",Toast.LENGTH_LONG).show();
                }else{
                    for( DataSnapshot ds: dataSnapshot.getChildren()){
                        User member = ds.getValue(User.class);
                        HashMap<String, Object> timestampCreated = new HashMap<>();
                        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                        Member contact = new Member(member.getName(),member.getUserId(),member.getPhotoUrl(), timestampCreated);
                        DatabaseReference newContactRef = dbRef.child(Constants.FIREBASE_LOCATION_CONTACTS).child(currentUserId).child(member.getUserId());
                        newContactRef.setValue(contact);
                        getDialog().cancel();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
