package com.thomasgravina.notificationstest;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMService {

	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
	private static final String SENDER_ID = "xxxxxxxxxxxx";
	private static final String TAG = "GCMService";

	private static GoogleCloudMessaging gcm;

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's shared preferences.
	 */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerBackground(final Context context) {

		new AsyncTask() {

			@Override
			protected Object doInBackground(final Object... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					msg = gcm.register(SENDER_ID);
				} catch (final IOException e) {
					msg = "ERROR " + e.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(final Object msg) {
				final String registrationId = msg.toString();
				if (!registrationId.startsWith("ERROR")) {
					AppPreferences.storeRegistrationId(context, msg.toString());
				}
			}

		}.execute(null, null, null);
	}

	/**
	 * Checks if the registration has expired.
	 * 
	 * <p>
	 * To avoid the scenario where the device sends the registration to the server
	 * but the server loses it, the app developer may choose to re-register after
	 * REGISTRATION_EXPIRY_TIME_MS.
	 * 
	 * @return true if the registration has expired.
	 */
	private static boolean isRegistrationExpired(final Context context) {
		final SharedPreferences prefs = AppPreferences.getPreferences(context);
		// checks if the information is not stale
		long expirationTime = prefs.getLong(
		    AppPreferences.PROPERTY_ON_SERVER_EXPIRATION_TIME, -1);
		return System.currentTimeMillis() > expirationTime;
	}

	/**
	 * Gets the current registration id for application on GCM service.
	 * <p>
	 * If result is empty, the registration has failed.
	 * 
	 * @return registration id, or empty string if the registration is not
	 *         complete.
	 */
	public static String getRegistrationId(Context context) {
		final String registrationId = AppPreferences.getRegistrationId(context);
		if (registrationId.length() == 0) {
			Log.v(TAG, "Registration not found.");
			return "";
		}
		// check if app was updated; if so, it must clear registration id to
		// avoid a race condition if GCM sends a message
		final int registeredVersion = AppPreferences.getAppVersion(context);
		final int currentVersion = AppUtils.getAppVersion(context);
		if (registeredVersion != currentVersion || isRegistrationExpired(context)) {
			Log.v(TAG, "App version changed or registration expired.");
			return "";
		}
		return registrationId;
	}
}
