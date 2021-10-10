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

import com.example.myapplication.Bandak.diffutil.PaymentOutDiffutil;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class PaymentOutAdapter extends RecyclerView.Adapter<PaymentOutAdapter.PaymentOutHolder> implements Filterable {
    
        ArrayList<PaymentOutData> arraylist;
        ArrayList<PaymentOutData> backup;
        Context context;
    PaymentOutListener PaymentOutListener;

        public PaymentOutAdapter(ArrayList<PaymentOutData> arraylist5, Context context,PaymentOutListener PaymentOutListener) {
            arraylist = new ArrayList<>(arraylist5);
            backup = new ArrayList<>(arraylist5);
            this.context = context;
            this.PaymentOutListener = PaymentOutListener;
        }


        @NonNull
        @Override
        public PaymentOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflator = LayoutInflater.from(parent.getContext());

            View view = inflator.inflate(R.layout.payment_out_singlerow, parent, false);
           PaymentOutHolder holder = new PaymentOutHolder(view,PaymentOutListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(PaymentOutHolder holder, int position) {


            holder.singlerow_name.setText(arraylist.get(position).name);
            holder.singlerow_date.setText(arraylist.get(position).date);
            holder.rate.setText(String.valueOf(arraylist.get(position).rate));
            holder.singlerow_amount.setText(String.format("%.2f",(arraylist.get(position).amount)));
        /*holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // clicklistenerinterface.onclick(holder.cardView, arraylist.get(position).name, arraylist.get(position).id);
            }
        });*/
        }

        @Override
        public int getItemCount() {
            return arraylist.size();
        }

        public void setdata(ArrayList<PaymentOutData> newlist) {
            PaymentOutDiffutil diffutil = new PaymentOutDiffutil(arraylist, newlist);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffutil);
            arraylist.clear();
            arraylist.addAll(newlist);
            diffResult.dispatchUpdatesTo(this);


        }

        public void filterlist(ArrayList<PaymentOutData> filteredlist) {
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
                ArrayList<PaymentOutData> filterddata = new ArrayList<>();
                if (keyword == null ||newkeyword.isEmpty()) {

                    filterddata.addAll(backup);
                } else {
                    if(Pattern.matches("[a-zA-Z]+",keyword))
                    {
                        for (PaymentOutData data : backup) {

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
                            for (PaymentOutData data : backup) {

                                if (data.date.trim().contains(replacedkeyword.trim())) {
                                    filterddata.add(data);
                                }
                            }
                        }
                        else
                        {
                            for (PaymentOutData data : backup) {

                                if (data.date.trim().contains(newkeyword.trim())) {
                                    filterddata.add(data);
                                }
                            }
                        }


                    }
                    else if (!Pattern.matches("[a-zA-Z]+",keyword) && !newkeyword.contains("-")||!newkeyword.contains("/") )

                    {
                        for (PaymentOutData data : backup) {

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
                    arraylist.addAll((ArrayList<PaymentOutData>) filterResults.values);
                    notifyDataSetChanged();
                }


            }


        };


        class PaymentOutHolder extends RecyclerView.ViewHolder {
            TextView singlerow_name, singlerow_amount, singlerow_date,rate;
            CardView cardView;
            PaymentOutListener PaymentOutListenerH;
            public PaymentOutHolder(View itemView,PaymentOutListener PaymentOutListenerH) {
                super(itemView);
                this.PaymentOutListenerH=PaymentOutListenerH;
                singlerow_name = itemView.findViewById(R.id.PO_single_row_party_name);
                singlerow_amount = itemView.findViewById(R.id.PO_single_row_amount);
                singlerow_date = itemView.findViewById(R.id.PO_single_row_date);
                rate=itemView.findViewById(R.id.Po_single_row_rate);
                cardView = itemView.findViewById(R.id.PO_single_row_cardview);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PaymentOutListenerH.PaymentOutonclick( arraylist.get(getAdapterPosition()).name, arraylist.get(getAdapterPosition()).id);
                    }
                });
                cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        PaymentOutListenerH.PaymentOutonlongclick(cardView,getAdapterPosition(),arraylist.get(getAdapterPosition()).id);
                        return false;
                    }
                });
            }
        }


        public interface PaymentOutListener {
            void PaymentOutonclick(String value, int id);
            void PaymentOutonlongclick(View v,int adaptpos,int id);

        }


    }



