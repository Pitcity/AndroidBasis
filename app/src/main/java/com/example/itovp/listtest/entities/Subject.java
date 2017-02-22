package com.example.itovp.listtest.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.itovp.listtest.activities.MainActivity;

import java.math.BigInteger;

/**
 * Created by itovp on 01.02.2017.
 */

public class Subject implements Parcelable {

	private int mIconRes;
	private BigInteger mCode;
	private Boolean mSelected;
	private String mDescription;

	public Subject(int mIconRes, BigInteger mCode, Boolean mSelected) {
		this.mIconRes = mIconRes;
		this.mCode = mCode;
		this.mSelected = mSelected;
		this.mDescription = MainActivity.DEFAULT_COMMENT;
	}

	public Subject(int mIconRes, BigInteger mCode, Boolean mSelected, String descr) {
		this(mIconRes, mCode, mSelected);
		this.mDescription = descr;
	}

	private Subject(Parcel in) {
		String[] strArr = new String[2];
		int[] intArr = new int[2];
		in.readIntArray(intArr);
		in.readStringArray(strArr);
		mDescription = strArr[0];
		mCode = BigInteger.valueOf(Long.parseLong(strArr[1]));
		mIconRes = intArr[0];
		mSelected = intArr[1] == 1;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeIntArray(new int[]{mIconRes, mSelected ? 1 : 0});
		dest.writeStringArray(new String[]{mDescription, mCode.toString()});
	}

	public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
		@Override
		public Subject createFromParcel(Parcel in) {
			return new Subject(in);
		}

		@Override
		public Subject[] newArray(int size) {
			return new Subject[size];
		}
	};

	@Override
	public boolean equals(Object o) {
		return ((o instanceof Subject) && mCode.equals(((Subject) o).getCode()));
	}

	public int getIconRes() {
		return mIconRes;
	}

	public BigInteger getCode() {
		return mCode;
	}

	public Boolean getSelected() {
		return mSelected;
	}

	public void setSelected(Boolean mSelected) {
		this.mSelected = mSelected;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public boolean update(Subject updatedSbj) {
		if (getCode().equals(updatedSbj.getCode())) {
			setDescription(updatedSbj.getDescription());
			setSelected(updatedSbj.getSelected());
			return true;
		} else {
			return false;
		}
	}
}
