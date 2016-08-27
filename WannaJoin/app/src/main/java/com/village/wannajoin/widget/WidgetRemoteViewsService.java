package com.village.wannajoin.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.village.wannajoin.R;
import com.village.wannajoin.model.Event;
import com.village.wannajoin.util.Constants;
import com.village.wannajoin.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                    Query dbRef = FirebaseDatabase.getInstance().getReference().child(Constants.FIREBASE_LOCATION_EVENTS).orderByChild("eventMembers/" + currentUserId).startAt(Util.getMidNightTimeStamp()).endAt(Util.getMidNightTimeStampNextDay());
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

                views.setTextViewText(R.id.date,Util.getDateFromTimeStamp(eventsData.get(position).getFromTime()));
                views.setTextViewText(R.id.time,Util.getTimeFromTimeStamp(eventsData.get(position).getFromTime()));
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
                views.setTextViewText(R.id.people_going,"+"+count+" going");


                final Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);


                return views;
            }

            private void setRemoteContentDescription(RemoteViews views, int viewId, String description) {
                views.setContentDescription(viewId, description);
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

