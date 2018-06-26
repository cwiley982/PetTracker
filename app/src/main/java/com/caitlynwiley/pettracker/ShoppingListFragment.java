package com.caitlynwiley.pettracker;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListFragment extends ListFragment {

    List mList;
    ArrayAdapter mAdadpter;
    FloatingActionButton fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList();
        fab = getView().findViewById(R.id.shopping_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open a dialog that allows user to type in an item?
                // Maybe just add a new row to the list that they can type in
            }
        });
        mAdadpter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_checked, mList);
        mAdadpter.setNotifyOnChange(true);
        setListAdapter(mAdadpter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.shopping_list_fragment, container, false);
    }
}
