package com.thomasgravina.notificationstest;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final String registrationId = GCMService.getRegistrationId(getApplicationContext());
		
		if (registrationId.length() == 0) {
			GCMService.registerBackground(getApplicationContext());
		}

	}

}
