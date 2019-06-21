package com.caitlynwiley.pettracker;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddPetActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.choose_new_or_existing_pet);

        Button petIdOption = findViewById(R.id.enter_pet_id_button);
        petIdOption.setOnClickListener(this);

        Button createPetOption = findViewById(R.id.create_pet_button);
        createPetOption.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.enter_pet_id_button:
                getSupportFragmentManager().beginTransaction().replace(R.id.add_pet_frag_view, new AddByIdFragment()).commit();
                break;
            case R.id.create_pet_button:
                getSupportFragmentManager().beginTransaction().replace(R.id.add_pet_frag_view, new CreatePetFragment()).commit();
                break;
        }
    }
}
