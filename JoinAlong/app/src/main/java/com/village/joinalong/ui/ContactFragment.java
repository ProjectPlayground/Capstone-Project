package com.village.joinalong.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.village.joinalong.R;
import com.village.joinalong.model.ContactAndGroup;
import com.village.joinalong.model.Event;
import com.village.joinalong.model.Group;
import com.village.joinalong.model.Member;
import com.village.joinalong.util.ArrayFirebase;
import com.village.joinalong.util.Constants;
import com.village.joinalong.util.DividerItemDecoration;
import com.village.joinalong.util.NotificationUtil;
import com.village.joinalong.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ContactFragment extends Fragment implements ContactsRecyclerViewAdapter.ViewClickedListener{
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    ArrayList<ContactAndGroup> contactAndGroupArrayList;
    ContactsRecyclerViewAdapter mContactsRecyclerViewAdapter;
    ArrayFirebase mSnapshotsContacts;
    ArrayFirebase mSnapshotsGroups;
    int lastGroupPosition;
    private boolean mTwoPane;
    //private OnFragmentInteractionListener mListener;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(int param1) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);

        }
        contactAndGroupArrayList = new ArrayList<>();
        contactAndGroupArrayList.add(new ContactAndGroup(getString(R.string.group_list_label), null,null,true,0));
        contactAndGroupArrayList.add(new ContactAndGroup(getString(R.string.empty_group_list_text),null,null,true,-1));
        contactAndGroupArrayList.add(new ContactAndGroup(getString(R.string.contacts_list_label), null,null,false,0));
        contactAndGroupArrayList.add(new ContactAndGroup(getString(R.string.empty_contact_list_text),null,null,false,-1));
        lastGroupPosition =1;
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query mGroupRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_GROUPS).orderByChild("groupMembers/"+currentUserId).startAt("");
        Query mContactRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_CONTACTS).child(currentUserId).orderByChild("name");
        mSnapshotsGroups = new ArrayFirebase(mGroupRef);
        mSnapshotsContacts = new ArrayFirebase(mContactRef);

        mSnapshotsContacts.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Member member = mSnapshotsContacts.getItem(index).getValue(Member.class);
                        ContactAndGroup cg = new ContactAndGroup(Util.capitalizeWords(member.getName()),member.getUserId(),member.getPhotoUrl(),false,2);
                        int pos = contactAndGroupArrayList.size();
                        if (contactAndGroupArrayList.get(pos-1).getType()==-1){
                            contactAndGroupArrayList.remove(pos-1);
                            mContactsRecyclerViewAdapter.notifyItemRemoved(pos-1);
                        }
                        pos = index+lastGroupPosition+1;
                        Log.d("RB","pos contact: "+pos);
                        contactAndGroupArrayList.add(pos,cg);
                        mContactsRecyclerViewAdapter.notifyItemInserted(pos);
                        break;
                    case Changed:
                        Member member1 = mSnapshotsContacts.getItem(index).getValue(Member.class);
                        ContactAndGroup cg1 = new ContactAndGroup(Util.capitalizeWords(member1.getName()),member1.getUserId(),member1.getPhotoUrl(),false,2);
                        int posc = index+lastGroupPosition+1;
                        contactAndGroupArrayList.set(posc,cg1);
                        mContactsRecyclerViewAdapter.notifyItemChanged(posc);
                        break;
                    case Removed:
                        int posr = index+lastGroupPosition+1;
                        contactAndGroupArrayList.remove(posr);
                        mContactsRecyclerViewAdapter.notifyItemRemoved(posr);
                        break;
                    case Moved:
                        mContactsRecyclerViewAdapter.notifyItemMoved(oldIndex+lastGroupPosition+1, index+lastGroupPosition+1);
                        break;
                    default:
                        throw new IllegalStateException(getString(R.string.snapshots_incomplete_case_error));
                }
            }
        });

        mSnapshotsGroups.setOnChangedListener(new ArrayFirebase.OnChangedListener() {
            @Override
            public void onChanged(EventType type, int index, int oldIndex) {
                switch (type) {
                    case Added:
                        Group group = mSnapshotsGroups.getItem(index).getValue(Group.class);
                        ContactAndGroup cg = new ContactAndGroup(group.getName(),group.getGroupId(),group.getGroupPhotoUrl(),true,1);
                        int pos = lastGroupPosition;
                        if(contactAndGroupArrayList.get(pos).getType()==-1){
                            contactAndGroupArrayList.remove(pos);
                            mContactsRecyclerViewAdapter.notifyItemRemoved(pos);
                        }
                        pos = index+1;
                        Log.d("RB","pos group: "+pos);
                        contactAndGroupArrayList.add(pos,cg);
                        lastGroupPosition = lastGroupPosition+1;
                        mContactsRecyclerViewAdapter.notifyItemInserted(pos);
                        break;
                    case Changed:
                        Group groupc = mSnapshotsGroups.getItem(index).getValue(Group.class);
                        ContactAndGroup cgc = new ContactAndGroup(groupc.getName(),groupc.getGroupId(),groupc.getGroupPhotoUrl(),true,1);
                        int posc = index+1;
                        contactAndGroupArrayList.set(posc,cgc);
                        mContactsRecyclerViewAdapter.notifyItemChanged(posc);
                        break;
                    case Removed:
                        int posr = index+1;
                        contactAndGroupArrayList.remove(posr);
                        lastGroupPosition = lastGroupPosition-1;
                        mContactsRecyclerViewAdapter.notifyItemRemoved(posr);
                        break;
                    case Moved:
                        mContactsRecyclerViewAdapter.notifyItemMoved(oldIndex+1, index+1);
                        break;
                    default:
                        throw new IllegalStateException("Incomplete case statement");
                }
            }
        });
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        Context context = view.getContext();
        RecyclerView contactRecyclerView = (RecyclerView) view.findViewById(R.id.contact_list);
        if (mColumnCount <= 1) {
            contactRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            contactRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        contactRecyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));
        mContactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(context,getActivity().getClass().getSimpleName(),contactAndGroupArrayList,this);
        contactRecyclerView.setAdapter(mContactsRecyclerViewAdapter);
        if (view.findViewById(R.id.detail_container)!=null)
            mTwoPane = true;
        else
            mTwoPane=false;
        return view;
    }


/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
   /* public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_contact_fragment,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getActivity().getClass().getSimpleName().equals("MainActivity")) {
            MenuItem item = menu.findItem(R.id.action_save);
            item.setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.new_contact){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = NewContactDialogFragment.newInstance();
            newFragment.show(ft, "dialog");
            return true;
        }

        if(id == R.id.new_group){
            Intent i = new Intent(getActivity(),NewGroupActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_save) {
            String actionType = getActivity().getIntent().getStringExtra("type");
            if (actionType.equals("NEW")) {
                saveNewEvent();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else{
                updateEvent();
            }
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSnapshotsContacts.cleanup();
        mSnapshotsGroups.cleanup();

    }

    private void saveNewEvent(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userPhotoUrl=null;
        if (firebaseUser.getPhotoUrl()!=null)
            userPhotoUrl = firebaseUser.getPhotoUrl().toString();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        if(getActivity().getClass().getSimpleName().equals("ShareEventActivity")) {
            final Intent i = getActivity().getIntent();
            DatabaseReference eventRef = dbRef.child(Constants.FIREBASE_LOCATION_EVENTS);
            DatabaseReference newEventRef = eventRef.push();
            final String eventId = newEventRef.getKey();
            HashMap<String, String> eventMembers = new HashMap<>();
            long eventFrom = i.getLongExtra(Constants.EVENT_FROM, 0);
            String ownerStatus = String.valueOf(eventFrom)+"-1";
            eventMembers.put(firebaseUser.getUid(), ownerStatus);

            Event newEvent = new Event(eventId, i.getStringExtra(Constants.EVENT_TITLE), firebaseUser.getUid(), firebaseUser.getDisplayName(), userPhotoUrl, i.getLongExtra(Constants.EVENT_FROM, 0), i.getLongExtra(Constants.EVENT_TO, 0), timestampCreated, eventMembers);
            newEvent.setLocation(i.getStringExtra(Constants.EVENT_LOCATION));
            newEvent.setNotes(i.getStringExtra(Constants.EVENT_NOTES));
            newEvent.setLocationLat(i.getDoubleExtra(Constants.EVENT_LOCATION_LAT,0.0));
            newEvent.setLocationLng(i.getDoubleExtra(Constants.EVENT_LOCATION_LNG,0.0));
            newEvent.setLocationId(i.getStringExtra(Constants.EVENT_LOCATION_ID));
            newEventRef.setValue(newEvent);
            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + firebaseUser.getUid(), new Member(firebaseUser.getDisplayName(), firebaseUser.getUid(), userPhotoUrl, "1",timestampCreated).toMap());
            final String memberStatus = String.valueOf(eventFrom)+"-0";
            final ArrayList<String> sendToUsers = new ArrayList<>();
            for (ContactAndGroup cg : contactAndGroupArrayList) {
                if ((cg.getType() == 2) && (cg.isSelected())) {
                    childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + cg.getId(), memberStatus);
                    childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + cg.getId(), new Member(cg.getName(), cg.getId(), cg.getPhotoUrl(), "0",timestampCreated).toMap());
                    sendToUsers.add(cg.getId());
                }
                if ((cg.getType() == 1) && (cg.isSelected())) {
                   //get group members and associate them with the event
                    DatabaseReference groupRef = dbRef.child(Constants.FIREBASE_LOCATION_GROUP_MEMBERS).child(cg.getId());
                    groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> groupUpdates = new HashMap<>();
                            for(DataSnapshot ds: dataSnapshot.getChildren()){
                                Member member = ds.getValue(Member.class);
                                member.setTimestampJoined(timestampCreated);
                                member.setStatus("0");
                                groupUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + member.getUserId(), memberStatus);
                                groupUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + member.getUserId(), member.toMap());
                                sendToUsers.add(member.getUserId());
                            }
                            dbRef.updateChildren(groupUpdates);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }


            }
            dbRef.updateChildren(childUpdates);
            NotificationUtil.sendEventNotification(eventId,firebaseUser.getDisplayName(),"New",i.getStringExtra(Constants.EVENT_TITLE),sendToUsers);
        }


    }

    private void updateEvent(){
        Intent i = getActivity().getIntent();
        final Event event = i.getParcelableExtra(Constants.EVENT);
        final String eventId = event.getEventId();
       // ArrayList<Member> eventMembers = i.getParcelableArrayListExtra(Constants.EVENT_MEMBERS);
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        final HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        final ArrayList<String> sendToUsers = new ArrayList<>();
        Map<String, Object> childUpdates = new HashMap<>();
        final String memberStatus = String.valueOf(event.getFromTime())+"-0";
        for (final ContactAndGroup cg : contactAndGroupArrayList) {
            if ((cg.getType() == 2) && (cg.isSelected()) && (!event.getEventMembers().containsKey(cg.getId()))) {
                childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + cg.getId(), memberStatus);
                childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + cg.getId(), new Member(cg.getName(), cg.getId(), cg.getPhotoUrl(), "0",timestampCreated).toMap());
                sendToUsers.add(cg.getId());
            }
            if ((cg.getType() == 1) && (cg.isSelected())) {
                //get group members and associate them with the event
                DatabaseReference groupRef = dbRef.child(Constants.FIREBASE_LOCATION_GROUP_MEMBERS).child(cg.getId());
                groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Object> groupUpdates = new HashMap<>();
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            Member member = ds.getValue(Member.class);
                            member.setTimestampJoined(timestampCreated);
                            member.setStatus("0");
                            if (!event.getEventMembers().containsKey(member.getUserId())) {
                                groupUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + member.getUserId(), memberStatus);
                                groupUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + eventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + member.getUserId(), member.toMap());
                                sendToUsers.add(member.getUserId());
                            }
                        }
                        dbRef.updateChildren(groupUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }


        }
        dbRef.updateChildren(childUpdates);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        NotificationUtil.sendEventNotification(eventId,firebaseUser.getDisplayName(),"New",i.getStringExtra(Constants.EVENT_TITLE),sendToUsers);

    }

    @Override
    public void onItemClicked(int type, ContactAndGroup contactAndGroup) {
        if (contactAndGroup ==null ){
            if (type == 1) {
                //create a new group
                Intent i = new Intent(getActivity(), NewGroupActivity.class);
                startActivity(i);
            } else if (type == 2) {
                //add a contact
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NewContactDialogFragment.newInstance();
                newFragment.show(ft, "dialog");
            }
        }else{
            if (type == 1) {
                //show group details
                if (mTwoPane){
                    GroupDetailFragment groupDetailFragment = new GroupDetailFragment();
                    Bundle args = new Bundle();
                    args.putParcelable(Constants.GROUP,contactAndGroup);
                    groupDetailFragment.setArguments(args);
                    getChildFragmentManager().beginTransaction().replace(R.id.detail_container, groupDetailFragment).commit();

                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle();
                        // startActivity(i, bundle);
                        Intent i = new Intent(getActivity(), GroupDetailActivity.class);
                        i.putExtra(Constants.GROUP, contactAndGroup);
                        startActivity(i, bundle);
                    } else {
                        Intent i = new Intent(getActivity(), GroupDetailActivity.class);
                        i.putExtra(Constants.GROUP, contactAndGroup);
                        startActivity(i);
                    }
                }


            }else if(type ==2){
                //show contact detail dialog fragment
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("contactdetail");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = ContactDetailDialogFragment.newInstance(contactAndGroup.getId(),contactAndGroup.getName(),contactAndGroup.getPhotoUrl());
                newFragment.show(ft, "contactdetail");

            }
        }

    }

}
