package com.syinfotech.mplayer.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
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
import com.squareup.picasso.Picasso;
import com.syinfotech.mplayer.DataBase.PlayistDataBase;
import com.syinfotech.mplayer.UI.MainActivity;
import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    private final Context context;
    private final List<Song> list;

    public SongAdapter(Context context, List<Song> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_list_indicator, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        if (list != null) {
            if (list.get(position).getTitle() != null) {
                holder.txtsongname.setText(list.get(position).getTitle());
            }
            else {
                holder.txtsongname.setText("N/A");
            }
        }
        holder.layout.setOnClickListener(view ->
        {
            if (list != null) {
                MainActivity.onPlaySetData(list, position);
            }

        });
        holder.layout.setOnLongClickListener(view -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogBar);
            @SuppressLint("InflateParams") View BottomsheetView = LayoutInflater.from(context).inflate(R.layout.song_menubar,null);
            LinearLayout AddToPlayist,Delete,Rename,Property,Share;
            AddToPlayist = BottomsheetView.findViewById(R.id.menu_add_to_playist);
            Delete = BottomsheetView.findViewById(R.id.menu_delete);
            Rename = BottomsheetView.findViewById(R.id.menu_rename);
            Property = BottomsheetView.findViewById(R.id.menu_Details);
            Share = BottomsheetView.findViewById(R.id.menu_share);
            ImageView Down = BottomsheetView.findViewById(R.id.menu_down_arrow);
            Down.setOnClickListener(view1 -> bottomSheetDialog.dismiss());
            AddToPlayist.setOnClickListener(view13 -> {
                AddToPlayist(list, position);
                bottomSheetDialog.dismiss();
            });
            Delete.setOnClickListener(view12 -> {
                Delete(position);
                bottomSheetDialog.dismiss();
            });
            Rename.setOnClickListener(view16 -> {
                Rename(position);
                bottomSheetDialog.dismiss();
            });
            Property.setOnClickListener(view15 -> {
                Property(position);
                bottomSheetDialog.dismiss();
            });
            Share.setOnClickListener(view14 -> {
                Share(position);
                bottomSheetDialog.dismiss();
            });
            bottomSheetDialog.setContentView(BottomsheetView);
            bottomSheetDialog.show();
             return true;
        });

    }

    private void Share(int position) {
        Uri uri = Uri.fromFile(new File(list.get(position).getPath()));
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("audio/*");
        i.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(i, "share"));
    }

    private void Property(int position) {
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

    private void Delete(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete")
                .setMessage(list.get(position).getTitle())
                .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss()).setPositiveButton("Delete", (dialog, which) -> {
            File file = new File(list.get(position).getPath());
            boolean deleted = file.delete();
            if (deleted) {
                Uri uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.parseLong(list.get(position).getId()));
                context.getApplicationContext().getContentResolver().delete(uri, null, null);
                list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeRemoved(position, list.size());
                ShowToast("File Deleted SuccessFully");
            } else {
                ShowToast("Failed To Delete ");

            }

        }).show();
    }

    private void AddToPlayist(List<Song> list, int position) {
        new PlayistDataBase(context).AddToPlayist(position,list);
        ShowToast("Added To Playist");
    }
    private void ShowToast(String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView txtsongname;
        ImageView Songimg;
        RelativeLayout layout;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            txtsongname = itemView.findViewById(R.id.songlisttitle);
            layout = itemView.findViewById(R.id.song_list_layout);
            Songimg = itemView.findViewById(R.id.songimg);
        }
    }
}
