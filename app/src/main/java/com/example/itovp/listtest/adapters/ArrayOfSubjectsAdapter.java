package com.example.itovp.listtest.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.entities.Subject;
import com.example.itovp.listtest.fragments.EditSbjFragment;

import java.util.List;


/**
 * Created by itovp on 31.01.2017.
 */

public class ArrayOfSubjectsAdapter extends RecyclerView.Adapter<ArrayOfSubjectsAdapter.ViewHolder> {

	private List<Subject> mListOfSubjects;

	private Context ctx;

	int orientation;

	int currentPosition = -1;

	public static final String KEY_EDIT_FRAGMENT = "edit";

	private View.OnClickListener mOnItemClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			notifyItemChanged(currentPosition);
			currentPosition = position;
			notifyItemChanged(currentPosition);
			FragmentManager fm = ((MainActivity) ctx).getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(KEY_EDIT_FRAGMENT);

			if (editSbjFragment == null) {
				editSbjFragment = EditSbjFragment.newInstance(mListOfSubjects.get(position));
				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					ft.replace(R.id.container_for_list, editSbjFragment);
					ft.addToBackStack(null);
				} else {
					ft.replace(R.id.container_for_edit_land, editSbjFragment, KEY_EDIT_FRAGMENT);
				}
				ft.commit();
			} else {
				editSbjFragment.setCurrentItem(mListOfSubjects.get(position));
			}
		}
	};

	private View.OnClickListener mOnCheckbox = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mListOfSubjects.get((Integer) v.getTag()).setmSelected(((CheckBox) v).isChecked());
		}
	};

	public ArrayOfSubjectsAdapter(List<Subject> list, Context ctx) {
		mListOfSubjects = list;
		this.ctx = ctx;
		orientation = ctx.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			FragmentManager fm = ((MainActivity) ctx).getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(KEY_EDIT_FRAGMENT);
			if (editSbjFragment != null) {
				ft.remove(editSbjFragment);
			}
			ft.commit();
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView iw;
		TextView tw, tw2;
		CheckBox cb;

		public ViewHolder(View view) {
			super(view);
			iw = (ImageView) view.findViewById(R.id.sbj_icon);
			tw = (TextView) view.findViewById(R.id.sbj_code);
			cb = (CheckBox) view.findViewById(R.id.sbj_checkbox);
			tw2 = (TextView) view.findViewById(R.id.sbj_descr);
		}
	}

	@Override
	public ArrayOfSubjectsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_item, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder vh, final int position) {
		final Subject sbj = mListOfSubjects.get(position);

		if (currentPosition == position) {
			vh.itemView.setBackgroundColor(Color.YELLOW);
		} else {
			vh.itemView.setBackgroundColor(Color.WHITE);
		}
		vh.cb.setChecked(sbj.getmSelected());
		vh.tw.setText(sbj.getCode().toString());
		vh.iw.setImageResource(sbj.getmIconRes());
		vh.tw2.setText(sbj.getmDescription());

		vh.itemView.setTag(position);
		vh.itemView.setOnClickListener(mOnItemClick);
		vh.cb.setTag(position);
		vh.cb.setOnClickListener(mOnCheckbox);
	}

	@Override
	public int getItemCount() {
		return mListOfSubjects.size();
	}
}
