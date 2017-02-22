package com.example.itovp.listtest.adapters;

import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

	private int orientation;
	private int openElement = -1;
	private int currentPosition = -1;//// TODO: 13.02.2017 what is that -_- refactor

	private static final String EDIT_FRAGMENT = "editFragment";

	public static final int WIDTH_FOR_ANIMATION = 200;
	public static final int DURATION_FOR_ANIMATION = 1000;

	private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		float startX;

		View previousView;

		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int x = (int) event.getRawX();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = x;
					break;

				case MotionEvent.ACTION_UP:
					if (v.getId() == R.id.sbj_btn) {
						mListOfSubjects.remove((int) v.getTag());
						final View parent = (View) v.getParent().getParent();
						((View) parent.getParent()).animate().setDuration(DURATION_FOR_ANIMATION).translationY(-parent.getHeight()).start();
						parent.animate().alpha(0f).setDuration(DURATION_FOR_ANIMATION).withEndAction(new Runnable() {
							@Override
							public void run() {
								openElement = -1;
								((View) parent.getParent()).animate().setDuration(0).translationY(0).start();
								notifyDataSetChanged();
							}
						}).start();
					} else {
						if (previousView != null && (int) previousView.getTag() == openElement) {
							Button button = (Button) (((View) previousView.getParent()).findViewWithTag(openElement)).findViewById(R.id.sbj_btn);
							button.setEnabled(false);
							ObjectAnimator.ofFloat(previousView.findViewById(R.id.sbj_main_item_view), View.X, 10).setDuration(DURATION_FOR_ANIMATION).start();
							openElement = -1;
						}
						if (x < startX + 5 && x > startX - 5) {
							int position = (Integer) v.getTag();
							notifyItemChanged(currentPosition);
							currentPosition = position;
							notifyItemChanged(currentPosition);
							FragmentManager fm = ((MainActivity) ctx).getFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(EDIT_FRAGMENT);

							if (editSbjFragment == null) {
								editSbjFragment = EditSbjFragment.newInstance(mListOfSubjects.get(position));
								if (orientation == Configuration.ORIENTATION_PORTRAIT) {
									ft.replace(R.id.container_for_list, editSbjFragment);
									ft.addToBackStack(null);
								} else {
									ft.replace(R.id.container_for_edit_land, editSbjFragment, EDIT_FRAGMENT);
								}
								ft.commit();
							} else {
								editSbjFragment.setCurrentItem(mListOfSubjects.get(position));
							}
						} else {                // move it
							Button button = (Button) (((View) v.getParent()).findViewWithTag(v.getTag())).findViewById(R.id.sbj_btn);
							button.setEnabled(x - startX > 0);
							ObjectAnimator.ofFloat(v.findViewById(R.id.sbj_main_item_view), View.X, x - startX > 0 ? WIDTH_FOR_ANIMATION : 10).setDuration(DURATION_FOR_ANIMATION).start();
							openElement = (Integer) v.getTag();
							previousView = v;
						}
					}
			}
			return true;
		}
	};

	private View.OnClickListener mOnCheckbox = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			mListOfSubjects.get((Integer) v.getTag()).setSelected(((CheckBox) v).isChecked());
		}
	};

	public ArrayOfSubjectsAdapter(List<Subject> list, Context ctx) {
		mListOfSubjects = list;
		this.ctx = ctx;
		orientation = ctx.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			FragmentManager fm = ((MainActivity) ctx).getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(EDIT_FRAGMENT);
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
		Button btn;

		public ViewHolder(View view) {
			super(view);
			iw = (ImageView) view.findViewById(R.id.sbj_icon);
			tw = (TextView) view.findViewById(R.id.sbj_code);
			cb = (CheckBox) view.findViewById(R.id.sbj_checkbox);
			tw2 = (TextView) view.findViewById(R.id.sbj_descr);
			btn = (Button) view.findViewById(R.id.sbj_btn);
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
		ObjectAnimator.ofFloat(vh.itemView.findViewById(R.id.sbj_main_item_view), View.ALPHA, 1).setDuration(0).start();
		ObjectAnimator.ofFloat(vh.itemView.findViewById(R.id.sbj_main_item_view), View.X, position == openElement ? WIDTH_FOR_ANIMATION : 10).setDuration(0).start();
		vh.itemView.setBackgroundColor(currentPosition == position ? Color.argb(50, 10, 10, 10) : Color.WHITE);
		vh.btn.setEnabled(position == openElement);

		vh.cb.setChecked(sbj.getSelected());
		vh.tw.setText(sbj.getCode().toString());
		vh.iw.setImageResource(sbj.getIconRes());
		vh.tw2.setText(sbj.getDescription());

		vh.cb.setOnClickListener(mOnCheckbox);
		vh.itemView.setTag(position);
		vh.itemView.setOnTouchListener(onTouchListener);
		vh.cb.setTag(position);
		vh.btn.setTag(position);
		vh.btn.setOnTouchListener(onTouchListener);
	}

	@Override
	public int getItemCount() {
		return mListOfSubjects.size();
	}


}
