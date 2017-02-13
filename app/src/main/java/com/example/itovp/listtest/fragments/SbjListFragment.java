package com.example.itovp.listtest.fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.adapters.ArrayOfSubjectsAdapter;
import com.example.itovp.listtest.entities.Subject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by itovp on 07.02.2017.
 */

public class SbjListFragment extends Fragment {

	ArrayList<Subject> mListOfSubjects = new ArrayList<>();
	RecyclerView mRecyclerView;
	FloatingActionButton mFab;
	ProgressBar mProgressBar;

	private class ResponceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mListOfSubjects.addAll((Collection<? extends Subject>) intent.getSerializableExtra(MainActivity.KEY_LIST_FRAGMENT));
			mRecyclerView.getAdapter().notifyDataSetChanged();
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle bundle) {
		View mV = lf.inflate(R.layout.frg_list, vg, false);

		mRecyclerView = (RecyclerView) mV.findViewById(R.id.frg_list_list);
		mFab = (FloatingActionButton) mV.findViewById(R.id.frg_list_addNew);
		mProgressBar = (ProgressBar) mV.findViewById(R.id.loading);
		final DialogFragment mDf = new AddNewFragment();

		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager mLayoutManager = new LinearLayoutManager(mV.getContext());

		mRecyclerView.setLayoutManager(mLayoutManager);
		LocalBroadcastManager.getInstance(mV.getContext()).registerReceiver(new ResponceReceiver(), new IntentFilter(MainActivity.DATA_FROM_SERVICE));

		if (bundle == null) {
			if (mListOfSubjects.size() == 0) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
		} else {
			mListOfSubjects = (ArrayList<Subject>) bundle.getSerializable("ListOfSubjects");
		}

		mRecyclerView.setAdapter(new ArrayOfSubjectsAdapter(mListOfSubjects, mV.getContext()));

		mFab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDf.show(getFragmentManager(), "addNewDialog");
			}
		});
		return mV;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable("ListOfSubjects", mListOfSubjects);
	}

	public void addSbj(Subject sbj) {
		mListOfSubjects.add(sbj);
		mRecyclerView.getAdapter().notifyDataSetChanged();
	}

	public void editSbj(Subject sbj) {
		for (Subject subject : mListOfSubjects) {
			if (subject.equals(sbj)) {
				subject = sbj;
			}
			mRecyclerView.getAdapter().notifyDataSetChanged();
		}
	}
}
