package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ChooseAdditionTypeFragment extends Fragment {
    private View mFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.choose_new_or_existing_pet, container, false);

        Button petIdOption = mFragView.findViewById(R.id.add_existing_button);
        petIdOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.add_pet_frag_view, new AddByIdFragment()).commit();
            }
        });

        Button createPetOption = mFragView.findViewById(R.id.create_new_button);
        createPetOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.add_pet_frag_view, new CreatePetFragment()).commit();
            }
        });

        return mFragView;
    }
}
