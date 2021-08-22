package com.syinfotech.mplayer.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syinfotech.mplayer.Adapter.FolderAdapter;
import com.syinfotech.mplayer.Adapter.SongAdapter;
import com.syinfotech.mplayer.Model.Methods;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;
import com.syinfotech.mplayer.UI.MainActivity;

import java.util.List;

@SuppressLint("StaticFieldLeak")
public class LocalFragment extends Fragment {

private static Context context;
public static RecyclerView folderRecyclerView;
public static RecyclerView folderSongRecyclerView;
    public LocalFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocalFragment.context =context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_local, container, false);
        folderRecyclerView = view.findViewById(R.id.folderrecyclerview);
        folderSongRecyclerView = view.findViewById(R.id.foldersongRecyclerview);
        folderSongRecyclerView.setHasFixedSize(true);
        folderSongRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        folderRecyclerView.setHasFixedSize(true);
        List<String> folderlist = Methods.getAllFolderList(context);
        SetDataIntoRecycerView(folderlist);
        return view;
    }
    public static void SetDataIntoSongsFolderlist(List<Song> list){
        if (list!=null){
            MainActivity.backtofolder.setVisibility(View.VISIBLE);
            folderRecyclerView.setVisibility(View.GONE);
            folderSongRecyclerView.setVisibility(View.VISIBLE);
            folderSongRecyclerView.setAdapter(new SongAdapter(context,list));
        }
    }

    private void SetDataIntoRecycerView(List<String> folderlist) {
        if (folderlist!=null){
            folderRecyclerView.setAdapter(new FolderAdapter(folderlist,context));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}