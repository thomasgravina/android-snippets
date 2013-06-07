package com.thomasgravina.notificationstest;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmBroadcastReceiver extends BroadcastReceiver {

	private static int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	private Context ctx;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		ctx = context;
		final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);		
		final String messageType = gcm.getMessageType(intent);
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
			sendNotification("Send error: " + intent.getExtras().toString());
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
			sendNotification("Deleted messages on server: "
			    + intent.getExtras().toString());
		} else {
			sendNotification("Received: " + intent.getExtras().toString());
		}
		setResultCode(Activity.RESULT_OK);
	}

	// Put the GCM message into a notification and post it.
	private void sendNotification(final String msg) {
		mNotificationManager = (NotificationManager) ctx
		    .getSystemService(Context.NOTIFICATION_SERVICE);

		final PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
		    new Intent(ctx, MainActivity.class), 0);

		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
		    ctx).setSmallIcon(R.drawable.ic_launcher)
		    .setContentTitle("GCM Notification")
		    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
		    .setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}