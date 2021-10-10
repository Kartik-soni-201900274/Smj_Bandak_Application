package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myapplication.Bandak.BandakAppDatabaseAdapter;
import com.example.myapplication.Bandak.DisplayTransParticularparty;
import com.example.myapplication.Bandak.Fragment_payment_in;
import com.example.myapplication.Bandak.Fragment_payment_out;
import com.example.myapplication.Bandak.WrapContentLinearLayoutManager;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecievableFragment extends Fragment implements PaymentInadapter.PaymentInListener, PaymentOutAdapter.PaymentOutListener, View.OnCreateContextMenuListener {
    Set<String> names = new HashSet<String>();
    TextInputEditText bndkmnedittext;
    PaymentInData paymentInData;
    PaymentOutData paymentOutData;
    RecyclerView Recycler;
    public PaymentInadapter paymentInadapter;
    public PaymentOutAdapter paymentOutAdapter;

    BandakAppDatabaseAdapter bandakAppDatabaseAdapter;
    ArrayList<PaymentInData> Paymentinarraylist = new ArrayList<>();
    ArrayList<PaymentOutData> Paymentoutarraylist = new ArrayList<>();

    View view;
    TextView textView1, textView2, total;
    ConstraintLayout constraintLayout;
    int paymentInadapterPosition = 0, id, counter;
    Context context;
    Cursor cursorshowall;
    int position;
    RecievableFragment contextthis = this;
    int adaptposition;

    public RecievableFragment(int position, Context context) {

        this.position = position;
        this.context = context;


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.receivable_fragment, container, false);
        setviewstoid(v);


        bndkmnedittext = v.findViewById(R.id.bndksearchedittext);
        bndkmnedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(position==0)
                paymentInadapter.getFilter().filter(charSequence);
                else
                    paymentOutAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //filter(editable.toString());
            }
        });
        // paymentInadapter.getFilter().filter(bndkmnedittext.getText().toString());


        return v;
    }

    public void taskdone() {
        if (paymentInadapter == null && position == 0) {
            managerecyclerPaymentIn();

        } else if (paymentOutAdapter == null && position == 1) {
            managerecyclerPaymentOut();
        } else if (paymentInadapter != null && position == 0) {

                 paymentInadapter.setdata(Paymentinarraylist);
        } else if (paymentOutAdapter != null && position == 1) {
//
            paymentOutAdapter.setdata(Paymentoutarraylist);
        }


    }

    public void onResume() {
        super.onResume();

        if (paymentInadapter == null && position == 0)
        {

            new Asyncgetdata().execute();
        } else if (paymentInadapter != null && position == 0) {

           Paymentinarraylist.clear();
            new Asyncgetdata().execute();
        } else if (paymentOutAdapter == null && position == 1) {
            new Asyncgetdata().execute();
        } else if (paymentOutAdapter != null && position == 1) {

            Paymentoutarraylist.clear();
            new Asyncgetdata().execute();
        }
    }

    public void setviewstoid(View v) {
        Recycler = v.findViewById(R.id.recievablerecyclerView);

    }


    public void filter(String text) {
        ArrayList<PaymentInData> filteredlist = new ArrayList<>();
        for (PaymentInData data : Paymentinarraylist) {
            if (data.name.toLowerCase().startsWith(text.toLowerCase())) {
                filteredlist.add(data);
            }
        }
        paymentInadapter.filterlist(filteredlist);
    }

    @Override
    public void PaymentInonclick(String value, int id) {

        this.id = id;
        Intent i = new Intent(getActivity(), DisplayTransParticularparty.class);
        i.putExtra("party name", value);
        i.putExtra("selector", position);
        i.putExtra("id", id);
        startActivity(i);
    }

    void createpopupmenu(View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.getMenuInflater().inflate(R.menu.longpressmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.lpedit: {
                        edit(position);

                        return true;
                    }
                    case R.id.lpdelete: {
                        if (position == 0) {
                            bandakAppDatabaseAdapter.deleteindividualtrans(id, position);
                            paymentInadapter.arraylist.remove(adaptposition);
                            paymentInadapter.notifyItemRemoved(adaptposition);
                            Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                            return true;
                        }
                        else
                        {
                            bandakAppDatabaseAdapter.deleteindividualtrans(id, position);
                            paymentOutAdapter.arraylist.remove(adaptposition);
                            paymentOutAdapter.notifyItemRemoved(adaptposition);
                            Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_LONG).show();
                            return true;
                        }

                    }
                    default: {
                        return true;
                    }
                }
            }
        });
        popupMenu.show();
    }

    @Override
    public void PaymentInonlongclick(View v, int adapterposition, int id) {

        this.id = id;
        this.adaptposition = adapterposition;
        createpopupmenu(v);
    }


    public void edit(int position) {




        Cursor cursor = bandakAppDatabaseAdapter.getColoumnWithID(id, position);
        String name = null, date = null, narration = null, spinner = null;
        float amount = 0, rate = 0;

        if (position == 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(4);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(3);
                id = cursor.getInt(5);
            }
            Intent i = new Intent(getActivity(), Fragment_payment_in.class);
            i.putExtra("name", name);

            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            i.putExtra("id", id);
            i.putExtra("pos", position);
            i.putExtra("adptpos", adaptposition);


            startActivity(i);
        } else {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(5);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(4);
                rate = cursor.getFloat(3);
                id = cursor.getInt(6);

            }
            Intent i = new Intent(getActivity(), Fragment_payment_out.class);
            i.putExtra("name", name);
            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            i.putExtra("rate", rate);
            i.putExtra("id", id);
            i.putExtra("pos", position);
            i.putExtra("adptpos", adaptposition);
            startActivity(i);
        }


    }


    public void managerecyclerPaymentIn() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        Recycler.setLayoutManager(wrapContentLinearLayoutManager);
        paymentInadapter = new PaymentInadapter((Paymentinarraylist), context, this);
        Recycler.setAdapter(paymentInadapter);
    }

    public void managerecyclerPaymentOut() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager = new WrapContentLinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        Recycler.setLayoutManager(wrapContentLinearLayoutManager);
        paymentOutAdapter = new PaymentOutAdapter((Paymentoutarraylist), context, this);
        Recycler.setAdapter(paymentOutAdapter);
    }

    @Override
    public void PaymentOutonclick(String value, int id) {
        this.id = id;
        Intent i = new Intent(getActivity(), DisplayTransParticularparty.class);
        i.putExtra("party name", value);
        i.putExtra("selector", position);
        i.putExtra("id", id);
        startActivity(i);
    }

    @Override
    public void PaymentOutonlongclick(View v, int adaptpos, int id) {
        this.id = id;
        this.adaptposition = adaptpos;
        createpopupmenu(v);
    }

    class Asyncgetdata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            taskdone();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (position == 0) {

                collectdataPaymentIn();
            } else {

                collectdataPaymentOut();
            }

            return null;
        }

        public void collectdataPaymentIn() {

            bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(getActivity());
            cursorshowall = bandakAppDatabaseAdapter.showrecordsPayment_in();
            while (cursorshowall.moveToNext()) {
                String narration=cursorshowall.getString(4);
                if(!narration.equalsIgnoreCase("Opening Interest"))
                {
                    paymentInData = new PaymentInData(cursorshowall.getString(2), cursorshowall.getString(0), cursorshowall.getFloat(1), cursorshowall.getInt(3));
                    Paymentinarraylist.add(paymentInData);
                }

            }

        }

        public void collectdataPaymentOut() {

            bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(getActivity());
            cursorshowall = bandakAppDatabaseAdapter.showrecordsPayment_out();
            while (cursorshowall.moveToNext()) {
                String narration=cursorshowall.getString(5);
                if(!narration.equalsIgnoreCase("Opening Balance") && !narration.equalsIgnoreCase("Opening Principal"))
                {
                    paymentOutData = new PaymentOutData(cursorshowall.getString(0), cursorshowall.getFloat(1), cursorshowall.getString(2), cursorshowall.getFloat(3), cursorshowall.getInt(4));
                    Paymentoutarraylist.add(paymentOutData);
                }

            }

        }
    }


}

