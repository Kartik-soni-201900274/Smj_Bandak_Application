package com.example.myapplication.Bandak;

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

import com.example.myapplication.Bandak.diffutil.diffutil_sop_adapter;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Show_All_Party_ADAPTER extends RecyclerView.Adapter<Show_All_Party_ADAPTER.Show_All_Party_holder> implements Filterable{


    ArrayList<show_All_party_Data> Show_All_Partyarraylist;
    ArrayList<show_All_party_Data> Show_All_Partybackup;
    Context context;
    sop_clickinterface sop_clickinterface;

    public Show_All_Party_ADAPTER(ArrayList<show_All_party_Data> arraylist5, Context context,sop_clickinterface sop_clickinterface) {
        Show_All_Partyarraylist = new ArrayList<>(arraylist5);
        Show_All_Partybackup = new ArrayList<>(arraylist5);
         this.sop_clickinterface=sop_clickinterface;

        this.context = context;

    }
    public void setdata(ArrayList<show_All_party_Data> newlist) {
        diffutil_sop_adapter diffutil = new diffutil_sop_adapter(Show_All_Partyarraylist, newlist);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffutil);
        Show_All_Partyarraylist.clear();
        Show_All_Partyarraylist.addAll(newlist);
        diffResult.dispatchUpdatesTo(this);


    }
    public void notifyitemremoved(int pos)
    {
        Show_All_Partyarraylist.remove(pos);
        notifyItemRemoved(pos);
    }

    @NonNull
    @Override
    public Show_All_Party_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(parent.getContext());

        View view = inflator.inflate(R.layout.showallparty_singlerow, parent, false);
        Show_All_Party_holder holder = new Show_All_Party_holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(Show_All_Party_holder holder, int position) {


        holder.sop_name.setText(Show_All_Partyarraylist.get(position).name);
        holder.sop_contact.setText(Show_All_Partyarraylist.get(position).contact);
        holder.sop_balance.setText(String.format("%.2f",Show_All_Partyarraylist.get(position).balance));


    }


    @Override
    public int getItemCount() {
        return Show_All_Partyarraylist.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<show_All_party_Data> filterddata = new ArrayList<>();
            if (keyword == null || keyword.toString().isEmpty()) {

                filterddata.addAll(Show_All_Partybackup);
            } else {
                if(Pattern.matches("[a-zA-Z]+",keyword))
                {
                    for (show_All_party_Data data : Show_All_Partybackup) {

                        if (data.name.toLowerCase().trim().startsWith(keyword.toString().toLowerCase().trim())) {
                            filterddata.add(data);
                        }
                    }

                }
                else if (!Pattern.matches("[a-zA-Z]+",keyword))
                {
                    for (show_All_party_Data data : Show_All_Partybackup) {

                        if (data.contact.toLowerCase().trim().contains(keyword.toString().toLowerCase().trim())) {
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

                Show_All_Partyarraylist.clear();
                Show_All_Partyarraylist.addAll((ArrayList<show_All_party_Data>) filterResults.values);
                notifyDataSetChanged();
            }


        }


    };

    class Show_All_Party_holder extends RecyclerView.ViewHolder {
        TextView sop_name, sop_contact,sop_balance;
        CardView sop_cardview;


        public Show_All_Party_holder(View itemView) {
            super(itemView);

            sop_cardview = itemView.findViewById(R.id.sop_cardview);
            sop_name = itemView.findViewById(R.id.sop_party_name);
            sop_balance=itemView.findViewById(R.id.sop_balance);
            sop_contact = itemView.findViewById(R.id.sop_number);


            sop_cardview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    sop_clickinterface.onlongclick(sop_cardview,getAdapterPosition());
                    return false;
                }
            });
           sop_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sop_clickinterface.oncontactclick(Show_All_Partyarraylist.get(getAdapterPosition()).contact);
                }
            });
           sop_cardview.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   sop_clickinterface.onclick(Show_All_Partyarraylist.get(getAdapterPosition()).name);
               }
           });
        }

    }
   public  interface sop_clickinterface{
        void oncontactclick(String contact);
        void onlongclick(CardView v,int pos);
        void onclick(String name);
    }

}


