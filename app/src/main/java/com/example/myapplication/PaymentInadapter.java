package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Bandak.diffutil.PaymentInDiffutil;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class PaymentInadapter extends RecyclerView.Adapter<PaymentInadapter.PaymentInHolder> implements Filterable {

    ArrayList<PaymentInData> arraylist;
    ArrayList<PaymentInData> backup;
    Context context;
    PaymentInListener PaymentInListener;

    public PaymentInadapter(ArrayList<PaymentInData> arraylist5, Context context, PaymentInListener PaymentInListener) {
        arraylist = new ArrayList<>(arraylist5);
        backup = new ArrayList<>(arraylist5);
        this.context = context;
        this.PaymentInListener = PaymentInListener;
    }


    @NonNull
    @Override
    public PaymentInHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());

        View view = inflator.inflate(R.layout.payment_in_single_row, parent, false);
        PaymentInHolder holder = new PaymentInHolder(view,PaymentInListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(PaymentInadapter.PaymentInHolder holder, int position) {


        holder.singlerow_name.setText(arraylist.get(position).name);
        holder.singlerow_date.setText(arraylist.get(position).date);
        holder.singlerow_amount.setText(String.format("%.2f",(arraylist.get(position).amount)));
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // PaymentInListener.onclick(holder.cardView, arraylist.get(position).name, arraylist.get(position).id);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public void setdata(ArrayList<PaymentInData> newlist) {
        PaymentInDiffutil diffutil = new PaymentInDiffutil(arraylist, newlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffutil);
        arraylist.clear();
        arraylist.addAll(newlist);
        diffResult.dispatchUpdatesTo(this);


    }

    public void filterlist(ArrayList<PaymentInData> filteredlist) {
        arraylist = filteredlist;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            String newkeyword=keyword.toString();
            ArrayList<PaymentInData> filterddata = new ArrayList<>();
            if (keyword == null ||newkeyword.isEmpty()) {

                filterddata.addAll(backup);
            } else {
                if(Pattern.matches("[a-zA-Z]+",keyword))
                {
                    for (PaymentInData data : backup) {

                        if (data.name.toLowerCase().trim().startsWith(newkeyword.toLowerCase().trim())) {
                            filterddata.add(data);
                        }
                    }
                }
                else if(newkeyword.contains("-")||newkeyword.contains("/"))
                {
                     if(newkeyword.contains("/"))
                     {
                            String replacedkeyword=newkeyword.replaceAll("/","-");
                            for (PaymentInData data : backup) {

                                if (data.date.trim().contains(replacedkeyword.trim())) {
                                    filterddata.add(data);
                                }
                            }
                     }
                        else
                        {
                            for (PaymentInData data : backup) {

                                if (data.date.trim().contains(newkeyword.trim())) {
                                    filterddata.add(data);
                                }
                            }
                        }


                }
                else if (!Pattern.matches("[a-zA-Z]+",keyword) && !newkeyword.contains("-")||!newkeyword.contains("/") )

                {
                    for (PaymentInData data : backup) {

                        if (String.valueOf(data.amount).trim().startsWith(newkeyword.trim())) {
                            filterddata.add(data);
                        }
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterddata;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            if (filterResults.values != null) {

                arraylist.clear();
                arraylist.addAll((ArrayList<PaymentInData>) filterResults.values);
                notifyDataSetChanged();
            }


        }


    };


    class PaymentInHolder extends RecyclerView.ViewHolder {
        TextView singlerow_name, singlerow_amount, singlerow_date;
        CardView cardView;
        PaymentInListener PaymentInListenerH;
        public PaymentInHolder(View itemView,PaymentInListener PaymentInListenerH) {
            super(itemView);
            this.PaymentInListenerH=PaymentInListenerH;
            singlerow_name = itemView.findViewById(R.id.PI_single_row_party_name);
            singlerow_amount = itemView.findViewById(R.id.PI_single_row_amount);
            singlerow_date = itemView.findViewById(R.id.PI_single_row_date);
            cardView = itemView.findViewById(R.id.PI_single_row_cardview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PaymentInListenerH.PaymentInonclick( arraylist.get(getAdapterPosition()).name, arraylist.get(getAdapterPosition()).id);
                }
            });
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    PaymentInListenerH.PaymentInonlongclick(cardView,getAdapterPosition(),arraylist.get(getAdapterPosition()).id);
                    return false;
                }
            });
        }
    }


    public interface PaymentInListener {
        void PaymentInonclick(String value, int id);
        void PaymentInonlongclick(View v,int adaptpos,int id);

    }


}

