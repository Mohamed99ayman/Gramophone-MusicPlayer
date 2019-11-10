package com.example.mediaplayer.models;

import android.graphics.Bitmap;

public class Song implements Comparable<Song> {


    private String name ,album, artist,path,AlbumArt;
    long albumID;

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    public int getIm() {
        return im;
    }

    public void setIm(int im) {
        this.im = im;
    }

    private int im;
    Bitmap songImage;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAlbumArt() {
        return AlbumArt;
    }

    public void setAlbumArt(String albumArt) {
        AlbumArt = albumArt;
    }

    public Bitmap getSongImage() {
        return songImage;
    }

    public void setSongImage(Bitmap songImage) {
        this.songImage = songImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
    public void setSongImage(){

    }

    public Song(){

    }
    public Song(String name, String album, String artist, String genre, int index) {
        this.name = name;
        this.album = album;
        this.artist = artist;

    }
    @Override
    public int compareTo(Song o) {
        return getName().compareTo(o.getName());
    }
}
