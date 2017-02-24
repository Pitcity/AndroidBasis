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

	private long mMiliseconds = 0;
	private transient ProgressBar mPb;
	private transient TextView mTv;

	private transient Timer mTimer;

	public Counter(ProgressBar progressBar, TextView textView) {
		super();
	}

	public Counter(Timer t, long miliseconds) {
		super();
		mTimer = t;
		mMiliseconds = miliseconds;
	}

	@Override
	protected Long doInBackground(Integer... params) {
		try {
			Thread.sleep(mMiliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1l;
	}

	@Override
	protected void onPostExecute(Long l) {
		mTimer.onFinishCounting();
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

	public interface Timer {
		void onFinishCounting();

		void startCounting(long miliseconds);
	}
}
