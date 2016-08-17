package com.village.wannajoin.ui;

import android.content.Intent;
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
import com.village.wannajoin.R;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewEventActivity extends AppCompatActivity
        implements PlaceSelectionListener , DatePickerFragment.DateSelectedListener, TimePickerFragment.TimeSelectedListener {

    Button mStartDate;
    Button mEndDate;
    Button mStartTime;
    Button mEndTime;
    TextView mlocation;
    double mLocationLat;
    double mLocationLng;
    EditText mTitle;
    EditText mNotes;
   // EditText mMembers;
    static final String STATE_START_DATE = "startDate";
    static final String STATE_START_TIME = "startTime";
    static final String STATE_END_DATE = "endDate";
    static final String STATE_END_TIME = "endTime";
    //static final int SHARE_REQUEST = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            mStartDate.setText(savedInstanceState.getString(STATE_START_DATE));
            mStartTime.setText(savedInstanceState.getString(STATE_START_TIME));
            mEndDate.setText(savedInstanceState.getString(STATE_END_DATE));
            mEndTime.setText(savedInstanceState.getString(STATE_END_TIME));
        }else {
            final Calendar c = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM d, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            mStartDate.setText(dateFormat.format(c.getTime()));
            mEndDate.setText(dateFormat.format(c.getTime()));
            mStartTime.setText(timeFormat.format(c.getTime()));
            mEndTime.setText(timeFormat.format(c.getTime()));
        }
        // Retrieve the PlaceAutocompleteFragment.
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint(getString(R.string.hint_search_location));

        // Register a listener to receive callbacks when a place has been selected or an error has
        // occurred.
        autocompleteFragment.setOnPlaceSelectedListener(this);
       /* mMembers = (EditText) findViewById(R.id.event_member);
        mMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewEventActivity.this, ShareEventActivity.class);
                startActivityForResult(i,SHARE_REQUEST);
            }
        });*/


    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_START_DATE,mStartDate.getText().toString());
        outState.putString(STATE_START_TIME,mStartTime.getText().toString());
        outState.putString(STATE_END_DATE,mEndDate.getText().toString());
        outState.putString(STATE_END_TIME,mEndTime.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDateSet(int viewId, int year, int month, int day) {
        if (viewId == R.id.event_start_date)
            mStartDate.setText(Util.formatDate(year,month,day));
        if (viewId == R.id.event_end_date)
            mEndDate.setText(Util.formatDate(year,month,day));

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
    }

    /**
     * Callback invoked when PlaceAutocompleteFragment encounters an error.
     */
    @Override
    public void onError(Status status) {

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
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
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            startEventSharingActivity();
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

    private void startEventSharingActivity(){
        String eventTitle = mTitle.getText().toString();
        long eventFrom = Util.getTimeStamp(mStartDate.getText().toString(), mStartTime.getText().toString());
        long eventTo = Util.getTimeStamp(mEndDate.getText().toString(), mEndTime.getText().toString());
        String eventNotes= mNotes.getText().toString();
        String eventLocationName = mlocation.getText().toString();

        Intent i = new Intent(NewEventActivity.this, ShareEventActivity.class);
        i.putExtra(Constants.EVENT_TITLE,eventTitle);
        i.putExtra(Constants.EVENT_FROM, eventFrom);
        i.putExtra(Constants.EVENT_TO,eventTo);
        i.putExtra(Constants.EVENT_NOTES, eventNotes);
        i.putExtra(Constants.EVENT_LOCATION,eventLocationName);
        i.putExtra(Constants.EVENT_LOCATION_LAT,mLocationLat);
        i.putExtra(Constants.EVENT_LOCATION_LNG,mLocationLng);
        startActivity(i);

        /*
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (!eventTitle.equals("")) {

            DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS);
            DatabaseReference newEventRef = eventRef.push();
            final String eventId = newEventRef.getKey();

            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
            HashMap<String,Boolean> eventMembers = new HashMap<>();
            eventMembers.put(firebaseUser.getUid(),true);

            Event newEvent = new Event(eventId,eventTitle, firebaseUser.getUid(), firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl(), eventFrom, eventTo, timestampCreated,eventMembers);
            newEvent.setLocation(eventLocationName);
            newEvent.setNotes(eventNotes);
            newEventRef.setValue(newEvent);
            finish();
        }*/
    }


}
