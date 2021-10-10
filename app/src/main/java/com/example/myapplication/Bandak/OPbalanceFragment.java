package com.example.myapplication.Bandak;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;


public class OPbalanceFragment extends Fragment {
    FragmentClickListener fragmentClickListener;
    Context context;

   TextInputEditText addpartyaddress, addpartytelephone, addpartyadditionalinfo, addpartyprincipalbalance, addpartyinterestbalance, addpartyopblncdate;

    public OPbalanceFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.openingbalancefragment, container, false);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentClickListener = (FragmentClickListener) context;
        fragmentClickListener.attached();

    }

    interface FragmentClickListener{
        void attached();
    }
}
