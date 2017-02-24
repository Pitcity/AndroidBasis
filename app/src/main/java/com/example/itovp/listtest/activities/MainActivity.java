package com.example.itovp.listtest.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.entities.Subject;
import com.example.itovp.listtest.fragments.AddNewFragment;
import com.example.itovp.listtest.fragments.EditSbjFragment;
import com.example.itovp.listtest.fragments.GroupedSubjectsFragment;
import com.example.itovp.listtest.fragments.SbjListFragment;
import com.example.itovp.listtest.services.SubjectsLoads_Service;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements AddNewFragment.OnNewSbjCreated, EditSbjFragment.onDataEdit {

	private final static int LENGTH_OF_CODE = 6;
	public final static int AMOUNT_OF_SUBJECTS = 100;
	public final static int DEFAULT_PICTURE = R.drawable.images_1;
	public final static String KEY_LIST_FRAGMENT = "list";
	public final static String KEY_EXP_LIST_FRAGMENT = "exp_list";
	public final static String DATA_FROM_SERVICE = "getDataFromService";
	public final static String ADD_NEW_DIALOG = "addNewDialog";
	public static final String DEFAULT_COMMENT = "no comments -_-";
	public static final String LIST_OF_SUBJECTS = "ListOfSubjects";
	public static final String EDIT_FRAGMENT = "editFragment";

	SbjListFragment mLf;
	GroupedSubjectsFragment expListFrg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentManager frM = getFragmentManager();
		mLf = (SbjListFragment) frM.findFragmentByTag(KEY_LIST_FRAGMENT);
		expListFrg = (GroupedSubjectsFragment) frM.findFragmentByTag(KEY_EXP_LIST_FRAGMENT);

		if (Configuration.ORIENTATION_LANDSCAPE != getResources().getConfiguration().orientation) {
			if (expListFrg == null) {
				expListFrg = new GroupedSubjectsFragment();
			}
			frM.beginTransaction().replace(R.id.container_for_list, expListFrg, KEY_EXP_LIST_FRAGMENT).commit();
		} else {
			if (mLf == null) {
				mLf = new SbjListFragment();
			}
			frM.beginTransaction().replace(R.id.container_for_list, mLf, KEY_LIST_FRAGMENT).commit();
		}

		startService(new Intent(getBaseContext(), SubjectsLoads_Service.class));
		setLocale("en");
	}

	@Override
	public void onNewCreated(Subject sbj) {
		if (mLf == null) {
			expListFrg.addSbj(sbj);
		} else {
			mLf.addSbj(sbj);
		}
	}

	@Override
	public void onDataChanged(Subject sbj) {
		if (mLf == null) {
			expListFrg.editSbj(sbj);
		} else {
			mLf.editSbj(sbj);
		}
	}

	private void setLocale(String countryLoc) {
		Locale locale = new Locale(countryLoc);
		Locale.setDefault(locale);
		Configuration config = getBaseContext().getResources().getConfiguration();
		config.locale = locale;
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
	}
}
