package com.example.myapplication.Bandak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends ArrayAdapter<MyContacts> {
    Context context;
    List<MyContacts> contactsArrayList;
    List<MyContacts> backup;

    public ContactsAdapter(@NonNull Context context, int resource, @NonNull List<MyContacts> objects) {
        super(context, resource, objects);
        this.contactsArrayList = objects;
        this.context=context;
        this.backup=objects;
    }


    @Override
    public int getCount() {
        return contactsArrayList.size();
    }

    @Nullable
    @Override
    public MyContacts getItem(int position) {
        return contactsArrayList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ContactsViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(R.layout.simplelist2, parent, false);
            holder = new ContactsViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ContactsViewHolder) row.getTag();
        }
        holder.name.setText(((MyContacts) getItem(position)).name);
        holder.phone.setText(((MyContacts) getItem(position)).number);
        return row;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {

            ArrayList<MyContacts> filterddata = new ArrayList<>();
            if (keyword == null || keyword.length()==0) {

                filterddata.addAll(backup);


            } else {
                String newkeyword = keyword.toString();
                    for (MyContacts data : backup) {

                        if (data.name.toLowerCase().trim().startsWith(newkeyword.toLowerCase().trim())) {
                            filterddata.add(data);
                        }
                    }

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterddata;
                filterResults.count=filterddata.size();
                return filterResults;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.values != null && filterResults.count>0) {

                contactsArrayList.clear();
                contactsArrayList.addAll((ArrayList<MyContacts>) filterResults.values);
                notifyDataSetChanged();
            }
        }
    };
}

class ContactsViewHolder{
TextView name,phone;
    public ContactsViewHolder(View v) {
        name=v.findViewById(R.id.simplelistname);
        phone=v.findViewById(R.id.simplelistphone);
    }
}
