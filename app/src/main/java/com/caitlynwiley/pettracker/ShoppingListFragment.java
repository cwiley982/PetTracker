package com.caitlynwiley.pettracker;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends ListFragment {

    List mList;
    ArrayAdapter mAdapter;
    FloatingActionButton fab;
    Animation mRotateForward;
    Animation mRotateBackward;
    View mFragView;

    boolean fabOpen;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList();
        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_checked, mList);
        mAdapter.setNotifyOnChange(true);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);
        fab = mFragView.findViewById(R.id.shopping_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabOpen) {
                    fab.startAnimation(mRotateBackward);
                } else {
                    fab.startAnimation(mRotateForward);
                }
                fabOpen = !fabOpen;
                // open a dialog that allows user to type in an item?
                // Maybe just add a new row to the list that they can type in
            }
        });
        fabOpen = false;
        return mFragView;
    }
}
