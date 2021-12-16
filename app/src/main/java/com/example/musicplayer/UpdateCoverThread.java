package com.example.musicplayer;

import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

class UpdateCoverThread extends Thread{
    public static List<Music> musicList=new ArrayList<>();
    public static List<byte[]> covers=new ArrayList<>();

    public void getMusicList(List<Music> musicList){
        UpdateCoverThread.musicList =musicList;
    }

    @Override
    public void run() {

        super.run();
        for (int i = 0; i <musicList.size() ; i++) {
            covers.add(getCoverImage(musicList.get(i).getPath()));
        }
        retriever.release();

    }

    public MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    private byte[] getCoverImage(String path){
        retriever.setDataSource(path);
        byte[] musicP = retriever.getEmbeddedPicture();
        return musicP;
    }

}