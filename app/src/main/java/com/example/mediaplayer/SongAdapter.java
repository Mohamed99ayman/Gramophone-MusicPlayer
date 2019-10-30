package com.example.mediaplayer;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    MediaMetadataRetriever metaRetriver;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_layout,viewGroup,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    Song song= songs.get(i);
    viewHolder.textview1.setText(song.getName());
        viewHolder.textView2.setText(song.getArtist());
        metaRetriver = new MediaMetadataRetriever();
        try {
            metaRetriver.setDataSource(song.getPath());
            Glide
                    .with(context)
                    .load(metaRetriver.getEmbeddedPicture())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.track)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .thumbnail(0.5f)
                    .transition(new DrawableTransitionOptions()
                            .crossFade()
                    )
                    .into(viewHolder.mImageView);
            return;
        }catch (Exception e){
            Glide.with(context).load(R.drawable.track).into(viewHolder.mImageView);
        }

    }
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
    public ImageView mImageView;
    public TextView textview1;
    public TextView textView2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            textview1=itemView.findViewById(R.id.textViewSongTitle);
            textView2=itemView.findViewById(R.id.textViewArtistName);
        }
    }



    Activity context;
    ArrayList<Song>songs;
    private static LayoutInflater inflater=null;

    public SongAdapter(Activity context, ArrayList<Song> song) {
        this.context = context;
        this.songs = song;
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


}
