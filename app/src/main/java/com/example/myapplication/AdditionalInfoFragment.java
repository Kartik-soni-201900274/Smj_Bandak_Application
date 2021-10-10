package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Bandak.OPbalanceFragment;
import com.google.android.material.textfield.TextInputEditText;

public class AdditionalInfoFragment extends Fragment {
    adiFragmentClickListener fragmentClickListener;
    Context context;



    public AdditionalInfoFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.activity_additional_info_fragment, container, false);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentClickListener = (adiFragmentClickListener) context;
        fragmentClickListener.adiFragmentattached();

    }

    public interface adiFragmentClickListener{

        void adiFragmentattached();
    }
}
