package com.example.myapplication.Bandak.diffutil;

import androidx.recyclerview.widget.DiffUtil;

import com.example.myapplication.PaymentOutData;

import java.util.ArrayList;

public class PaymentOutDiffutil extends DiffUtil.Callback {
   
        ArrayList<PaymentOutData> newlist=new ArrayList<>();
        ArrayList<PaymentOutData> oldlist=new ArrayList<>();

        public PaymentOutDiffutil(ArrayList<PaymentOutData> oldlist,ArrayList<PaymentOutData> newlist) {
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


