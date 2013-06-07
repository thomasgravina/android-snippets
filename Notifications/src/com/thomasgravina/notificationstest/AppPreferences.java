package com.thomasgravina.notificationstest;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

	public static final String PROPERTY_REG_ID = "registrationId";
	public static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
	private static final String PROPERTY_APP_VERSION = "appVersion";

	/**
	 * Registration ID
	 */
	public static void storeRegistrationId(final Context context,
	    final String regId) {

		final int appVersion = AppUtils.getAppVersion(context);
		final long expirationTime = System.currentTimeMillis()
		    + GCMService.REGISTRATION_EXPIRY_TIME_MS;

		final SharedPreferences.Editor editor = getPreferences(context).edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
	}

	public static String getRegistrationId(final Context context) {
		return getPreferences(context).getString(PROPERTY_REG_ID, "");
	}

	/**
	 * App Version
	 */
	public static int getAppVersion(final Context context) {
		return getPreferences(context).getInt(PROPERTY_APP_VERSION,
		    Integer.MIN_VALUE);
	}

	public static SharedPreferences getPreferences(final Context context) {
		return context.getSharedPreferences(MainActivity.class.getSimpleName(),
		    Context.MODE_PRIVATE);
	}
}
