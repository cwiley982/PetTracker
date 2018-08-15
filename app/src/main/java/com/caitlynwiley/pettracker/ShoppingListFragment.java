package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShoppingListFragment extends ListFragment {

    List mList;
    ArrayAdapter mAdapter;
    Animation mRotateForward;
    Animation mRotateBackward;
    EditText mNewItemEditText;
    ImageButton mAddButton;
    View mFragView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList();
        mAdapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.shopping_list_item, mList);
        mAdapter.setNotifyOnChange(true);
        setListAdapter(mAdapter);
        // Line below is causing problems
        //getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        mRotateForward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_forward);
        mRotateBackward = AnimationUtils.loadAnimation(getContext(), R.anim.fab_spin_backward);
        mNewItemEditText = mFragView.findViewById(R.id.new_list_item);
        mAddButton = mFragView.findViewById(R.id.add_list_item);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = mNewItemEditText.getText().toString();
                mList.add(newItem);
                mNewItemEditText.setText("");
                mAdapter.notifyDataSetChanged();
            }
        });
        return mFragView;
    }
}
