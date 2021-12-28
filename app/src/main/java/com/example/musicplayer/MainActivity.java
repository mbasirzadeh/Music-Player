package com.example.musicplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.viewpager.widget.ViewPager;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity  implements SongsFragment.OnMusicClick {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    TextView txt_title,txt_artist,txt_dur,txt_whole_dur;
    ImageView img_play,img_cover,img_next,img_prev;
    MotionLayout motionLayout;
    AppCompatSeekBar seekBar;
    UpdateCoverThread updateCoverThread=new UpdateCoverThread(this);
    MediaPlayer mediaPlayer;
    Timer timer=new Timer();
    public static List<byte[]> covers=new ArrayList<>();

    //0=playing 1=pause
    int playInt=0;
    //0=onStopTrackingTouch 1=onStartTrackingTouch
    int seekBarState =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiViews();
        checkMyPermissions();

        //after request permissions
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new SongsFragment(this), "Songs");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);




        updateCoverThread.getMusicList(new Music(this).getAllMusics());
        updateCoverThread.start();


    }
    private void intiViews() {
        viewPager=findViewById(R.id.viewpager_main);
        tabLayout=findViewById(R.id.tabLayout);
        txt_artist=(TextView)findViewById(R.id.txt_artist);
        txt_title=(TextView)findViewById(R.id.txt_title);
        txt_dur=(TextView)findViewById(R.id.duration);
        txt_whole_dur=(TextView)findViewById(R.id.wholeDuration);
        img_cover=(ImageView)findViewById(R.id.img_cover);
        img_next=(ImageView)findViewById(R.id.next);
        img_prev=(ImageView)findViewById(R.id.prev);
        img_cover=(ImageView)findViewById(R.id.img_cover);
        img_play=(ImageView)findViewById(R.id.play);
        seekBar=(AppCompatSeekBar)findViewById(R.id.slider);
        motionLayout=(MotionLayout)findViewById(R.id.constraint);
    }


    //check permission
    private static final int REQUEST_PERMISSION = 11228;
    @SuppressLint("InlinedApi")
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_MEDIA_LOCATION};

    @SuppressLint("NewApi")
    public boolean isPerDenied() {
        for (String permission : PERMISSIONS) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (hasAllPermissionsGranted(grantResults)) {
                recreate();
            } else {
                ((ActivityManager) Objects.requireNonNull(this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
    public void checkMyPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isPerDenied()) {
            requestPermissions(PERMISSIONS, REQUEST_PERMISSION);
            return;
        }
    }
    //.

    //music has gotten from songsFragment
    @Override
    public void OnMusicClickListener(Music music) {
        onVoidMusic(music);
    }

    private void onVoidMusic(@NotNull Music music) {

        motionLayout.transitionToEnd();
        byte[] cover=MainActivity.covers.get(music.getId());
        if (cover==null){
            Glide.with(MainActivity.this).load(R.drawable.music_picture).override(2000).placeholder(R.drawable.black).useAnimationPool(true).into(img_cover);
        }else{
            Glide.with(MainActivity.this).load(cover).override(2000).placeholder(R.drawable.black).useAnimationPool(true).into(img_cover);
        }
        txt_title.setText(music.getName());
        txt_artist.setText(music.getArtistName());
        txt_whole_dur.setText(MusicAdapterRecyclerView.getMinSecDuration(music.getDuration()));
        Glide.with(MainActivity.this).load(R.drawable.pause_button).useAnimationPool(true).into(img_play);
        seekBar.setProgress(0);


        if (mediaPlayer!=null){
            timer.purge();
            mediaPlayer.release();
        }
        mediaPlayer= android.media.MediaPlayer.create(getApplicationContext(), Uri.parse(music.getPath()));
        mediaPlayer.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(android.media.MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        seekBar.setMax(mediaPlayer.getDuration());


        Handler handler=new Handler(Looper.getMainLooper());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (seekBarState!=1){
                            if (!seekBar.isFocusableInTouchMode()){
                                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            }

                            txt_dur.setText(MusicAdapterRecyclerView.getMinSecDuration(mediaPlayer.getCurrentPosition()+""));
                        }

                    }
                });
            }
        },0,1000);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txt_dur.setText(MusicAdapterRecyclerView.getMinSecDuration(progress+""));
                //change slider shape

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarState=1;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarState=0;
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (playInt){
                    case 0:
                        Glide.with(MainActivity.this).load(R.drawable.play).useAnimationPool(true).into(img_play);
                        mediaPlayer.pause();
                        playInt++;
                        break;
                    case 1:
                        Glide.with(MainActivity.this).load(R.drawable.pause_button).useAnimationPool(true).into(img_play);
                        mediaPlayer.start();
                        playInt--;
                        break;

                }
            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext(music);
            }
        });
        img_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPrev(music);
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVoidMusic(MusicAdapterRecyclerView.musicList.get(music.getId()+1));
            }
        });

    }
    private void goNext(@NotNull Music music) {
        int musicIndex=music.getId();
        onVoidMusic(MusicAdapterRecyclerView.musicList.get(musicIndex+1));
    }
    private void goPrev(@NotNull Music music) {
        int musicIndex=music.getId();
        onVoidMusic(MusicAdapterRecyclerView.musicList.get(musicIndex-1));
    }


    @Override
    public void onBackPressed() {
        if (motionLayout.getCurrentState()==motionLayout.getEndState()){
            motionLayout.transitionToStart();
        }else {
            super.onBackPressed();
        }
    }
}