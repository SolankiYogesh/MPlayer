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
import android.widget.TextView;

import com.syinfotech.mplayer.Adapter.PlayistAdapter;
import com.syinfotech.mplayer.Adapter.SongAdapter;
import com.syinfotech.mplayer.DataBase.PlayistDataBase;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;

import java.util.List;


public class PlayistFragment extends Fragment {
private List<Song> list;
private Context context;
private RecyclerView recyclerView;

    public PlayistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_playist, container, false);
        recyclerView=view.findViewById(R.id.playsirecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        list=new PlayistDataBase(context).getAllPlayistSongs();
        if (list.size()!=0){
            SetDataOfPlayist(list);
            view.findViewById(R.id.txtnosong).setVisibility(View.GONE);
        }
        else {
            view.findViewById(R.id.txtnosong).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        return view ;
    }

    private void SetDataOfPlayist(List<Song> list) {

        recyclerView.setAdapter(new PlayistAdapter(context,list));

    }
}