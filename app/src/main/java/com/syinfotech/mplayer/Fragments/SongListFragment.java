package com.syinfotech.mplayer.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syinfotech.mplayer.Adapter.SongAdapter;
import com.syinfotech.mplayer.Model.Methods;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;

import java.util.List;


public class SongListFragment extends Fragment {
private Context context;
private List<Song> songList;
private RecyclerView AllSongRecyclerView;

    public SongListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.fragment_song_list, container, false);
       AllSongRecyclerView = view.findViewById(R.id.allsongrecyclerview);
       AllSongRecyclerView.setHasFixedSize(true);
       AllSongRecyclerView.setLayoutManager(new LinearLayoutManager(context));
       songList = Methods.getAllSongsFromDevice(context);
       SetSongsToRecyclerView(songList);
        return view;
    }

    private void SetSongsToRecyclerView(List<Song> songList) {
        if (songList!=null){
            AllSongRecyclerView.setAdapter(new SongAdapter(context,songList));
        }
    }
}