package com.syinfotech.mplayer.Model;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Methods {
    //All Songs List From Devices
    public static List<Song> getAllSongsFromDevice(Context context) {
        List<Song> songslist = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION
        };
        @SuppressLint("Recycle") Cursor cursor1 = context.getContentResolver().query(uri,
                cursor,
                null,
                null,
                null);
        if (cursor1 != null) {
            while (cursor1.moveToNext()) {
                String Id = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String PAth = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                String Title = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String Album = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                long AlbumId = cursor1.getLong(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                int Duration = cursor1.getInt(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String Size = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                String Artist = cursor1.getString(cursor1.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                Uri imagepath = Uri.parse("content://media/extrenal/audio/albumart");
                Uri imagepathuri = ContentUris.withAppendedId(imagepath, AlbumId);

                Song song = new Song();
                song.setId(Id);
                song.setPath(PAth);
                song.setTitle(Title);
                song.setAlbum(Album);
                song.setArtist(Artist);
                song.setDuration(getTimeFormatted((long) Duration));
                song.setSize(getSize(Long.valueOf(Size), context));
                song.setAlbumId(String.valueOf(AlbumId));
                song.setImagepath(String.valueOf(imagepathuri));
                songslist.add(song);
            }
        }

        return songslist;
    }

    //All Song List From Folders
    public static List<Song> getAllSongsFromFolder(Context context, String dir) {
        final List<Song> tempAudioList = new ArrayList<>();

        String[] selectionArgs = new String[]{"%" + dir + "%"};
        String Slection = MediaStore.Audio.AudioColumns.DATA + " like?";
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION

        };
        Cursor c = context.getContentResolver().query(uri,
                projection,
                Slection,
                selectionArgs,
                null);
        if (c != null) {
            while (c.moveToNext()) {

                String id = c.getString(0);
                String Path = c.getString(1);
                String Title = c.getString(2);
                String Album = c.getString(3);
                String Artist = c.getString(5);
                String Size = c.getString(6);
                String Duration = c.getString(7);

                Song song = new Song();
                song.setId(id);
                song.setPath(Path);
                song.setTitle(Title);
                song.setAlbum(Album);
                song.setArtist(Artist);
                song.setDuration(getTimeFormatted(Long.valueOf(Duration)));
                song.setSize(getSize(Long.valueOf(Size), context));
                tempAudioList.add(song);


            }
            c.close();
        }

        return tempAudioList;
    }


    //Get All Folder List Which Have Music Files
    public static List<String> getAllFolderList(Context context) {
        List<String> folderlist = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] cursor = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION
        };
        @SuppressLint("Recycle") Cursor c = context.getContentResolver().query(uri,
                cursor,
                null,
                null,
                null);
        if (c != null) {
            while (c.moveToNext()) {


                String data = c.getString(1);

                int SlashIndex = data.lastIndexOf("/");
                String Substring = data.substring(0, SlashIndex);
                int index = Substring.lastIndexOf("/");
                String FolderName = Substring.substring(index + 1, SlashIndex);
                if (!folderlist.contains(FolderName)) {
                    folderlist.add(FolderName);
                }
            }
            c.close();
        }

        return folderlist;
    }

    //get Size in Mb,Kb ect..
    public static String getSize(Long size, Context context) {
        return Formatter.formatFileSize(context, size);
    }

    //Convert long to minutes and Secons
    public static String getTimeFormatted(Long milliSeconds) {
        String finalTimerString = "";
        String secondsString;

        //Converting total duration into time
        int hours = (int) (milliSeconds / 3600000);
        int minutes = (int) (milliSeconds % 3600000) / 60000;
        int seconds = (int) ((milliSeconds % 3600000) % 60000 / 1000);

        // Adding hours if any
        if (hours > 0)
            finalTimerString = hours + ":";

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10)
            secondsString = "0" + seconds;
        else
            secondsString = "" + seconds;

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // Return timer String;
        return finalTimerString;
    }

    //get All files count integer
    public static int getAudioCount(Context context, String dir) {
        final List<Song> tempAudioList = new ArrayList<>();

        String[] selectionArgs = new String[]{"%" + dir + "%"};
        String Slection = MediaStore.Audio.AudioColumns.DATA + " like?";
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri,
                projection,
                Slection,
                selectionArgs,
                null);
        if (c != null) {
            while (c.moveToNext()) {

                Song audioModel = new Song();
                String path = c.getString(0);
                String artist = c.getString(1);

                String name = path.substring(path.lastIndexOf("/") + 1);

                audioModel.setTitle(name);
                audioModel.setArtist(artist);
                audioModel.setPath(path);

                tempAudioList.add(audioModel);
            }
            c.close();
        }

        return tempAudioList.size();
    }
}
