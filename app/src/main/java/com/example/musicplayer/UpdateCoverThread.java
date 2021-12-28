package com.example.musicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class  UpdateCoverThread extends Thread{

    public Context context;
    public  List<Music> musicList=new ArrayList<>();
    public MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public UpdateCoverThread(Context context){
        this.context=context;
    }

    public void getMusicList(List<Music> musicList){
        this.musicList =musicList;
    }

    @Override
    public void run() {

        for (int i = 0; i <musicList.size() ; i++) {
            String path=musicList.get(i).getPath();
            if (path!=null){
                try {
                    retriever.setDataSource(path);
                    byte[] musicP = retriever.getEmbeddedPicture();
                    MainActivity.covers.add(musicP);
                }catch (Exception e){
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.music_picture);
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
                    byte[] b=baos.toByteArray();
                    MainActivity.covers.add(b);
                }

            }

        }
        retriever.release();

    }



//    private byte[] getCoverImage(String path){
//        if (path!=null){
//            retriever.setDataSource(path);
//            byte[] musicP = retriever.getEmbeddedPicture();
//            retriever.release();
//            return musicP;
//        }
//        return null;
//    }

}
