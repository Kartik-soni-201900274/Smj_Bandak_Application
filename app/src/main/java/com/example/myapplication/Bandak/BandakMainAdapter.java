package com.example.myapplication.Bandak;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.RecievableFragment;

public class BandakMainAdapter extends FragmentStateAdapter {
    Context context;
    public BandakMainAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
        super(fragmentActivity);
        this.context=context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==0)
        {
            return new RecievableFragment(position,context);
        }
        else
        {
            return new RecievableFragment(position,context);
        }




    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
