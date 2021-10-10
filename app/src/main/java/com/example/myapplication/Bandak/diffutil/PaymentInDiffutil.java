package com.example.myapplication.Bandak.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.PaymentInData;

import java.util.ArrayList;

public class PaymentInDiffutil extends DiffUtil.Callback {
    ArrayList<PaymentInData> newlist=new ArrayList<>();
    ArrayList<PaymentInData> oldlist=new ArrayList<>();

    public PaymentInDiffutil(ArrayList<PaymentInData> oldlist,ArrayList<PaymentInData> newlist ) {
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
}
