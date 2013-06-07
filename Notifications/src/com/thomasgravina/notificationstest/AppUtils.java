package com.thomasgravina.notificationstest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtils {

	public static int getAppVersion(Context context) {
		try {
			final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
			    context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (final NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}
}
