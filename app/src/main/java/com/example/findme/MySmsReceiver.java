package com.example.findme;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

public class MySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String messageBody, phoneNumber;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > -1) {
                    messageBody = messages[0].getMessageBody();
                    phoneNumber = messages[0].getDisplayOriginatingAddress();

                    if(messageBody.toLowerCase().contains("findme")){
                        Toast.makeText(context,
                                "Message : " + messageBody + "Received from;" + phoneNumber,
                                Toast.LENGTH_LONG)
                                .show();
                    }



                    NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(
                            context, "CHANNEL_FINDME");
                    mbuilder.setContentTitle("Find me");
                    mbuilder.setContentText("Nouvelle position recu" + messageBody);
                    mbuilder.setAutoCancel(true);
                    mbuilder.setSmallIcon(android.R.drawable.ic_dialog_map);
                    mbuilder.setVibrate(new long[]{0, 2000, 500, 2000});
                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mbuilder.setSound(sound);

                    Intent i = new Intent(context, Ajout.class);

                    i.putExtra("NUMERO", phoneNumber);
                    i.putExtra("CONTENU", messageBody);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    PendingIntent pi = PendingIntent.getActivity(context,
                            0,
                            i,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    mbuilder.setContentIntent(pi);

                    NotificationManagerCompat notificationManager =
                            NotificationManagerCompat.from(context);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("CHANNEL_FINDME",
                                "Findme_App", NotificationManager.IMPORTANCE_HIGH);
                        //channel.setDescription("description");
                        notificationManager.createNotificationChannel(channel);
                        notificationManager.notify(1, mbuilder.build());
                    }


                    //context.startActivity(i);

                }
            }
        }
}

