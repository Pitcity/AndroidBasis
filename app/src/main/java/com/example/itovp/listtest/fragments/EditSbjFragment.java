package com.example.itovp.listtest.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.entities.Subject;

/**
 * Created by itovp on 06.02.2017.
 */

public class EditSbjFragment extends Fragment {

	public static final String KEY_SUBJECT = "key_subject";

	onDataEdit mNotifier;

	EditText descr;
	CheckBox cb;
	ImageView iv;
	TextView tv;

	Subject sbj;

	public interface onDataEdit {
		void onDataChanged(Subject sbj);
	}

	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);

		try {
			mNotifier = (onDataEdit) context;
		} catch (ClassCastException e) {
		}
	}

	public static EditSbjFragment newInstance(Subject subject) {
		Bundle args = new Bundle();
		args.putParcelable(KEY_SUBJECT, subject);
		EditSbjFragment fragment = new EditSbjFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sbj = getArguments().getParcelable(KEY_SUBJECT);
	}

	@Override
	public View onCreateView(LayoutInflater lf, ViewGroup vg, Bundle bundle) {
		View v = lf.inflate(R.layout.frg_subject_info, vg, false);
		cb = (CheckBox) v.findViewById(R.id.sbj_inf_checked);
		descr = (EditText) v.findViewById(R.id.sbj_inf_descr);
		iv = (ImageView) v.findViewById(R.id.sbj_inf_logo);
		tv = (TextView) v.findViewById(R.id.sbj_inf_code);
		if (bundle != null) {
			Subject sbj = bundle.getParcelable(KEY_SUBJECT);
			if (sbj != null) {
				this.sbj = sbj;
			}
		}
		updateView();
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelable(KEY_SUBJECT, sbj);
	}

	public void setCurrentItem(Subject sbj) {
		this.sbj.setSelected(cb.isChecked());
		this.sbj.setDescription(descr.getText().toString());
		mNotifier.onDataChanged(this.sbj);

		this.sbj = sbj;
		if (getView() != null) {
			updateView();
		}
	}

	private void updateView() {
		cb.setChecked(sbj.getSelected());
		iv.setImageResource(sbj.getIconRes());
		tv.setText(sbj.getCode().toString());
		descr.setText(sbj.getDescription());
	}

	@Override
	public void onDestroy() {
		this.sbj.setSelected(cb.isChecked());
		this.sbj.setDescription(descr.getText().toString());
		mNotifier.onDataChanged(this.sbj);
		super.onDestroy();
	}
}
