package com.example.mediaplayer.adapters;
import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.mediaplayer.R;
import com.example.mediaplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

import Interfaces.OnClickListen;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {
    MediaMetadataRetriever metaRetriver;
       static Typeface myfont;
       private OnClickListen monclicklisten;
        byte art[];

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_layout,viewGroup,false);
        ViewHolder vh=new ViewHolder(v,monclicklisten);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song= songs.get(i);
        viewHolder.textview1.setText(song.getName());
            viewHolder.textView2.setText(song.getArtist());
        try {

            Glide
                    .with(context)
                    .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),song.getAlbumID()).toString())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.track_2_min)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .thumbnail(0.1f)
                    .transition(new DrawableTransitionOptions()
                            .crossFade()
                    )
                    .into(viewHolder.mImageView);
            return;
        }catch (Exception e){
        }

    }
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ImageView mImageView;
    public TextView textview1;
    public TextView textView2;
    OnClickListen onClickListen;
        public ViewHolder(@NonNull View itemView,OnClickListen onClickListen) {
            super(itemView);
            mImageView=itemView.findViewById(R.id.imageView);
            textview1=itemView.findViewById(R.id.textViewSongTitle);
            textview1.setTypeface(myfont);
            textView2=itemView.findViewById(R.id.textViewArtistName);
            this.onClickListen=onClickListen;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        onClickListen.onClick(getAdapterPosition());
        }
    }



    Activity context;
   public static ArrayList<Song>songs;
    ArrayList<Song>allSongs;
    private static LayoutInflater inflater=null;

    public Song getSong(int position){
        return allSongs.get(position);
    }

        public SongAdapter(Activity context, ArrayList<Song> song,OnClickListen onClickListen) {
        this.context = context;
        this.songs = song;
        allSongs=new ArrayList<>(song);
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.monclicklisten=onClickListen;

    }
@Override
    public Filter getFilter(){
    return filter;
}
private Filter  filter=new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        List<Song>filteredList=new ArrayList<>();
        if(constraint==null||constraint.length()==0){
            filteredList.addAll(allSongs);
        }else{
            String filterpattern=constraint.toString().toLowerCase().trim();

            for (Song oneSong:allSongs){
                if(oneSong.getName().toLowerCase().startsWith(filterpattern)||oneSong.getArtist().toLowerCase().startsWith(filterpattern)){
                    filteredList.add(oneSong);
                }
            }

        }
        FilterResults filterResults=new FilterResults();
        filterResults.values=filteredList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    songs.clear();
    songs.addAll((List)results.values);
    notifyDataSetChanged();
    }
};

}
