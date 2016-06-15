package com.village.wannajoin.ui;

import android.content.Context;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.village.wannajoin.model.Group;
import com.village.wannajoin.model.User;
import com.village.wannajoin.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewGroupActivity extends AppCompatActivity {

    ArrayList<User> groupMembers;
    static final String GROUP_NAME = "group_name";
    static final String GROUP_MEMBERS = "group_members";
    EditText mGroupName;
    EditText mContactEmail;
    GroupMemberAdapter groupMemberAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mGroupName = (EditText) findViewById(R.id.group_name);
        mContactEmail = (EditText) findViewById(R.id.participant_email);
        Button memberAddButton = (Button)findViewById(R.id.member_add);
        if(savedInstanceState!=null){
            mGroupName.setText(savedInstanceState.getString(GROUP_NAME));
            groupMembers = savedInstanceState.getParcelableArrayList(GROUP_MEMBERS);

        }else {
            groupMembers = new ArrayList<User>();
        }
        ListView listView = (ListView)findViewById(R.id.member_list);
        groupMemberAdapter = new GroupMemberAdapter(groupMembers,this);
        listView.setAdapter(groupMemberAdapter);

        mContactEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addMember();
                    return true;
                }
                return false;
            }
        });


        memberAddButton.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   addMember();
                                               }
                                           }
        );


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(GROUP_NAME,mGroupName.getText().toString());
        outState.putParcelableArrayList(GROUP_MEMBERS,groupMembers);
        super.onSaveInstanceState(outState);
    }

    private void addMember(){
        String emailString = mContactEmail.getText().toString();
        Query fbUser = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).orderByChild("email").equalTo(emailString);
        fbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(NewGroupActivity.this,"User doesn't exist",Toast.LENGTH_LONG).show();
                }else{
                    for( DataSnapshot ds: dataSnapshot.getChildren()){
                        User member = ds.getValue(User.class);
                        if(!groupMembers.contains(member)) {
                            groupMembers.add(member);
                            groupMemberAdapter.updateAdapter(groupMembers);
                            groupMemberAdapter.notifyDataSetChanged();
                        }
                        mContactEmail.setText("");
                        hideSoftKeyBoard();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void hideSoftKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveGroup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveGroup(){

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference newGroupRef = dbRef.child(Constants.FIREBASE_LOCATION_GROUPS).push();


        final String groupId = newGroupRef.getKey();
        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        HashMap<String, Boolean> groupMembersMap = new HashMap<>();
        Map<String, Object> childUpdates = new HashMap<>();
        for(User user: groupMembers){
            groupMembersMap.put(user.getUserId(),true);
            childUpdates.put("/"+Constants.FIREBASE_LOCATION_USERS+"/"+ user.getUserId()+"/"+Constants.FIREBASE_LOCATION_GROUPS+"/"+groupId,true);
        }

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        groupMembersMap.put(currentUserId,true);
        childUpdates.put("/"+Constants.FIREBASE_LOCATION_USERS+"/"+ currentUserId+"/"+Constants.FIREBASE_LOCATION_GROUPS+"/"+groupId,true);

        Group group = new Group(mGroupName.getText().toString(),groupId, currentUserId,null, timestampCreated,groupMembersMap);


        childUpdates.put("/"+Constants.FIREBASE_LOCATION_GROUPS+"/" + groupId, group.toMap());


        dbRef.updateChildren(childUpdates);
        finish();
    }
}
