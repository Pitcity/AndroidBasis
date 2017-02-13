package com.example.itovp.listtest.asyncrone;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by itovp on 02.02.2017.
 */

public class Counter extends AsyncTask<Integer, Integer, Long> implements Parcelable {

	private transient ProgressBar mPb;
	private transient TextView mTv;

	private final static int MAX_FOR_COUNTER = 100;
	private final static int DELIMITER_FOR_COUNTER = 10;
	private final static long DELAY_FOR_EACH_INCREMENT = 100L;

	public Counter(ProgressBar progressBar, TextView textView) {
		super();
		mPb = progressBar;
		mTv = textView;
	}

	public void reLink(ProgressBar progressBar, TextView textView) {
		textView.setText(mTv.getText());
		progressBar.setProgress(mPb.getProgress());
		mPb = progressBar;
		mTv = textView;
		onPreExecute();
	}

	@Override
	protected Long doInBackground(Integer... params) {
		long counter = 0;
		for (int i = 0; i < MAX_FOR_COUNTER; i++) {
			counter++;
			try {
				Thread.sleep(DELAY_FOR_EACH_INCREMENT);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (i % DELIMITER_FOR_COUNTER == 0) {
				publishProgress((i * DELIMITER_FOR_COUNTER / MAX_FOR_COUNTER));
			}
		}
		return counter;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		mPb.setProgress(values[0]);
		mTv.setText("Progress is = " + values[0] + "; on prBar is " + mPb.getProgress());
	}

	@Override
	protected void onPreExecute() {
		mPb.setMax(MAX_FOR_COUNTER / DELIMITER_FOR_COUNTER);
		mPb.setVisibility(ProgressBar.VISIBLE);
	}

	@Override
	protected void onPostExecute(Long l) {
		mPb.setVisibility(ProgressBar.GONE);
		mTv.setText("Counter finished counting with " + l + " numbers");
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(mPb);
		dest.writeValue(mTv);
	}

	protected Counter(Parcel in) {
		mPb = (ProgressBar) in.readValue(ProgressBar.class.getClassLoader());
		mTv = (TextView) in.readValue(TextView.class.getClassLoader());
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<Counter> CREATOR = new Parcelable.Creator<Counter>() {
		@Override
		public Counter createFromParcel(Parcel in) {
			return new Counter(in);
		}

		@Override
		public Counter[] newArray(int size) {
			return new Counter[size];
		}
	};
}
