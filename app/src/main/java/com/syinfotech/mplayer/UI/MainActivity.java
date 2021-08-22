package com.syinfotech.mplayer.UI;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.syinfotech.mplayer.Adapter.PagerAdapter;
import com.syinfotech.mplayer.Fragments.LocalFragment;
import com.syinfotech.mplayer.Model.Methods;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.Notification.CreateNotification;
import com.syinfotech.mplayer.R;
import com.syinfotech.mplayer.Services.OnClearFromRecentService;

import java.io.File;
import java.util.List;
import java.util.Random;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity {
    //declaring viewpager variables
    private ViewPager viewPager;
    //declaring medaiPlayer variables
    public static int pos;
    public static NotificationManager notificationManager;
    public static MediaPlayer mediaPlayer;
    public static ImageView Next, Play, Previus, Repeat, Shuffle;
    public static TextView Title, StartDuration, EndDuration, Artist;
    public static ImageView AlbumArtImg;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private DrawerLayout drawerLayout;
    public static SeekBar seekBar;
    public static Handler handler;
    public static Random rand;
    public static boolean isShuffle = false;
    private static List<Song> songList;
    public static Context context;
    public static ImageView layout_PlayPause;
    public static Button backtofolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         backtofolder = findViewById(R.id.toolbar_backtofolder);
        backtofolder.setOnClickListener(view1 -> {
            LocalFragment.folderRecyclerView.setVisibility(View.VISIBLE);
            LocalFragment.folderSongRecyclerView.setVisibility(View.GONE);
            backtofolder.setVisibility(View.GONE);
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menu = findViewById(R.id.toolbar_menu_img);
        menu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        //intializing view pager Variables
        //declaring viewpager variables
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);
        //Adding Tabs To View Pager
        tabLayout.addTab(tabLayout.newTab().setText("Songs"));
        tabLayout.addTab(tabLayout.newTab().setText("Local"));
        tabLayout.addTab(tabLayout.newTab().setText("Favorite"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MainActivity.context = getApplicationContext();
        //Declaring All Variables of Music_Ui Buttons
        Next = findViewById(R.id.buttonNext);
        Play = findViewById(R.id.buttonPlay);
        Previus = findViewById(R.id.buttonPrevius);
        Repeat = findViewById(R.id.buttonRepeat);
        Shuffle = findViewById(R.id.buttonShuffle);
        layout_PlayPause = findViewById(R.id.button_layout_Play);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();

        }
        registerReceiver(broadcastReceiver, new IntentFilter("TRACKS_TRACKS"));
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        handler = new Handler();
        //Declaring All Variables of Music_Ui TextViews
        Title = findViewById(R.id.playedsongtitle);
        StartDuration = findViewById(R.id.start_time_count);
        EndDuration = findViewById(R.id.end_time_count);
        Artist = findViewById(R.id.song_artist);

        //Declaring Seekbar of Music_Ui
        seekBar = findViewById(R.id.playerSeekbar);

        //intializinf Media Player
        mediaPlayer = new MediaPlayer();

        //Declaring AlbumArt Image
        AlbumArtImg = findViewById(R.id.imageAlbumArt);

        //Setting All View Pager Layout
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("actionname");

            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    Previus();
                    break;
                case CreateNotification.ACTION_PLAY:
                    Play();
                    break;
                case CreateNotification.ACTION_NEXT:
                    Next();
                    break;
            }
        }
    };


    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "KOD Dev", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    private static void Previus() {
        pos = ((pos - 1) < 0) ? (songList.size() - 1) : (pos - 1);
        onPlaySetData(songList, pos);
    }

    private static void Play() {
        if (mediaPlayer.isPlaying()) {
            CreateNotification.createNotification(context,songList,R.drawable.ic_play,pos,false);
            mediaPlayer.pause();
            Play.setImageResource(R.drawable.ic_play);
            layout_PlayPause.setImageResource(R.drawable.ic_play);
        } else {
            CreateNotification.createNotification(context,songList,R.drawable.ic_pause,pos,true);
            mediaPlayer.start();
            Play.setImageResource(R.drawable.ic_pause);
            layout_PlayPause.setImageResource(R.drawable.ic_pause);
            playCycle();
        }
    }

    private static void Next() {
        if (pos == songList.size()) {
            pos = 0;
        } else {
            pos++;
            pos %= songList.size();
        }
        onPlaySetData(songList, pos);
    }


    public static void onPlaySetData(List<Song> list, int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        pos = position;
        songList = list;
        Title.setText(list.get(pos).getTitle());
        Artist.setText(list.get(pos).getArtist());
        AlbumArtImg.setImageResource(R.drawable.albumart);
        File f = new File(list.get(pos).getPath());
        Uri u = Uri.fromFile(f);
        mediaPlayer = MediaPlayer.create(getAppContext(), u);
        mediaPlayer.start();
        Play.setImageResource(R.drawable.ic_pause);
        layout_PlayPause.setImageResource(R.drawable.ic_pause);
        mediaPlayer.setScreenOnWhilePlaying(true);
        CreateNotification.createNotification(context,songList,R.drawable.ic_pause,pos,true);
        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            if (!isShuffle) {
                Next();
            } else {
                pos = rand.nextInt((songList.size() - 1) + 1);
                onPlaySetData(songList, pos);
            }
        });
        playCycle();
        seekBar.setMax(mediaPlayer.getDuration());
        EndDuration.setText(Methods.getTimeFormatted((long) mediaPlayer.getDuration()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    StartDuration.setText(Methods.getTimeFormatted((long) i));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //setonlclickLinsternes to Buttons
        Next.setOnClickListener(view -> Next());
        Play.setOnClickListener(view -> Play());
        Previus.setOnClickListener(view -> Previus());
        Repeat.setOnClickListener(view -> Repeat());
        layout_PlayPause.setOnClickListener(view -> Play());
        Shuffle.setOnClickListener(view -> Shuffle());
    }

    private static void Shuffle() {
        rand = new Random();
        if (isShuffle) {
            isShuffle = false;
            Shuffle.setBackgroundColor(Color.parseColor("#000"));
            Toast.makeText(getAppContext(), "Shuffle off", Toast.LENGTH_SHORT).show();
        } else {
            isShuffle = true;
            Shuffle.setImageResource(R.drawable.ic_shuffle);
            Toast.makeText(getAppContext(), "Shuffle on", Toast.LENGTH_SHORT).show();
        }
    }

    private static void Repeat() {
        if (mediaPlayer.isLooping()) {
            mediaPlayer.setLooping(false);
            Repeat.setImageResource(R.drawable.ic_repeat);
            Toast.makeText(getAppContext(), "Repeat All Songs", Toast.LENGTH_SHORT).show();
        } else {
            mediaPlayer.setLooping(true);
            Repeat.setImageResource(R.drawable.ic_repeat_one);
            Toast.makeText(getAppContext(), "Repeat Current Song", Toast.LENGTH_SHORT).show();

        }
    }

    private static void playCycle() {
        try {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            StartDuration.setText(Methods.getTimeFormatted((long) mediaPlayer.getCurrentPosition()));
            if (mediaPlayer.isPlaying()) {
                Runnable runnable = MainActivity::playCycle;
                handler.postDelayed(runnable, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }

        unregisterReceiver(broadcastReceiver);

    }

    public static Context getAppContext() {
        return MainActivity.context;
    }
}