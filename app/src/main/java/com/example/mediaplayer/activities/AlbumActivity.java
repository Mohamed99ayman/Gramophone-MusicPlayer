package com.example.mediaplayer.activities;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mediaplayer.R;
import com.example.mediaplayer.adapters.SongAlbumAdapter;
import interfaces.OnClickListen;
import static com.example.mediaplayer.activities.MainActivity.al;

public class AlbumActivity extends AppCompatActivity implements OnClickListen {


    protected ImageView imageView;
    protected int position;
    private TextView textView1,textView2;
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager mmanager;
    private LinearLayout linearLayout;
    static SongAlbumAdapter songalbumAdapter;
    private Palette.Swatch lightVibrantSwatch;
    private Palette.Swatch darkMutedSwatch;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        recyclerView = findViewById(R.id.album_recycler);
        imageView=findViewById(R.id.albumimage);
        linearLayout=findViewById(R.id.linear);
        recyclerView.setHasFixedSize(true);
        textView1=findViewById(R.id.text1);
        textView2=findViewById(R.id.text2);
        mmanager=new LinearLayoutManager(this);
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        position = bundle.getInt("index");
        songalbumAdapter = new SongAlbumAdapter(al.get(position),this);
        System.out.println(al.get(position).get(0).getArtist());
        textView1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView1.setText(al.get(position).get(0).getArtist());
        String size=(al.get(position).size())+"";
        textView2.setText(size);

        Glide.with(this)
                .load(ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"),al.get(position).get(0).getAlbumID()).toString())
                .thumbnail(0.2f)
                .centerCrop()
                .placeholder(R.drawable.track)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        setBack();
                        return false;
                    }
                })
                .into(imageView);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.line_divider);
        verticalDecoration.setDrawable(verticalDivider);
        recyclerView.addItemDecoration(verticalDecoration);

        recyclerView.setLayoutManager(mmanager);
        recyclerView.setAdapter(songalbumAdapter);


    }


    @Override
    public void onClick(int position) {
        Intent intent=new Intent(MainActivity.getInstance(), PlayerActivity.class).putExtra("index",position).putExtra("val",1).putExtra("from",true);
        startActivity(intent);
    }

    public void setBack(){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        Palette.from(bitmap).maximumColorCount(40).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                lightVibrantSwatch = palette.getLightVibrantSwatch();
                darkMutedSwatch = palette.getDarkMutedSwatch();
                if (lightVibrantSwatch != null) {
                    linearLayout.setBackgroundColor(lightVibrantSwatch.getRgb());

                } else {
                    linearLayout.setBackgroundColor(Color.WHITE);
                }
                if(darkMutedSwatch!=null){
                    textView1.setTextColor(darkMutedSwatch.getRgb());
                    textView2.setTextColor(darkMutedSwatch.getRgb());
                }else{
                    textView1.setTextColor(Color.BLACK);
                    textView2.setTextColor(Color.BLACK);
                }

            }
        });
    }

}
