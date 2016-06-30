package com.village.wannajoin.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.village.wannajoin.R;
import com.village.wannajoin.model.ContactAndGroup;
import com.village.wannajoin.model.Group;
import com.village.wannajoin.model.User;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.Util;

import java.util.ArrayList;
import java.util.Collections;

public class ShareEventActivity extends AppCompatActivity {
    ArrayList<ContactAndGroup> contactList;
    ShareEventRecyclerViewAdapter_1 mShareEventRecyclerViewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        contactList = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.share_event_contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mShareEventRecyclerViewAdapter = new ShareEventRecyclerViewAdapter_1(this,contactList);
        initializeData();
        recyclerView.setAdapter(mShareEventRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_event, menu);
        return true;
    }

    public void initializeData(){
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query mGroupRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUPS).orderByChild("groupMembers/"+currentUserId).equalTo(true);
        Query mContactRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_USERS).orderByChild("contactOf/"+currentUserId).equalTo(true);
        mGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Group group = ds.getValue(Group.class);
                    ContactAndGroup cg = new ContactAndGroup(group.getName(),group.getGroupId(),group.getGroupPhotoUrl(),true);
                    contactList.add(cg);
                }
                mShareEventRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mContactRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    ContactAndGroup cg = new ContactAndGroup(Util.capitalizeWords(user.getName()),user.getUserId(),user.getPhotoUrl(),false);
                    contactList.add(cg);
                }
                mShareEventRecyclerViewAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Collections.sort(contactList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            finish();
            return true;
        }
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
