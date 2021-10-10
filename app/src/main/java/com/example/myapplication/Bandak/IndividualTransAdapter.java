package com.example.myapplication.Bandak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Bandak.diffutil.diffutil_individual_Adapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class IndividualTransAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    ArrayList<Individual_trans_data> IndividualTransarraylist;
    ArrayList<Individual_trans_data> IndividualTransbackup;
    Context context;
    ClickInterface clickinterface;

    @Override
    public int getItemViewType(int position) {

        if(IndividualTransarraylist.get(position).type==0)
        {
            return 0;
        }
        else
        {
            return 1;
        }


    }

    public IndividualTransAdapter(ArrayList<Individual_trans_data> arraylist5, Context context, ClickInterface clickinterface) {
        IndividualTransarraylist = new ArrayList<>(arraylist5);
        IndividualTransbackup = new ArrayList<>(arraylist5);
        this.clickinterface = clickinterface;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());
        View view = null;
        if (viewType == 0) {
            view = inflator.inflate(R.layout.singlerow_pi, parent, false);
            return new paymentin_holder(view);
        }

        view = inflator.inflate(R.layout.single_rowpo, parent, false);
        return new paymentout_holder(view);


    }

    public void refreshafterdelete(int adptpos) {
        IndividualTransarraylist.remove(adptpos);
        notifyItemRemoved(adptpos);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if(IndividualTransarraylist.get(position).type==0)
    {
        paymentin_holder holderpi = (paymentin_holder) holder;
        holderpi.pi_singlerow_date.setText(IndividualTransarraylist.get(position).date);
        holderpi.pi_singlerow_amount.setText(String.format("%.2f",(IndividualTransarraylist.get(position).amount)));
        holderpi.pi_singlerow_desc.setText(IndividualTransarraylist.get(position).desc);
    }
    else if(IndividualTransarraylist.get(position).type==1)
    {
        paymentout_holder holderpo=(paymentout_holder) holder;
        holderpo.pi_singlerow_rate.setText(String.valueOf(IndividualTransarraylist.get(position).rate));
        holderpo.po_singlerow_amount.setText(String.format("%.2f",(IndividualTransarraylist.get(position).amount)));
        holderpo.po_singlerow_date.setText(IndividualTransarraylist.get(position).date);
        holderpo.po_singlerow_desc.setText(IndividualTransarraylist.get(position).desc);
    }
    }

    public void setdata(ArrayList<Individual_trans_data> newlist) {
        diffutil_individual_Adapter diffutil_individual_adapter = new diffutil_individual_Adapter(IndividualTransarraylist, newlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffutil_individual_adapter);
        IndividualTransarraylist.clear();
        IndividualTransarraylist.addAll(newlist);
        diffResult.dispatchUpdatesTo(this);


    }

    public void refreshlist(ArrayList<Individual_trans_data> newlist) {
        IndividualTransarraylist.clear();
        IndividualTransarraylist.addAll(newlist);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return IndividualTransarraylist.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            String newkeyword = keyword.toString();
            ArrayList<Individual_trans_data> filterddata = new ArrayList<>();
            if (keyword == null || newkeyword.isEmpty()) {

                filterddata.addAll(IndividualTransbackup);
            } else {

                if (newkeyword.contains("-") || newkeyword.contains("/")) {
                    if (newkeyword.contains("/")) {
                        String replacedkeyword = newkeyword.replaceAll("/", "-");
                        for (Individual_trans_data data : IndividualTransbackup) {

                            if (data.date.trim().contains(replacedkeyword.trim())) {
                                filterddata.add(data);
                            }
                        }
                    } else {
                        for (Individual_trans_data data : IndividualTransbackup) {

                            if (data.date.trim().contains(newkeyword.trim())) {
                                filterddata.add(data);
                            }
                        }
                    }


                } else if (!Pattern.matches("[a-zA-Z]+", keyword) && !newkeyword.contains("-") || !newkeyword.contains("/")) {
                    for (Individual_trans_data data : IndividualTransbackup) {

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

                IndividualTransarraylist.clear();
                IndividualTransarraylist.addAll((ArrayList<Individual_trans_data>) filterResults.values);
                notifyDataSetChanged();
            }


        }


    };

    class paymentin_holder extends RecyclerView.ViewHolder {
        TextView pi_singlerow_desc, pi_singlerow_amount, pi_singlerow_date;
        CardView pi_cardView;
        ImageButton pi_expandButton;
        ClickInterface clickInterfaceH;

        public paymentin_holder(View itemView) {
            super(itemView);

            pi_singlerow_desc = itemView.findViewById(R.id.pi_single_row_description);
            pi_singlerow_amount = itemView.findViewById(R.id.pisingle_row_amount);
            pi_singlerow_date = itemView.findViewById(R.id.pisingle_row_date);
            pi_cardView = itemView.findViewById(R.id.pi_singlerow_cardview);

            pi_expandButton = itemView.findViewById(R.id.piexpandbtn);
            pi_expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickinterface.displayextended(IndividualTransarraylist.get(getAdapterPosition()).id,IndividualTransarraylist.get(getAdapterPosition()).type);
                }
            });
            pi_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickinterface.edit(IndividualTransarraylist.get(getAdapterPosition()).id,getAdapterPosition(),IndividualTransarraylist.get(getAdapterPosition()).type);
                }
            });

        }
    }

    class paymentout_holder extends RecyclerView.ViewHolder {
        TextView po_singlerow_desc, po_singlerow_amount, po_singlerow_date,pi_singlerow_rate;
        CardView po_cardView;
        ImageButton po_expandButton;


        public paymentout_holder(View itemView) {
            super(itemView);
            pi_singlerow_rate=itemView.findViewById(R.id.posinglerow_rate);
            po_singlerow_desc = itemView.findViewById(R.id.posinglerow_desc);
            po_singlerow_amount = itemView.findViewById(R.id.posingle_row_amount);
            po_singlerow_date = itemView.findViewById(R.id.posingle_row_date);
            po_cardView = itemView.findViewById(R.id.po_singlerow_cardview);

            po_expandButton = itemView.findViewById(R.id.posinglerow_expandbtn);
            po_expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickinterface.displayextended(IndividualTransarraylist.get(getAdapterPosition()).id,IndividualTransarraylist.get(getAdapterPosition()).type);
                }
            });

            po_cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickinterface.edit(IndividualTransarraylist.get(getAdapterPosition()).id,getAdapterPosition(),IndividualTransarraylist.get(getAdapterPosition()).type);
                }
            });
        }
    }


    public interface ClickInterface {
        void edit(int id, int adptpos,int type);

        void displayextended(int id,int type);

    }
}