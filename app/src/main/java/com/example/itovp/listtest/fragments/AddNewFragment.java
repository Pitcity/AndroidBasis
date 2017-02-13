package com.example.itovp.listtest.fragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.entities.Subject;

import java.math.BigInteger;

/**
 * Created by itovp on 06.02.2017.
 */

public class AddNewFragment extends DialogFragment {

	OnNewSbjCreated mCallBack;

	public interface OnNewSbjCreated {
		void onNewCreated(Subject sbj);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallBack = (OnNewSbjCreated) activity;
		} catch (ClassCastException e) {

		}
	}

	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, final Bundle bundle) {
		final View mV = lf.inflate(R.layout.add_new_sbj_fragment, vg, false);

		mV.findViewById(R.id.addNew_submit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String code = ((EditText) mV.findViewById(R.id.addNew_code)).getText().toString();
				if (!code.equals("")) {
					Subject sbj = new Subject(MainActivity.DEFAULT_PICTURE,
							BigInteger.valueOf(Long.valueOf(code)), false,
							((EditText) mV.findViewById(R.id.addNew_descr)).getText().toString());
					mCallBack.onNewCreated(sbj);
					((DialogFragment) getFragmentManager().findFragmentByTag("addNewDialog")).dismiss();
				}
			}
		});
		return mV;
	}
}
