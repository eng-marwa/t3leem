package com.salim.ta3limes.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.salim.ta3limes.Activities.LoginActivity;
import com.salim.ta3limes.Activities.NotificationActivity;
import com.salim.ta3limes.R;

public class FirebaseMessagingHandler extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessagingHandle";
    private String title, type;
    private String text = "" ;
    SharedPreferencesUtilities preferencesUtilities;
    private Context mContext;
    AudioAttributes audioAttributes ;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        Log.e(TAG, "onMessageReceived: data " + remoteMessage.getData());
//        Log.e(TAG, "onMessageReceived: size " + remoteMessage.getData().size());
//        Log.e(TAG, "onMessageReceived: title " + remoteMessage.getData().get("title"));
//        Log.e(TAG, "onMessageReceived: type " + remoteMessage.getData().get("type"));

        title = remoteMessage.getData().get("title");
        text = remoteMessage.getData().get("body");
        type = remoteMessage.getData().get("type");

        Log.e(TAG, "onMessageReceived: text   " + text);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.slow_spring);

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
        }

        Intent intent;
        if(type.equals("block")){
            intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Long.toString(System.currentTimeMillis()));

        }else {
            intent = new Intent(getApplicationContext(), NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("title", title);
            intent.putExtra("body", text);
            intent.setAction(Long.toString(System.currentTimeMillis()));
        }

        int code = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, code, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_notification_channel_id),
                    "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(soundUri ,audioAttributes);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

        NotificationCompat.Builder notificationBuidler = new NotificationCompat.Builder(this)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text)
                        .setBigContentTitle(title))
                .setContentTitle(title)
                .setContentText(text)
                .setSound(soundUri)
                .setSmallIcon(R.mipmap.ic_launcher_logo_round)
                .setAutoCancel(true)
                .setContentIntent(contentIntent);
        notificationBuidler.setChannelId(getString(R.string.default_notification_channel_id));
        notificationManager.notify(code, notificationBuidler.build());

    }

    @Override
    public void onNewToken(String s) {

        preferencesUtilities = new SharedPreferencesUtilities(this);
        preferencesUtilities.setRegToken(s);
        Log.e(TAG, "onNewToken: " + s);
        super.onNewToken(s);
    }

}
