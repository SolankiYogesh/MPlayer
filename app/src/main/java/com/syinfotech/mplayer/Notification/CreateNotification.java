package com.syinfotech.mplayer.Notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.syinfotech.mplayer.Model.Song;
import com.syinfotech.mplayer.R;
import com.syinfotech.mplayer.Services.NotificationActionService;
import com.syinfotech.mplayer.UI.MainActivity;

import java.util.List;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";


    public static final String ACTION_PREVIUOS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";

    public static Notification notification;

    @SuppressLint("UnspecifiedImmutableFlag")
    public static void createNotification(Context context, List<Song> list, int playbutton, int pos,boolean ShowNotification) {


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.albumart);

        PendingIntent pendingIntentPrevious;
        int drw_previous;

        Intent intentPrevious = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_PREVIUOS);
        pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
        drw_previous = R.drawable.ic_previous;


        Intent intentPlay = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_PLAY);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntentNext;
        int drw_next;
        Intent intentNext = new Intent(context, NotificationActionService.class)
                .setAction(ACTION_NEXT);
        pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
        drw_next = R.drawable.ic_next;


        //create notification
        notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.music)
                .setContentTitle(list.get(pos).getTitle())
                .setContentText(list.get(pos).getArtist())
                .setLargeIcon(icon)
                .setOngoing(ShowNotification)
                .addAction(drw_previous, "Previous", pendingIntentPrevious)
                .addAction(playbutton, "Play", pendingIntentPlay)
                .addAction(drw_next, "Next", pendingIntentNext)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        notificationManagerCompat.notify(1, notification);

    }
}

