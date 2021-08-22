package com.syinfotech.mplayer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.syinfotech.mplayer.Fragments.LocalFragment;
import com.syinfotech.mplayer.Model.Methods;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    private final List<String> folderlist;
    private final Context context;

    public FolderAdapter(List<String> folderlist, Context context) {
        this.folderlist = folderlist;
        this.context = context;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_list_indicator, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {

        if (folderlist != null) {
            if (folderlist.get(position) != null) {
                if (folderlist.get(position).equals("0")) {
                    holder.txtfoldername.setText(R.string.storage);
                } else {
                    holder.txtfoldername.setText(folderlist.get(position));
                }

            }
        }
        if (folderlist != null) {
            int count = Methods.getAudioCount(context, folderlist.get(position));
            holder.txtcount.setText(count +" Songs");
        }
        holder.layout.setOnClickListener(view -> {
            assert folderlist != null;
            List<Song> list = Methods.getAllSongsFromFolder(context,folderlist.get(position));
            LocalFragment.SetDataIntoSongsFolderlist(list);
        });


    }

    @Override
    public int getItemCount() {
        return folderlist.size();
    }

    public static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView txtcount, txtfoldername;
        RelativeLayout layout;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtcount = itemView.findViewById(R.id.folder_song_count);
            txtfoldername = itemView.findViewById(R.id.folder_title);
            layout = itemView.findViewById(R.id.folder_layout);
        }
    }
}
