package com.example.itovp.listtest.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.entities.Subject;
import com.example.itovp.listtest.fragments.AddNewFragment;
import com.example.itovp.listtest.fragments.EditSbjFragment;
import com.example.itovp.listtest.fragments.SbjListFragment;
import com.example.itovp.listtest.services.SubjectsLoads_Service;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements AddNewFragment.OnNewSbjCreated, EditSbjFragment.onDataEdit {

	private final static int LENGTH_OF_CODE = 6;
	public final static int AMOUNT_OF_SUBJECTS = 100;
	public final static int DEFAULT_PICTURE = 2130837597;
	public final static String KEY_LIST_FRAGMENT = "list";
	public final static String DATA_FROM_SERVICE = "getDataFromService";
	public final static String ADD_NEW_DIALOG = "addNewDialog";
	public static final String DEFAULT_COMMENT = "no comments -_-";
	public static final String LIST_OF_SUBJECTS = "ListOfSubjects";

	SbjListFragment mLf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLf = (SbjListFragment) getFragmentManager().findFragmentByTag(KEY_LIST_FRAGMENT);
		if (mLf == null) {
			mLf = new SbjListFragment();
			getFragmentManager().beginTransaction().replace(R.id.container_for_list, mLf, KEY_LIST_FRAGMENT).commit();
			if (mLf.getListLength()==0) {
				startService(new Intent(getBaseContext(), SubjectsLoads_Service.class));
			}
		}

		setLocale("en");
	}

	@Override
	public void onNewCreated(Subject sbj) {
		mLf.addSbj(sbj);
	}

	@Override
	public void onDataChanged(Subject sbj) {
		mLf.editSbj(sbj);
	}

	private void setLocale(String countryLoc) {
		Locale locale = new Locale(countryLoc);
		Locale.setDefault(locale);
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	}
}
