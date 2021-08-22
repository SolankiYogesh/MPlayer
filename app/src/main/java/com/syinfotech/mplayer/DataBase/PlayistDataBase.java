package com.syinfotech.mplayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.syinfotech.mplayer.Model.Song;

import java.util.ArrayList;
import java.util.List;

public class PlayistDataBase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABSE_NAME = "MPlayerPlayist";
    private static final String TABLE_PLAYIST = "PLAYIST";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_SIZE = "size";
    private static final String KEY_Path = "path";


    public PlayistDataBase(@Nullable Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_PLAYIST + "(" + KEY_ID + " TEXT PRIMARY KEY, "
                + KEY_TITLE + " TEXT, "
                + KEY_ARTIST + " TEXT, "
                + KEY_ALBUM + " TEXT, "
                + KEY_DURATION + " TEXT, "
                + KEY_Path + " TEXT ,"
                + KEY_SIZE + " TEXT ) ";
        sqLiteDatabase.execSQL(CREATE_CART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYIST);
        onCreate(sqLiteDatabase);
    }

    public void AddToPlayist(int position, List<Song> songList) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, songList.get(position).getId());
        values.put(KEY_TITLE, songList.get(position).getTitle());
        values.put(KEY_ARTIST, songList.get(position).getArtist());
        values.put(KEY_ALBUM, songList.get(position).getAlbum());
        values.put(KEY_DURATION, songList.get(position).getDuration());
        values.put(KEY_SIZE, songList.get(position).getSize());
        values.put(KEY_Path,songList.get(position).getPath());
        database.insert(TABLE_PLAYIST,null,values);
        database.close();
    }
    public List<Song> getAllPlayistSongs(){
        String selectQuery = "SELECT * FROM " +TABLE_PLAYIST;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        List<Song> songList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do {
                Song song = new Song();
                song.setId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)));
                song.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                song.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DURATION)));
                song.setSize(cursor.getString(cursor.getColumnIndexOrThrow(KEY_SIZE)));
                song.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ARTIST)));
                song.setAlbum(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ALBUM)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(KEY_Path)));
                songList.add(song);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return songList;
    }
    public void RemoveAllPlayistSongs(){
        String selectQuery = "DELETE FROM " +TABLE_PLAYIST;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL(selectQuery);
    }
    public void RemoveSongFromPlayist(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_PLAYIST, KEY_ID + "= ?", new String[]{id});
        sqLiteDatabase.close();
    }

}
