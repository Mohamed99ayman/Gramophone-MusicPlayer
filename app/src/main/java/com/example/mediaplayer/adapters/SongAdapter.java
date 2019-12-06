package com.example.mediaplayer.adapters;
import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Color;
import android.graphics.Typeface;
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

import interfaces.OnClickListen;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements Filterable {
       protected static Typeface myfont;
       protected OnClickListen monclicklisten;
    private Activity context;
    public static ArrayList<Song>songs;
    private ArrayList<Song>allSongs;
    private static LayoutInflater inflater;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_view_layout,viewGroup,false);
        ViewHolder vh=new ViewHolder(v,monclicklisten);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if(i==0){
            viewHolder.textview1.setText("Shuffle All");
            viewHolder.textView2.setText("");

            viewHolder.textview1.setTextSize(25);
            viewHolder.textview1.setTextColor(Color.parseColor("#FF1105"));
            Glide
                    .with(context)
                    .load(R.drawable.ic_shuffle_black_24dp)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                    )
                    .thumbnail(0.1f)
                    .transition(new DrawableTransitionOptions()
                            .crossFade()
                    )
                    .into(viewHolder.mImageView);

        }else {
            Song song = songs.get(i);
            viewHolder.textview1.setX(viewHolder.textview1.getX());
            viewHolder.textview1.setY(viewHolder.textview1.getY());
            viewHolder.textview1.setTextSize(18);
            viewHolder.textview1.setText(song.getName());
            viewHolder.textView2.setText(song.getArtist());
            viewHolder.textView2.setX(viewHolder.textView2.getX());
            viewHolder.textview1.setTextColor(Color.parseColor("#E7DBDB"));
            try {

                Glide
                        .with(context)
                        .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), song.getAlbumID()).toString())
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
            } catch (Exception e) {
            }
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
    songs.add(new Song("shufflee"));
    songs.addAll((List)results.values);
    notifyDataSetChanged();
    }
};

}
