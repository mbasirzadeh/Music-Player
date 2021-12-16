package com.example.musicplayer;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;

public class MusicAdapterRecyclerView extends RecyclerView.Adapter<MusicAdapterRecyclerView.MusicBinding> {
    public static List<Music> musicList = new ArrayList<>();
    private Context context;
    public static OnItemClick musicClick;
    public static int playingMusic;
    public MusicAdapterRecyclerView(Context context,OnItemClick musicClick) {
        this.context=context;
        this.musicClick=musicClick;
    }
    public void getMusics(List<Music> musicList) {
        this.musicList = musicList;
    }

//    public void updateCover(int i){
//        byte[] musicP =getCoverImage(musicList.get(i).getPath());
//        musicList.get(i).setCover(musicP);
//        notifyItemChanged(i);
//    }


    @NonNull
    @Override
    public MusicBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MusicBinding(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music, parent, false));
    }


    @Override
    public int getItemCount() {
        return musicList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MusicBinding holder, int position) {
        holder.title.setText(musicList.get(position).getName());
        holder.artist.setText(musicList.get(position).getArtistName());
        holder.duration.setText(getMinSecDuration(musicList.get(position).getDuration()));
        Glide.with(context).load(UpdateCoverThread.covers.get(position)).centerCrop().placeholder(R.drawable.music_picture).error(R.drawable.music_picture).dontAnimate().priority(Priority.LOW).into(holder.cover);
//        if (position==playingMusic){
//            holder.title.setTextColor(context.getResources().getColor(R.color.selectedItemColor));
//            holder.artist.setTextColor(context.getResources().getColor(R.color.selectedItemColor));
//        }else {
//            holder.title.setTextColor(context.getResources().getColor(R.color.white));
//            holder.artist.setTextColor(context.getResources().getColor(R.color.artist_item_gray));
//        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    public  class MusicBinding extends RecyclerView.ViewHolder {
        public ConstraintLayout mContainer;
        public TextView title;
        public TextView artist;
        public ImageView cover;
        public TextView duration;
        public MusicBinding(@NonNull View itemView) {
            super(itemView);
            mContainer=itemView.findViewById(R.id.constraint_item_music);
            title = itemView.findViewById(R.id.txt_title_sheet);
            artist = itemView.findViewById(R.id.txt_artist_sheet);
            cover = itemView.findViewById(R.id.imageView_sheet);
            duration=itemView.findViewById(R.id.txt_item_durtion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicClick.OnItemClickListener(musicList.get(getAdapterPosition()));
//                    title.setTextColor(context.getResources().getColor(R.color.selectedItemColor));
//                    artist.setTextColor(context.getResources().getColor(R.color.selectedItemColor));
                }
            });
        }

    }



//    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//    private byte[] getCoverImage(String path){
//        retriever.setDataSource(path);
//        byte[] musicP = retriever.getEmbeddedPicture();
//        return musicP;
//    }



    //get duration Min & Sec
    public static String getMinSecDuration(String duration){
        long milliseconds = Integer.parseInt(duration);
        long clock = (milliseconds / 1000) / 3600;
        long minutes = (milliseconds / 1000) / 60;
        long seconds = (milliseconds / 1000) % 60;
        int seconds1= (int) (seconds/10);
        String seconds2=seconds1+"";
        if (seconds2.startsWith("0")){
            return (minutes + ":0" + seconds);
        }
        return (minutes + ":" + seconds);
    }

    public interface OnItemClick{
        void OnItemClickListener(Music music);
    }

}
