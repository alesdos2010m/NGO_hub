package com.example.ngohub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.EventRegistration;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventPostsAdapter extends RecyclerView.Adapter<EventPostsAdapter.PostsViewHolder> {

    private Context mctx;
    private List<NGO_EventPosts> eventPostsList;
    private ArrayList<String> keys;

    public EventPostsAdapter(Context mctx, List<NGO_EventPosts> eventPostsList, ArrayList<String> keys) {
        this.mctx = mctx;
        this.eventPostsList = eventPostsList;
        this.keys = keys;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.event_posts_list_view_layout,parent, false);
        //Register Textview should be invisible to Vo/Ngo
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            TextView EventRegistration =view.findViewById(R.id.event_register);
            EventRegistration.setVisibility(View.GONE);
        }
        return new PostsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {

        NGO_EventPosts ngo_eventPosts = eventPostsList.get(position);
        holder.eventTitle.setText(ngo_eventPosts.getTitle());
        holder.eventCaption.setText(ngo_eventPosts.getCaption());
        holder.eventVenue.setText(ngo_eventPosts.getVenue());
        Glide.with(mctx)
                .load(ngo_eventPosts.getImageAddress())
                .into(holder.eventPostImage);
    }
    @Override
    public int getItemCount() {
        return eventPostsList.size();
    }

    //********************View Holder****************************//

    class PostsViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventCaption, eventVenue;
        ImageView eventPostImage;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);

            eventPostImage = itemView.findViewById(R.id.event_post_image);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventCaption = itemView.findViewById(R.id.event_caption);
            eventVenue = itemView.findViewById(R.id.event_venue);
        }
    }
}