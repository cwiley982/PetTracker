package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ManagePetsFragment extends Fragment implements View.OnClickListener {

    private View mFragView;
    private FloatingActionButton mFab;
    private AddPetDialogFragment mDiag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.manage_pets_fragment, container, false);

        mDiag = new AddPetDialogFragment();

        mFab = mFragView.findViewById(R.id.add_pet_fab);
        mFab.setOnClickListener(this);
        return mFragView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_pet_fab:
                mDiag.show(getChildFragmentManager(), "");
                // open diag view to add new pet
                // ask for name, age, gender, species (dog, cat, bird, etc), opt birthday, opt breed
        }
    }
}
