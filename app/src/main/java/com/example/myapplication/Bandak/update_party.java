package com.example.myapplication.Bandak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.AdditionalInfoFragment;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class update_party extends AppCompatActivity implements OPbalanceFragment.FragmentClickListener, AdditionalInfoFragment.adiFragmentClickListener {
    TextInputEditText updatepartphone,updatepartyname, updatepartyopblncdate, updatepartyopeningbalance, updatepartyaddress, updatepartytelephone, updatepartyadditionalinfo, updatepartyprincipalbalance, updatepartyinterestbalance;
    Button updatepartysavebutton, updatepartycancelbutton;
    String oldname, oldaddress, oldphone, name, address, phone, telephone, group, additionalinfo, opblncdate;
    float interestblnc;
    float principalblnc;
    float openingblnc;
    MaterialAutoCompleteTextView updatepartygroup;
    BandakAppDatabaseAdapter bandakAppDatabaseAdapter;
    OPbalanceFragment opBalanceFragment;
    AdditionalInfoFragment additionalInfoFragment;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    Context context = this;
    int opcounter=0;
    int adicounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        opBalanceFragment = new OPbalanceFragment(this);
        additionalInfoFragment = new AdditionalInfoFragment(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_party);
        bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        getintent();
        setviewstoid();
        setTexttoviews();
        managetablayoutmediator();
    }

    @Override
    protected void onResume() {
        super.onResume();
       Asyncintent  asyncPopulate =new  Asyncintent();
        asyncPopulate.execute();
    }

    public void getintent() {
        Intent i = getIntent();
        oldname = i.getStringExtra("updatepartyname");
        oldphone = i.getStringExtra("updatepartyphone");
        oldaddress = i.getStringExtra("updatepartyaddress");
    }

    public void setviewstoid() {
        tabLayout = findViewById(R.id.PartyEdit_tablayout);
        viewPager = findViewById(R.id.PartyEdit_viewpager);
        viewPager.setAdapter(new editpartyextraAdapter(this, this));
        updatepartysavebutton = findViewById(R.id.PartyEdit_savebutton);
        updatepartycancelbutton = findViewById(R.id.PartyEdit_cancelbutton);
        updatepartysavebutton.setOnClickListener(this::updatebuttonclick);
        updatepartycancelbutton.setOnClickListener(this::cancelbutton);
        updatepartphone=findViewById(R.id.partyEdit_Partyphone);

        updatepartyname = findViewById(R.id.partyEdit_Partyname);
        updatepartphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 14) {
                    updatepartytelephone.setError("Contact Number too long");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void managetablayoutmediator() {
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0: {

                        tab.setText("Additional Info");


                        break;
                    }
                    case 1: {
                        tab.setText("Opening Balance");

                        break;
                    }


                }
            }
        });
        tabLayoutMediator.attach();

    }

    public void setadifragmentviewstoid() {
        updatepartytelephone = findViewById(R.id.adiFragmentPartytelephone);
        updatepartyaddress = findViewById(R.id.adiFragmentPartyAddress);
        updatepartyadditionalinfo = findViewById(R.id.adiFragmentadditionalinfo);
        updatepartytelephone.setText(telephone);
        updatepartyadditionalinfo.setText(additionalinfo);
        updatepartyaddress.setText(oldaddress);

    }

    public void setOPBfragmentviewstoid() {


        updatepartyprincipalbalance = findViewById(R.id.opbFragmentprincipalBalance);
        updatepartyinterestbalance = findViewById(R.id.opbFragmentinterestbalance);
        updatepartyopeningbalance = findViewById(R.id.opbFragmentopeningBalance);
        updatepartyopblncdate=findViewById(R.id.opbFragmentopeningbalancedate);
        updatepartyopblncdate.setText(opblncdate);
        updatepartyprincipalbalance.setText(String.format("%.2f", interestblnc));
        updatepartyinterestbalance.setText(String.format("%.2f", principalblnc));
        updatepartyopeningbalance.setText(String.format("%.2f", openingblnc));

        updatepartyopblncdate = findViewById(R.id.opbFragmentopeningbalancedate);

//
        updatepartyopblncdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().contains("/")) {
                    updatepartyopblncdate.setError("Date format wrong");


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    @Override
    public void attached() {
        opcounter=1;
        Toast.makeText(context, "opbfragment attached", Toast.LENGTH_SHORT).show();
        setOPBfragmentviewstoid();
    }

    @Override
    public void adiFragmentattached() {
        adicounter=1;
        setadifragmentviewstoid();
    }


    public void setTexttoviews() {
        updatepartyname.setText(oldname);
        updatepartphone.setText(oldphone);


    }



    public void updatebuttonclick(View v) {
        name = updatepartyname.getText().toString().trim();
        phone = updatepartphone.getText().toString().trim();
        if(adicounter!=0)
        {
             address = updatepartyaddress.getText().toString().trim();
             telephone = updatepartytelephone.getText().toString();
             additionalinfo = updatepartyadditionalinfo.getText().toString();

        }
        if(opcounter!=0)
        {
             openingblnc= Float.parseFloat(updatepartyopeningbalance.getText().toString());
             opblncdate = updatepartyopblncdate.getText().toString().trim();
             interestblnc = Float.parseFloat(updatepartyinterestbalance.getText().toString());
             principalblnc = Float.parseFloat(updatepartyprincipalbalance.getText().toString());
        }

        int id = bandakAppDatabaseAdapter.updateparty(oldname, oldphone, name, phone, address, telephone, principalblnc, interestblnc, additionalinfo, opblncdate,openingblnc);

        if (id >= 0) {
            bandakAppDatabaseAdapter.updateinterestandprincipal(name, interestblnc, principalblnc,opblncdate);
          bandakAppDatabaseAdapter.updatepartyopeningbalance(openingblnc,name,opblncdate);
        }
        Toast.makeText(this, "UPDATED", Toast.LENGTH_LONG).show();
        onBackPressed();


    }

    public void cancelbutton(View v) {
        onBackPressed();
    }

    class Asyncintent extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getdata();

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


        }
        public void getdata() {
            Cursor cursor = bandakAppDatabaseAdapter.showallpartycomplete(oldname);
            cursor.moveToFirst();
            address = (cursor.getString(1));
            additionalinfo = (cursor.getString(3));
            openingblnc =cursor.getFloat(8);
            principalblnc = cursor.getFloat(4);
            interestblnc = cursor.getFloat(5);
            opblncdate = (cursor.getString(6));
            telephone = (cursor.getString(7));

        }

    }

    public class editpartyextraAdapter extends FragmentStateAdapter {
        Context context;

        public editpartyextraAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
            super(fragmentActivity);
            this.context = context;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return additionalInfoFragment;
            } else {
                return opBalanceFragment;
            }


        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}