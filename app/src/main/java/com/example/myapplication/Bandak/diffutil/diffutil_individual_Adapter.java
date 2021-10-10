package com.example.myapplication.Bandak.diffutil;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.Bandak.Individual_trans_data;

import java.util.ArrayList;

public class diffutil_individual_Adapter extends DiffUtil.Callback{
    ArrayList<Individual_trans_data> newlist=new ArrayList<>();
    ArrayList<Individual_trans_data> oldlist=new ArrayList<>();
    public diffutil_individual_Adapter(ArrayList<Individual_trans_data> newlist, ArrayList<Individual_trans_data> oldlist) {
        this.newlist = newlist;
        this.oldlist = oldlist;
    }

    @Override
    public int getOldListSize() {
        return oldlist!=null?oldlist.size():0;
    }

    @Override
    public int getNewListSize() {
        return newlist!=null?newlist.size():0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldlist.get(oldItemPosition).id==newlist.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result=newlist.get(newItemPosition).compareto(oldlist.get(oldItemPosition));
        if(result==0)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    @Nullable

    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
