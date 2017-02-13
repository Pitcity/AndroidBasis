package com.example.itovp.listtest.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.entities.Subject;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.itovp.listtest.activities.MainActivity.DEFAULT_PICTURE;

/**
 * Created by itovp on 13.02.2017.
 */

public class SubjectsLoads_Service extends IntentService {

	/**
	 * Creates an IntentService.  Invoked by your subclass's constructor.
	 *
	 * @param name Used to name the worker thread, important only for debugging.
	 */
	public SubjectsLoads_Service(String name) {
		super(name);
	}

	public SubjectsLoads_Service() {
		super("");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		List<Subject> list = new ArrayList<>();
		for (int i = 0; i < MainActivity.AMOUNT_OF_SUBJECTS; i++) {
			System.out.println("\n service = " + i);
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list.add(new Subject(DEFAULT_PICTURE, BigInteger.valueOf((long) i), false));
		}
		Intent outputIntent = new Intent(MainActivity.DATA_FROM_SERVICE);
		outputIntent.putExtra(MainActivity.KEY_LIST_FRAGMENT, (Serializable) list);
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(outputIntent);
		stopSelf();
	}
}
