package com.example.itovp.listtest.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.adapters.ExpandListAdapt;
import com.example.itovp.listtest.entities.Subject;

import java.util.ArrayList;

/**
 * Created by itovp on 23.02.2017.
 */

public class GroupedSubjectsFragment extends Fragment implements ListFragment {

	ImageView mImageView;
	TextView mTextView;
	ExpandableListView mExpandableListView;
	Context ctx;

	ArrayList<Subject> mListOfSubjects = new ArrayList<>();

	ResponceReceiver mRespReceiver = new ResponceReceiver();

	public static final String KEY_FOR_LIST_EXP = "keyFroExpList";

	public GroupedSubjectsFragment() {
		super();
	}

	@Override
	public void addSbj(Subject sbg) {
		mListOfSubjects.add(sbg);
		((ExpandListAdapt) mExpandableListView.getExpandableListAdapter()).notifyDataSetChanged();
	}

	@Override
	public void editSbj(Subject sbg) {
		for (Subject subject : mListOfSubjects) {
			if (subject.equals(sbg)) {
				subject = sbg;
			}
			((ExpandListAdapt) mExpandableListView.getExpandableListAdapter()).notifyDataSetChanged();
		}
	}

	private class ResponceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mListOfSubjects.addAll(intent.<Subject>getParcelableArrayListExtra(MainActivity.KEY_LIST_FRAGMENT));
			((ExpandListAdapt) mExpandableListView.getExpandableListAdapter()).addItems(mListOfSubjects);
			LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mRespReceiver);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle bundle) {
		View v = lf.inflate(R.layout.frg_grouped_sbj, vg, false);

		mImageView = (ImageView) v.findViewById(R.id.frg_group_list_img);
		mTextView = (TextView) v.findViewById(R.id.frg_group_list_text);
		mExpandableListView = (ExpandableListView) v.findViewById(R.id.frg_group_list_list);

		mImageView.setImageResource(R.drawable.images);//// TODO: 23.02.2017 Random Picture
		//mTextView.setText(""); //// TODO: 23.02.2017 here will be a poem O_o

		if (bundle == null) {
			if (mListOfSubjects.size() == 0) {
				LocalBroadcastManager.getInstance(v.getContext()).registerReceiver(mRespReceiver, new IntentFilter(MainActivity.DATA_FROM_SERVICE));
			}
		} else {
			mListOfSubjects = (ArrayList<Subject>) bundle.getSerializable(MainActivity.LIST_OF_SUBJECTS);
		}
		ExpandListAdapt adapter = new ExpandListAdapt(v.getContext(), mListOfSubjects);
		mExpandableListView.setAdapter(adapter);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(MainActivity.LIST_OF_SUBJECTS, mListOfSubjects);
		super.onSaveInstanceState(outState);
	}
}
