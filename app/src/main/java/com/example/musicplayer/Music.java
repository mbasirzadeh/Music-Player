package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Music {

    private int id;
    private String path;
    private String name;
    private String artistName;
    private String album;
    private boolean favorite;
    private String duration;
    private Context context;


    public Music(int id,String path, String name, String artistName, String album, String duration, boolean favorite) {
        this.path = path;
        this.name = name;
        this.artistName = artistName;
        this.album = album;
        this.duration=duration;
        this.favorite = favorite;
        this.id=id;
    }

    public Music(Context context) {
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public byte[] getCover() {
//        return cover;
//    }
//
//    public void setCover(byte[] cover) {
//        this.cover = cover;
//    }
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    private Music music0;


    // get musics with content provider MediaStore in a method
    public List<Music> getAllMusics() {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = new String[]{
                MediaStore.Audio.Media.DATA,//for path
                MediaStore.Audio.Media.TITLE,

                MediaStore.Audio.Media.ARTIST,

                MediaStore.Audio.Media.ALBUM,

                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.SIZE,

        };
        List<Music> musicList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int i=-1;
            while (cursor.moveToNext()) {
                i++;
                String path = cursor.getString(0);
                String name = cursor.getString(1);
                String artistName = cursor.getString(2);
                int resArtistCover;
                String album = cursor.getString(3);
                String duration = cursor.getString(4);

                String albumCover;

                musicList.add(new Music(i,path,name,artistName, album, duration, false));
            }
        }
        return musicList;
    }
}