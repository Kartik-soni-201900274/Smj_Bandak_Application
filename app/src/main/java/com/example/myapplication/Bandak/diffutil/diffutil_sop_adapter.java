package com.example.myapplication.Bandak.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.Bandak.show_All_party_Data;

import java.util.ArrayList;

public class diffutil_sop_adapter extends DiffUtil.Callback{
    ArrayList<show_All_party_Data> newlist=new ArrayList<>();
    ArrayList<show_All_party_Data> oldlist=new ArrayList<>();
    public diffutil_sop_adapter(ArrayList<show_All_party_Data> newlist, ArrayList<show_All_party_Data> oldlist) {
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
        return oldlist.get(oldItemPosition).name==newlist.get(newItemPosition).name;
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
}
