package com.example.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongsFragment extends Fragment implements MusicAdapterRecyclerView.OnItemClick {
    RecyclerView recyclerView;
    MusicAdapterRecyclerView adapter;
    Music music;
    public List<Music> musicList=new ArrayList<>();
    public OnMusicClick musicClick;

    public SongsFragment(OnMusicClick musicClick) {
        this.musicClick=musicClick;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        music=new Music(getContext());
        musicList=music.getAllMusics();

        recyclerView=view.findViewById(R.id.recycler_view);
        adapter=new MusicAdapterRecyclerView(getContext(),this);
        adapter.getMusics(musicList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(20);
    }


    //music has gotten from adapter to songs fragment
    @Override
    public void OnItemClickListener(Music music) {
        musicClick.OnMusicClickListener(music);

    }

    public interface OnMusicClick{
        void OnMusicClickListener(Music music);
    }

}
