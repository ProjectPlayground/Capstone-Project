package com.village.wannajoin.ui;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.NotificationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class EventDetailActivity extends AppCompatActivity implements EventDetailFragment.OnFragmentInteractionListener  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String mEventId;
    private Event mEvent;
    private String menuTitle;
    private String fromTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.event_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mEvent = getIntent().getParcelableExtra(Constants.EVENT);
        mEventId = mEvent.getEventId();

        getSupportActionBar().setTitle(mEvent.getTitle());
        String status = mEvent.getEventMembers().get(FirebaseAuth.getInstance().getCurrentUser().getUid());
        String[] statusArray = status.split("-");
        menuTitle = statusArray[1];
        fromTime = String.valueOf(mEvent.getFromTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item = menu.findItem(R.id.action_join);
        if (menuTitle.equals("0"))
            item.setTitle(getString(R.string.join_button_text));
        else
            item.setTitle(getString(R.string.cancel_button_text));
        MenuItem itemDelete = menu.findItem(R.id.action_delete);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(mEvent.getOwnerId())){
            itemDelete.setVisible(true);
        }else
            itemDelete.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_join) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference dbrefJoin = FirebaseDatabase.getInstance().getReference();
            String newStatus=null;
            String newStatus1 = null;
            if (fromTime !=null) {
                if (menuTitle.equals("0")) {
                    newStatus = fromTime + "-1";
                    newStatus1 = "1";
                } else {
                    newStatus = fromTime + "-0";
                    newStatus1 = "0";
                }
                Map<String, Object> childUpdates = new HashMap<>();
                //dbrefJoin.setValue(newStatus);
                childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + mEventId + "/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + firebaseUser.getUid(), newStatus);
                childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENT_MEMBERS + "/" + mEventId + "/" + Constants.FIREBASE_LOCATION_USERS + "/" + firebaseUser.getUid() + "/" + Constants.FIREBASE_LOCATION_MEMBER_STATUS, newStatus1);
                dbrefJoin.updateChildren(childUpdates);
            }
            return true;
        }

        if (id == R.id.action_delete) {
            DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/" + Constants.FIREBASE_LOCATION_EVENTS + "/" + mEventId,null);
            childUpdates.put("/"+Constants.FIREBASE_LOCATION_EVENT_MEMBERS+"/"+mEventId,null);
            dbref.updateChildren(childUpdates);
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ArrayList<String> sendToUsers = new ArrayList<>();
            Set<String> set = mEvent.getEventMembers().keySet();
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()){
                String uid = iterator.next();
                if (!uid.equals(firebaseUser.getUid()))
                    sendToUsers.add(uid);
            }
            NotificationUtil.sendEventNotification(firebaseUser.getDisplayName(),"Delete",mEvent.getTitle(),sendToUsers);
            finish();
            return true;
        }

        if (id ==android.R.id.home){
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String status, String fromTime) {
        menuTitle = status;
        this.fromTime = fromTime;
        invalidateOptionsMenu();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_event_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0: return EventDetailFragment.newInstance(mEvent);
                case 1: return EventMemberFragment.newInstance(mEvent);
            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Detail";
                case 1:
                    return "People";
            }
            return null;
        }
    }
}
