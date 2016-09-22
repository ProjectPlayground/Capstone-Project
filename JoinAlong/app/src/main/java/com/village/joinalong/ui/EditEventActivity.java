package com.village.joinalong.ui;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.village.joinalong.R;
import com.village.joinalong.util.Constants;
import com.village.joinalong.util.Util;
import com.village.joinalong.model.Event;
import com.village.joinalong.util.NotificationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class EditEventActivity extends AppCompatActivity
        implements PlaceSelectionListener , DatePickerFragment.DateSelectedListener, TimePickerFragment.TimeSelectedListener {

    Button mStartDate;
    Button mEndDate;
    Button mStartTime;
    Button mEndTime;
    TextView mlocation;
    double mLocationLat;
    double mLocationLng;
    String mLocationId;
    EditText mTitle;
    EditText mNotes;
    // EditText mMembers;
    static final String STATE_START_DATE = "startDate";
    static final String STATE_START_TIME = "startTime";
    static final String STATE_END_DATE = "endDate";
    static final String STATE_END_TIME = "endTime";
    static final String STATE_TITLE = "title";
    static final String STATE_LOCATION = "location";
    static final String STATE_NOTES = "notes";
    Event mEvent;
    //static final int SHARE_REQUEST = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mEvent = getIntent().getParcelableExtra(Constants.EVENT);
        initializeUI(savedInstanceState);

    }

    public void initializeUI(Bundle savedInstanceState){

        mTitle = (EditText)findViewById(R.id.event_title);
        mNotes = (EditText)findViewById(R.id.event_notes);

        //to allow soft wrapping of text in multiple lines.
        mTitle.setHorizontallyScrolling(false);
        mTitle.setMaxLines(Integer.MAX_VALUE);
        mNotes.setHorizontallyScrolling(false);
        mNotes.setMaxLines(Integer.MAX_VALUE);


        //initialize date and time with current time


        mStartDate = (Button)findViewById(R.id.event_start_date);
        mEndDate = (Button)findViewById(R.id.event_end_date);
        mStartTime = (Button)findViewById(R.id.event_start_time);
        mEndTime = (Button)findViewById(R.id.event_end_time);
        mlocation = (TextView)findViewById(R.id.event_location);
        if (savedInstanceState!=null){
            mTitle.setText(savedInstanceState.getString(STATE_TITLE));
            mlocation.setText(savedInstanceState.getString(STATE_LOCATION));
            mStartDate.setText(savedInstanceState.getString(STATE_START_DATE));
            mStartTime.setText(savedInstanceState.getString(STATE_START_TIME));
            mEndDate.setText(savedInstanceState.getString(STATE_END_DATE));
            mEndTime.setText(savedInstanceState.getString(STATE_END_TIME));
            mNotes.setText(savedInstanceState.getString(STATE_NOTES));
        }else {
            mTitle.setText(mEvent.getTitle());
            mlocation.setText(mEvent.getLocation());
            mStartDate.setText(Util.getFormattedDateFromTimeStamp(mEvent.getFromTime()));
            mEndDate.setText(Util.getFormattedDateFromTimeStamp(mEvent.getToTime()));
            mStartTime.setText(Util.getTimeFromTimeStamp(mEvent.getFromTime()));
            mEndTime.setText(Util.getTimeFromTimeStamp(mEvent.getToTime()));
            mNotes.setText(mEvent.getNotes());
            mLocationId = mEvent.getLocationId();
            mLocationLat = mEvent.getLocationLat();
            mLocationLng = mEvent.getLocationLng();
        }
        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.hint_search_location));

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TITLE,mTitle.getText().toString());
        outState.putString(STATE_LOCATION,mlocation.getText().toString());
        outState.putString(STATE_START_DATE,mStartDate.getText().toString());
        outState.putString(STATE_START_TIME,mStartTime.getText().toString());
        outState.putString(STATE_END_DATE,mEndDate.getText().toString());
        outState.putString(STATE_END_TIME,mEndTime.getText().toString());
        outState.putString(STATE_NOTES,mNotes.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDateSet(int viewId, int year, int month, int day) {
        if (viewId == R.id.event_start_date) {
            String startDate = Util.formatDate(year, month, day);
            String endDate = mEndDate.getText().toString();
            if (Util.isDateBefore(startDate,endDate)){
                mStartDate.setText(startDate);
            }else{
                mStartDate.setText(startDate);
                mEndDate.setText(startDate);
            }
        }
        if (viewId == R.id.event_end_date) {
            String endDate = Util.formatDate(year, month, day);
            String startDate = mStartDate.getText().toString();
            if (Util.isDateBefore(startDate,endDate)){
                mEndDate.setText(endDate);
            }else{
                mStartDate.setText(endDate);
                mEndDate.setText(endDate);
            }
        }

    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("view_id",v.getId() );
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }



    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("view_id",v.getId() );
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }


    /**
     * Callback invoked when a place has been selected from the PlaceAutocompleteFragment.
     */
    @Override
    public void onPlaceSelected(Place place) {

       /* mlocation.setText(formatPlaceDetails(getResources(), place.getName(), place.getId(),
                place.getAddress(), place.getPhoneNumber(), place.getWebsiteUri()).toString());*/
        mlocation.setText(place.getName()+"\n"+place.getAddress());
        mLocationLat = place.getLatLng().latitude;
        mLocationLng = place.getLatLng().longitude;
        mLocationId = place.getId();
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {

        Toast.makeText(this, getString(R.string.place_selection_failed) + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper method to format information about a place nicely.
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {

        return Html.fromHtml(res.getString(R.string.place_details, name, address, phoneNumber,
                websiteUri));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            saveEvent();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(int viewId, int hourOfDay, int minute) {
        if (viewId == R.id.event_start_time)
            mStartTime.setText(Util.formatTime(hourOfDay,minute));
        if (viewId == R.id.event_end_time)
            mEndTime.setText(Util.formatTime(hourOfDay,minute));
    }

    private void saveEvent(){
        String eventTitle = mTitle.getText().toString();
        long eventFrom = Util.getTimeStamp(mStartDate.getText().toString(), mStartTime.getText().toString());
        long eventTo = Util.getTimeStamp(mEndDate.getText().toString(), mEndTime.getText().toString());
        String eventNotes= mNotes.getText().toString();
        String eventLocationName = mlocation.getText().toString();

        if (eventTitle.equals("")){
            Toast.makeText(this, R.string.event_title_data_error,Toast.LENGTH_SHORT).show();
        }else{
            if (eventFrom>eventTo){
                Toast.makeText(this, R.string.event_timing_data_error,Toast.LENGTH_SHORT).show();
            }else{

                DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS).child(mEvent.getEventId());

                HashMap<String, Object> timestampLastUpdated = new HashMap<>();
                timestampLastUpdated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
                mEvent.setTitle(eventTitle);
                mEvent.setLocation(eventLocationName);
                mEvent.setLocationId(mLocationId);
                mEvent.setLocationLat(mLocationLat);
                mEvent.setLocationLng(mLocationLng);
                mEvent.setNotes(eventNotes);
                mEvent.setFromTime(eventFrom);
                mEvent.setToTime(eventTo);
                mEvent.setTimestampLastChanged(timestampLastUpdated);
                eventRef.setValue(mEvent);
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                ArrayList<String> sendToUsers = new ArrayList<>();
                Set<String> set = mEvent.getEventMembers().keySet();
                Iterator<String> iterator = set.iterator();
                while(iterator.hasNext()){
                    String id = iterator.next();
                    if (!id.equals(firebaseUser.getUid()))
                    sendToUsers.add(id);
                }
                NotificationUtil.sendEventNotification(mEvent.getEventId(),firebaseUser.getDisplayName(),"Update",eventTitle,sendToUsers);
                finish();
            }
        }
    }


}
