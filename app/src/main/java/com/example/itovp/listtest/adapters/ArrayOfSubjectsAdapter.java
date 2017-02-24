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
import com.example.itovp.listtest.asyncrone.Counter;
import com.example.itovp.listtest.entities.Subject;
import com.example.itovp.listtest.fragments.EditSbjFragment;

import java.util.List;

/**
 * Created by itovp on 31.01.2017.
 */

public class ArrayOfSubjectsAdapter extends RecyclerView.Adapter<ArrayOfSubjectsAdapter.ViewHolder> implements Counter.Timer {

	private List<Subject> mListOfSubjects;
	private Context ctx;
	private int orientation;
	private int openElement = -1;
	private int currentPosition = -1;//// TODO: 13.02.2017 what is that -_- refactor
	public boolean toClose;

	public static final int WIDTH_FOR_ANIMATION = 200;
	public static final int DURATION_FOR_ANIMATION = 1000;

	View previousView;

	private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
		float startX;

		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int x = (int) event.getRawX();
			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = x;
					break;

				case MotionEvent.ACTION_UP:
					if (v.getId() == R.id.sbj_btn) { // delete button
						mListOfSubjects.remove((int) v.getTag());
						notifyItemRemoved(openElement);
						if (currentPosition == openElement) currentPosition = -1;
						openElement = -1;
						startCounting(400);
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
							toClose = true;
							FragmentManager fm = ((MainActivity) ctx).getFragmentManager();
							FragmentTransaction ft = fm.beginTransaction();
							EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(MainActivity.EDIT_FRAGMENT);
							if (editSbjFragment == null) {
								editSbjFragment = EditSbjFragment.newInstance(mListOfSubjects.get(position));
								if (orientation == Configuration.ORIENTATION_PORTRAIT) {
									ft.replace(R.id.container_for_list, editSbjFragment);
									ft.addToBackStack(null);
								} else {
									ft.replace(R.id.container_for_edit_land, editSbjFragment, MainActivity.EDIT_FRAGMENT);
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
							toClose = false;
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
			EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(MainActivity.EDIT_FRAGMENT);
			if (editSbjFragment != null) {
				ft.remove(editSbjFragment);
			}
			ft.commit();
		}
	}

	@Override
	public void onFinishCounting() {
		notifyDataSetChanged();
	}

	@Override
	public void startCounting(long miliseconds) {
		new Counter(this, miliseconds).execute();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		ImageView iw;
		TextView tw, tw2;
		CheckBox cb;
		Button btn;
		View coveredView;

		public ViewHolder(View view) {
			super(view);

			coveredView = view.findViewById(R.id.sbj_main_item_view);
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
		if (position == openElement) {
			previousView = vh.coveredView;
		}
		ObjectAnimator.ofFloat(vh.itemView.findViewById(R.id.sbj_main_item_view), View.X, position == openElement ? WIDTH_FOR_ANIMATION : 10).setDuration(0).start();
		if (Configuration.ORIENTATION_LANDSCAPE == orientation && position == openElement && toClose) {
			ObjectAnimator.ofFloat(vh.itemView.findViewById(R.id.sbj_main_item_view), View.X, 10).setDuration(DURATION_FOR_ANIMATION).start();
			toClose = false;
		}
		vh.itemView.setBackgroundColor(currentPosition == position ? Color.argb(50, 10, 10, 10) : Color.WHITE);
		vh.cb.setChecked(sbj.getSelected());
		vh.tw.setText(sbj.getCode().toString());
		vh.iw.setImageResource(sbj.getIconRes());
		vh.tw2.setText(sbj.getDescription());

		vh.cb.setOnClickListener(mOnCheckbox);
		vh.cb.setTag(position);
		vh.coveredView.setTag(position);
		vh.coveredView.setOnTouchListener(onTouchListener);
		vh.coveredView.setAlpha(1);
		vh.btn.setTag(position);
		vh.btn.setOnTouchListener(onTouchListener);
		vh.btn.setEnabled(position == openElement);
	}

	@Override
	public int getItemCount() {
		return mListOfSubjects.size();
	}


}
