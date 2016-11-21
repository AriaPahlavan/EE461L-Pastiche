package com.pastiche.pastiche.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.pastiche.pastiche.PObject.PEvent;
import com.pastiche.pastiche.PObject.PPhoto;
import com.pastiche.pastiche.R;
import com.pastiche.pastiche.Server.ServerHandler;
import com.pastiche.pastiche.Server.ServerRequestHandler;
import com.pastiche.pastiche.viewHolder.EventListViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Aria Pahlavan on 11/5/16.
 */

public class EventlistAdapter extends RecyclerView.Adapter<EventListViewHolder> {
    private static final String TAG = "EventListAdapter";

    private final Context appContext;

    private List<PEvent> events;
    private Map<Integer, PPhoto> eventFirstPictures;

    /**
     * get resources (should be an array of event IDs)
     *
     * @param context
     */
    public EventlistAdapter(Context context) {

        appContext = context;

        ServerHandler handler = ServerHandler.getInstance(appContext);
        events = new ArrayList<>(100);


        handler.listEvents(

                data -> loadListEvents(data),

                error -> Log.e(TAG, error)
        );
    }

    private void loadListEvents(PEvent[] data) {
        ServerHandler handler = ServerHandler.getInstance(appContext);

        events.clear();
        Collections.addAll(events, data);
        this.notifyDataSetChanged(); //TODO remove?

        eventFirstPictures = new ConcurrentHashMap<>();

        events.parallelStream().forEach(event -> handler.listPhotosForAnEvent(event.getEventId(), listPhotos -> {
            if ( listPhotos.length > 0 ) {
                eventFirstPictures.put(event.getEventId(), listPhotos[listPhotos.length - 1]);//TODO get the most seen photo
            }

            this.notifyDataSetChanged();
        }, error -> Log.e(TAG, error)));
    }


    @Override
    public EventListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventListViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }


    /**
     * assemble the view HOLDER for given POSITION
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(EventListViewHolder holder, int position) {
//        holder.setImg_event_item(mEventPictures[position % mEventPictures.length]);
        String internetUrl = ServerRequestHandler.baseURL + "/photos/";

        PEvent event = events.get(position);

        if ( eventFirstPictures != null && eventFirstPictures.containsKey(event.getEventId()) ) {

            String url = internetUrl + eventFirstPictures.get(event.getEventId()).getId();
            Glide.with(appContext).load(url).into(holder.getImg_event_item());
        }
        else {
            holder.setImg_event_item(appContext.getDrawable(R.drawable.empty_photo));
        }

        holder.setTxt_event_item_title(event.getName());
    }


    @Override
    public int getItemCount() {
        return events.size();
    }
}
