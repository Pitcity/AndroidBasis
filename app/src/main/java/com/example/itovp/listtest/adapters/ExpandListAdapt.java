package com.example.itovp.listtest.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.itovp.listtest.R;
import com.example.itovp.listtest.activities.MainActivity;
import com.example.itovp.listtest.entities.Subject;
import com.example.itovp.listtest.fragments.EditSbjFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itovp on 23.02.2017.
 */

public class ExpandListAdapt extends BaseExpandableListAdapter implements View.OnClickListener {
	Context mCtx;
	ArrayList<Subject> mItems;

	public static final int AMMOUNT_OF_ELEMENTS_IN_GROUP = 10;

	public ExpandListAdapt(Context ctx, ArrayList<Subject> sbj) {
		mCtx = ctx;
		mItems = sbj;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
	}

	@Override
	public int getGroupCount() {//// TODO: 24.02.2017 refactor
		return mItems.size() > AMMOUNT_OF_ELEMENTS_IN_GROUP ? (int) Math.ceil(mItems.size() / AMMOUNT_OF_ELEMENTS_IN_GROUP) : 1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mItems.size() > AMMOUNT_OF_ELEMENTS_IN_GROUP ? groupPosition != getGroupCount() - 1
				? AMMOUNT_OF_ELEMENTS_IN_GROUP : mItems.size() - (groupPosition - 1) * AMMOUNT_OF_ELEMENTS_IN_GROUP : mItems.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return mItems.get(mItems.size() > AMMOUNT_OF_ELEMENTS_IN_GROUP ? groupPosition * AMMOUNT_OF_ELEMENTS_IN_GROUP + childPosition : childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.parent_explist_item, null);
		}
		((TextView) convertView.findViewById(R.id.parent_list_text)).setText((groupPosition * AMMOUNT_OF_ELEMENTS_IN_GROUP) + " - " + ((groupPosition + 1) * AMMOUNT_OF_ELEMENTS_IN_GROUP));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.subject_list_item, null);
		}
		//// TODO: 24.02.2017 refactor
		Subject current = mItems.get(mItems.size() > AMMOUNT_OF_ELEMENTS_IN_GROUP ? groupPosition * AMMOUNT_OF_ELEMENTS_IN_GROUP + childPosition : childPosition);
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.sbj_checkbox);
		cb.setChecked(current.getSelected());
		cb.setEnabled(false);
		((TextView) convertView.findViewById(R.id.sbj_code)).setText(current.getCode().toString());
		((TextView) convertView.findViewById(R.id.sbj_descr)).setText(current.getDescription());
		ImageView iv = (ImageView) convertView.findViewById(R.id.sbj_icon);
		iv.setImageResource(current.getIconRes());

		convertView.setOnClickListener(this);
		convertView.setTag(groupPosition * AMMOUNT_OF_ELEMENTS_IN_GROUP + childPosition);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public void notifyDataSetInvalidated() {
		super.notifyDataSetInvalidated();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return super.areAllItemsEnabled();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getCombinedChildId(long groupId, long childId) {
		return super.getCombinedChildId(groupId, childId);
	}

	@Override
	public long getCombinedGroupId(long groupId) {
		return super.getCombinedGroupId(groupId);
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}

	@Override
	public int getChildType(int groupPosition, int childPosition) {
		return super.getChildType(groupPosition, childPosition);
	}

	@Override
	public int getChildTypeCount() {
		return super.getChildTypeCount();
	}

	@Override
	public int getGroupType(int groupPosition) {
		return super.getGroupType(groupPosition);
	}

	@Override
	public int getGroupTypeCount() {
		return super.getGroupTypeCount();
	}

	public void addItems(List<Subject> listToAdd) {
		mItems.addAll(listToAdd);
		notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		int position = (Integer) v.getTag();
		notifyDataSetChanged();
		FragmentManager fm = ((MainActivity) mCtx).getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		EditSbjFragment editSbjFragment = (EditSbjFragment) fm.findFragmentByTag(MainActivity.EDIT_FRAGMENT);

		if (editSbjFragment == null) {
			editSbjFragment = EditSbjFragment.newInstance(mItems.get(position));
			if (mCtx.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				ft.replace(R.id.container_for_list, editSbjFragment);
				ft.addToBackStack(null);
			} else {
				ft.replace(R.id.container_for_edit_land, editSbjFragment, MainActivity.EDIT_FRAGMENT);
			}
			ft.commit();
		} else {
			editSbjFragment.setCurrentItem(mItems.get(position));
		}
	}
}
