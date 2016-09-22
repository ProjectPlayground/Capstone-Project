package com.village.joinalong.widget;

import android.content.Intent;
import android.os.Binder;

import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.village.joinalong.R;
import com.village.joinalong.util.Constants;
import com.village.joinalong.util.Util;
import com.village.joinalong.model.Event;

import java.util.ArrayList;

/**
 * Created by richa on 8/26/16.
 */
public class WidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private ArrayList<Event> eventsData;
            private  boolean  dataLoadedFlag;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (eventsData != null) {
                    eventsData = null;
                }
                dataLoadedFlag = false;
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Binder.restoreCallingIdentity(identityToken);
                if (FirebaseAuth.getInstance().getCurrentUser() !=null) {

                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Query dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS).orderByChild("eventMembers/" + currentUserId).startAt(Util.getMidNightTimeStamp());//.endAt(Util.getMidNightTimeStampNextDay());
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                             if (dataSnapshot.exists()) {
                                 eventsData = new ArrayList<>();
                                 for(DataSnapshot ds:dataSnapshot.getChildren()){
                                     Event event = ds.getValue(Event.class);
                                     eventsData.add(event);
                                 }
                             }

                             dataLoadedFlag = true;

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dataLoadedFlag = true;
                        }
                    });

                    while(!dataLoadedFlag){}

                }

            }

            @Override
            public void onDestroy() {
                if (eventsData != null) {
                    eventsData = null;
                }
            }

            @Override
            public int getCount() {
                return eventsData== null ? 0 : eventsData.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        eventsData == null ) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);



                views.setTextViewText(R.id.owner,Util.capitalizeWords(eventsData.get(position).getOwnerName()));
                views.setTextViewText(R.id.title,eventsData.get(position).getTitle());
                views.setTextViewText(R.id.location,eventsData.get(position).getLocation() );

                String dateString = Util.getDateFromTimeStamp(eventsData.get(position).getFromTime());
                String timeString = Util.getTimeFromTimeStamp(eventsData.get(position).getFromTime());

                views.setTextViewText(R.id.date,dateString);
                views.setTextViewText(R.id.time,timeString);
               /* (eventsData.get(position).getOwnerPhotoUrl() != null) {
                    views.setImageViewUri(R.id.messengerImageView,eventsData.get(position).getOwnerPhotoUrl());
                }else{
                    views.setImageViewResource(R.id.messengerImageView,R.drawable.ic_account_circle_black_48dp);
                }*/
                int count=0;
                for(String s:eventsData.get(position).getEventMembers().values()){
                    if (s!=null) {
                        String[] sa = s.split("-");
                        if (sa[1].equals("1"))
                            count++;
                    }
                }
                views.setTextViewText(R.id.people_going,"+"+count+" "+getString(R.string.event_member_going));
                if (eventsData.get(position).getLocation().equals("")){
                    if (count>1)
                        views.setContentDescription(R.id.widget_list_item,getString(R.string.event_list_item_content_description2,eventsData.get(position).getOwnerName(),eventsData.get(position).getTitle(),dateString,timeString,count));
                    else
                        views.setContentDescription(R.id.widget_list_item,getString(R.string.event_list_item_content_description1,eventsData.get(position).getOwnerName(),eventsData.get(position).getTitle(),dateString,timeString,count));
                }else {
                    if (count>1)
                        views.setContentDescription(R.id.widget_list_item,getString(R.string.event_list_item_content_description2l,eventsData.get(position).getOwnerName(),eventsData.get(position).getTitle(),dateString,timeString,eventsData.get(position).getLocation(),count));
                    else
                        views.setContentDescription(R.id.widget_list_item,getString(R.string.event_list_item_content_description1l,eventsData.get(position).getOwnerName(),eventsData.get(position).getTitle(),dateString,timeString,eventsData.get(position).getLocation(),count));
                }


                final Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);


                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {

                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }


}

