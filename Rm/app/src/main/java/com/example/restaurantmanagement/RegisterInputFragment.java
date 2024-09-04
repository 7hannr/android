package com.example.restaurantmanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

public class RegisterInputFragment extends Fragment {
    FoodVO food=new FoodVO(); EditText name, address, tel, description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_register_input, container, false);
        name = view.findViewById(R.id.name);
        address = view.findViewById(R.id.address);
        tel = view.findViewById(R.id.tel);
        description = view.findViewById(R.id.description);
        food=(FoodVO)getArguments().getSerializable("food");
        address.setText(food.getAddress());
        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                food.setName(name.getText().toString());
                food.setAddress(address.getText().toString());
                food.setTel(tel.getText().toString());
                food.setDescription(description.getText().toString());
                bundle.putSerializable("food", food);
                RegisterActivity registerActivity=(RegisterActivity)getActivity();
                registerActivity.replaceFragment("image", bundle);
            }
        });
        return view;
    } //onCreateView
} //RegisterInputFragment
