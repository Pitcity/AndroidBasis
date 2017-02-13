package com.example.itovp.listtest.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itovp.listtest.R;

/**
 * Created by itovp on 13.02.2017.
 */

public class LoadingFragment extends DialogFragment{
	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, final Bundle bundle) {
		return lf.inflate(R.layout.frg_loading, vg, false);
	}
}
