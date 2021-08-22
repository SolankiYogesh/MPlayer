package com.syinfotech.mplayer.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.syinfotech.mplayer.DataBase.PlayistDataBase;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;
import com.syinfotech.mplayer.UI.MainActivity;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class PlayistAdapter extends RecyclerView.Adapter<PlayistAdapter.PlayistViewHolder> {
    private final Context context;
    private  List<Song> list;

    public PlayistAdapter(Context context, List<Song> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PlayistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlayistViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_indicator, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlayistViewHolder holder, int position) {

        if (list != null) {
            if (list.get(position).getTitle() != null) {
                holder.txtsongname.setText(list.get(position).getTitle());
            }
        }
        holder.layout.setOnClickListener(view -> MainActivity.onPlaySetData(list,position));
        holder.layout.setOnLongClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogBar);
            @SuppressLint("InflateParams") View BottomsheetView = LayoutInflater.from(context).inflate(R.layout.playsit_menubar,null);
            LinearLayout RemoveFromPlayist,Rename,Property,Share;
            RemoveFromPlayist = BottomsheetView.findViewById(R.id.playist_remove_from);
            Rename = BottomsheetView.findViewById(R.id.playist_menu_rename);
            Property = BottomsheetView.findViewById(R.id.playist_menu_Details);
            Share = BottomsheetView.findViewById(R.id.playist_menu_share);
            ImageView Down = BottomsheetView.findViewById(R.id.playist_menu_down);
            Down.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            RemoveFromPlayist.setOnClickListener(view12 -> {
                RemoveFromPlayist(list.get(position).getId(),position);
                bottomSheetDialog.dismiss();
            });
            Rename.setOnClickListener(view13 -> {
                Rename(position);
                bottomSheetDialog.dismiss();
            });
            Property.setOnClickListener(view14 -> {
                Details(position);
                bottomSheetDialog.dismiss();
            });
            Share.setOnClickListener(view15 -> {
                Share(position);
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.setContentView(BottomsheetView);
            bottomSheetDialog.show();
            return true;
        });
    }

    private void Details(int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.menubar_property);
        String name = list.get(position).getTitle();
        String Path = list.get(position).getPath();
        String Size = list.get(position).getSize();
        String Duration = list.get(position).getDuration();

        TextView aname = dialog.findViewById(R.id.pro_title);
        TextView apath = dialog.findViewById(R.id.pro_storage);
        TextView asize = dialog.findViewById(R.id.pro_size);
        TextView aDuration = dialog.findViewById(R.id.pro_duration);
        aname.setText(name);
        apath.setText(Path);
        asize.setText(Size);
        aDuration.setText(Duration);
        dialog.show();

    }
    private void Rename(int position) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.rename_layout);
        EditText editText = dialog.findViewById(R.id.rename_edit);
        Button Cancel = dialog.findViewById(R.id.cancel_rename_btn);
        Button Rename = dialog.findViewById(R.id.Rename_rename_btn);
        final File f = new File(list.get(position).getPath());
        String TempText = f.getName();
        TempText = TempText.substring(0, TempText.lastIndexOf("."));
        editText.setText(TempText);
        editText.clearFocus();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        Cancel.setOnClickListener(v -> dialog.dismiss());
        Rename.setOnClickListener(v -> {
            String onlyPath = Objects.requireNonNull(f.getParentFile()).getAbsolutePath();
            String Extrathings = f.getAbsolutePath();
            Extrathings = Extrathings.substring(Extrathings.lastIndexOf("."));
            String newPath = onlyPath + "/" + editText.getText() + Extrathings;
            File newFile = new File(newPath);
            boolean RenamedComplete = f.renameTo(newFile);
            if (RenamedComplete) {
                context.getApplicationContext().getContentResolver().delete(MediaStore.Files.getContentUri("external"), MediaStore.MediaColumns.DATA + "=?", new String[]{
                        f.getAbsolutePath()
                });
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(newFile));
                context.getApplicationContext().sendBroadcast(intent);
                ShowToast("File Renamed SuccessFully");
                notifyItemChanged(position);
                notifyItemRangeChanged(position,list.size());

            } else {
                ShowToast("Failed to Rename File");
            }
            dialog.dismiss();
        });
        dialog.show();
    }

    private void Share(int position) {
        Uri uri = Uri.fromFile(new File(list.get(position).getPath()));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("audio/*");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(i, "share"));
    }

    private void RemoveFromPlayist(String id,int position) {

        new PlayistDataBase(context).RemoveSongFromPlayist(id);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,list.size());
        ShowToast("Song Removed From Playist SuccessFully!");

    }
   private void ShowToast(String text){
       Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PlayistViewHolder extends RecyclerView.ViewHolder {
        TextView txtsongname;

        ImageView imgSong;
        RelativeLayout layout;

        public PlayistViewHolder(@NonNull View itemView) {
            super(itemView);
            txtsongname = itemView.findViewById(R.id.songlisttitle);
            imgSong = itemView.findViewById(R.id.songimg);
            layout = itemView.findViewById(R.id.song_list_layout);
        }
    }
}
