package com.example.itovp.listtest.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.asyncrone.Counter;
import com.example.itovp.listtest.entities.Subject;

/**
 * Created by itovp on 01.02.2017.
 */

public class SubjectInfoActivity extends AppCompatActivity {

	EditText mTvDescr;
	TextView mTvId, mPbResult;
	ImageView mIvLogo;
	CheckBox mChChecked;
	ProgressBar mProgressBar;

	Counter mCounter;
	Subject mCurrentrSbj;

	int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_info_activity);

		mTvDescr = (EditText) findViewById(R.id.sbj_inf_descr);
		mTvId = (TextView) findViewById(R.id.sbj_inf_code);
		mIvLogo = (ImageView) findViewById(R.id.sbj_inf_logo);
		mChChecked = (CheckBox) findViewById(R.id.sbj_inf_checked);
		mPbResult = (TextView) findViewById(R.id.resCounter);
		mProgressBar = (ProgressBar) findViewById(R.id.counterBp);

		mCurrentrSbj = getIntent().getBundleExtra("bundleInfo").getParcelable("currentSubject");
		position = getIntent().getBundleExtra("bundleInfo").getInt("position", 0);

		updateView(mCurrentrSbj);

		if (savedInstanceState != null) {
			mCounter = savedInstanceState.getParcelable("counter");
			if (mCounter != null) {
				if (!(AsyncTask.Status.FINISHED == mCounter.getStatus())) {
					mCounter.reLink(mProgressBar, mPbResult);
				} else {
					mPbResult.setText("");
				}
			}
		}
		if (savedInstanceState == null) {
			mCounter = new Counter(mProgressBar, mPbResult);
			mCounter.execute();
		}
	}

	@Override
	public void onBackPressed() {
		Bundle bundle = new Bundle();

		mCurrentrSbj.setmDescription(mTvDescr.getText().toString());
		mCurrentrSbj.setmSelected(mChChecked.isChecked());

		bundle.putInt("position", position);
		bundle.putParcelable("updatedSbj", mCurrentrSbj);
		Intent mIntent = new Intent();
		mIntent.putExtras(bundle);
		setResult(RESULT_OK, mIntent);
		mCounter.cancel(true);

		super.onBackPressed();
	}

	private void updateView(Subject sbj) {
		mTvDescr.setText(sbj.getmDescription());
		mTvId.setText(sbj.getCode().toString());
		mIvLogo.setImageResource(sbj.getmIconRes());
		mChChecked.setChecked(sbj.getmSelected());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable("counter", mCounter);
	}

}
