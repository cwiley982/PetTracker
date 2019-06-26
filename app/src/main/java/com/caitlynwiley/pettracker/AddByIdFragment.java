package com.caitlynwiley.pettracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddByIdFragment extends Fragment {

    private View mFragView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mFragView = inflater.inflate(R.layout.enter_pet_id_layout, container, false);

        mFragView.findViewById(R.id.add_pet_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((EditText) mFragView.findViewById(R.id.new_pet_id)).getText().toString();
                FirebaseDatabase.getInstance().getReference().child("accounts")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("pets")
                        .child(id)
                        .setValue(true);
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return mFragView;
    }
}