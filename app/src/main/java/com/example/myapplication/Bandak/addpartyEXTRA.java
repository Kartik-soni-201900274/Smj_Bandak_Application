package com.example.myapplication.Bandak;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.AdditionalInfoFragment;
import com.example.myapplication.R;
import com.example.myapplication.RecievableFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class addpartyEXTRA extends AppCompatActivity implements OPbalanceFragment.FragmentClickListener, AdditionalInfoFragment.adiFragmentClickListener {

    TextInputEditText addpartyphone;
    // MaterialAutoCompleteTextView  addpartygroup;
    AutoCompleteTextView addpartyname;
    TextWatcher validateinput;
    Button addpartysavebutton, addpartycancelbutton;
    LinearLayout fragmentlayout;
    private SimpleAdapter mAdapter;
    ArrayAdapter groupadapter;
    private ArrayList<Map<String, String>> mPeopleList = new ArrayList<Map<String, String>>();
    ArrayList<MyContacts> myContactsArrayList = new ArrayList<>();
    ConstraintLayout addpartyLinearlayout;
    BandakAppDatabaseAdapter DatabaseAdapter;
    ArrayList<String> partylist = new ArrayList<>();
    ArrayList<String> grouplist = new ArrayList<>();
    Context context = this;
    int counter;
    TabLayout tabLayout;
    TextInputEditText addpartyaddress, addpartyopeningbalance, addpartytelephone, addpartyadditionalinfo, addpartyprincipalbalance, addpartyinterestbalance, addpartyopblncdate;
    String phone, address, telephone, additional, openingblncdate;
    float principal, interest, openingbalance;
    int flag = 0;
    ViewPager2 viewPager;
    OPbalanceFragment opBalanceFragment;
    AdditionalInfoFragment additionalInfoFragment;
    int opcounter=0;
    int adicounter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opBalanceFragment = new OPbalanceFragment(this);
        additionalInfoFragment = new AdditionalInfoFragment(this);
        setContentView(R.layout.addpartyextra);
        DatabaseAdapter = new BandakAppDatabaseAdapter(this);
        setTitle("Add New Party");
        checkcontactspermission();
        APsetviewtoid();
        setpartynameadapter();
        Apsettextwatcher();
        managetablayoutmediator();
        openingblncdate=gettodaysdate();

    }
    @Override
    protected void onResume() {
        super.onResume();
        AsyncPopulate asyncPopulate = new AsyncPopulate();
        asyncPopulate.execute();
    }


    public void checkcontactspermission() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
        } else {
            counter = 1;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                counter = 1;
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();

            }
        }

    }


    public void APsetviewtoid() {
        addpartyLinearlayout = findViewById(R.id.addpartyConstraintlayout);
        addpartyphone = findViewById(R.id.Partyphone);
        tabLayout = findViewById(R.id.addpartytablayout);
        viewPager = findViewById(R.id.addpartyviewpager);
        viewPager.setAdapter(new addpartyextraAdapter(this, this));

        addpartysavebutton = findViewById(R.id.addpartysavebutton);
        addpartysavebutton.setEnabled(false);
        addpartysavebutton.setOnClickListener(this::AddpartySavebutton);
        addpartyname = findViewById(R.id.Partyname);
        addpartycancelbutton = findViewById(R.id.addpartycancelbutton);


        addpartycancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tabLayout.selectTab(tabLayout.getTabAt(0));
    }


    public void setpartynameadapter() {
        mAdapter = new SimpleAdapter(context, mPeopleList, R.layout.simplelist2,
                new String[]{"Name", "Phone"}, new int[]{
                R.id.simplelistname, R.id.simplelistphone});
        ContactsAdapter contactsAdapter = new ContactsAdapter(context, R.layout.simplelist2, myContactsArrayList);
        addpartyname.setAdapter(mAdapter);
        addpartyname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View view, int index, long l) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                Iterator<String> myVeryOwnIterator = map.keySet().iterator();
                while (myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = (String) map.get(key);
                    addpartyphone.setText(value);
                    key = (String) myVeryOwnIterator.next();
                    value = (String) map.get(key);
                    addpartyname.setText(value);
                    break;

                }
            }

        });
    }


    public void Apsettextwatcher() {
        validateinput = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (addpartyphone.getText().toString().trim().length() == 0 || addpartyname.getText().toString().trim().length() == 0) {
                    addpartysavebutton.setEnabled(false);
                } else {
                    addpartysavebutton.setEnabled(true);
                }
                if (addpartyphone.getText().toString().trim().length() > 14) {
                    addpartyphone.setError("Contact Number too long");
                    addpartysavebutton.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        addpartyphone.addTextChangedListener(validateinput);
        addpartyname.addTextChangedListener(validateinput);

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





    public String gettodaysdate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateConverter converter = new DateConverter();

        return formatdate(converter.ADTOBS(year, month, day));
    }

    public String formatdate(String fdate) {
        String datetime = null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        } catch (ParseException e) {

        }
        return datetime;


    }

    public void setadifragmentviewstoid() {
        addpartytelephone = findViewById(R.id.adiFragmentPartytelephone);
        addpartyaddress = findViewById(R.id.adiFragmentPartyAddress);
        addpartyadditionalinfo = findViewById(R.id.adiFragmentadditionalinfo);
        addpartytelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 14) {
                    addpartytelephone.setError("Contact Number too long");

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setOPBfragmentviewstoid() {
        addpartyprincipalbalance = findViewById(R.id.opbFragmentprincipalBalance);
        addpartyinterestbalance = findViewById(R.id.opbFragmentinterestbalance);
        addpartyopeningbalance = findViewById(R.id.opbFragmentopeningBalance);
        addpartyinterestbalance.setText("0");
        addpartyprincipalbalance.setText("0");
        addpartyopeningbalance.setText("0");
        addpartyopblncdate = findViewById(R.id.opbFragmentopeningbalancedate);
        addpartyopblncdate.setText(openingblncdate);

//
        addpartyopblncdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().contains("/")) {
                    addpartyopblncdate.setError("Date format wrong");


                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    public void AddpartySavebutton(View v) {


        int counter = 0;
        String text = addpartyname.getText().toString();
        for (String temp : partylist) {
            if (temp.equalsIgnoreCase(text)) {
                counter = 1;
                Toast.makeText(this, "The Party name already exists.Create  a new party ", Toast.LENGTH_LONG).show();
                break;
            }
        }
        if (counter == 0) {
            String name = addpartyname.getText().toString().trim();
            phone = addpartyphone.getText().toString();
        if(adicounter!=0)
        {
            address = addpartyaddress.getText().toString();
            telephone = addpartytelephone.getText().toString();
            additional = addpartyadditionalinfo.getText().toString();
        }
        else
        {
            address = "";
            telephone = "";
            additional = "";
        }
            if(opcounter!=0)
            {


                interest = Float.parseFloat(addpartyinterestbalance.getText().toString());
                openingbalance = Float.parseFloat(addpartyopeningbalance.getText().toString());
                principal = Float.parseFloat(addpartyprincipalbalance.getText().toString());
                openingblncdate=addpartyopblncdate.getText().toString();
            }
            else
            {
                openingbalance = 0;
                principal = 0;
                interest = 0;

            }



            DatabaseAdapter.insertNewParty(name, phone, address, telephone, openingbalance, principal, interest, additional, openingblncdate);
            DatabaseAdapter.setopeningbalance(openingbalance, interest, principal, openingblncdate, name);
            onBackPressed();

        }


    }




    @Override
    public void attached() {
        opcounter=1;

        setOPBfragmentviewstoid();
    }

    @Override
    public void adiFragmentattached() {
        adicounter=1;
        setadifragmentviewstoid();
    }


    class AsyncPopulate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (counter == 1) {
                PopulatePeopleList();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


        }

        public void PopulatePeopleList() {
            long startnow;
            long endnow;

            startnow = android.os.SystemClock.uptimeMillis();
            ContentResolver cr = getContentResolver();
            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};
            Cursor cursorPhones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    ContactsContract.Contacts.HAS_PHONE_NUMBER + ">0 AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.NUMBER + ")>0" + " AND LENGTH(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ")>0",
                    null,
                    null);


            int indexname = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int indexnumber = cursorPhones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursorPhones.moveToNext()) {

                String displayName = cursorPhones.getString(indexname);
                String number = cursorPhones.getString(indexnumber);
                Map<String, String> NamePhoneType = new HashMap<String, String>();
                NamePhoneType.put("Phone", number);
                NamePhoneType.put("Name", displayName);
                mPeopleList.add(NamePhoneType);

            }

            cursorPhones.close();
            endnow = android.os.SystemClock.uptimeMillis();
            Log.d("END", "TimeForContacts " + (endnow - startnow) + " ms");

        }

    }

    public class addpartyextraAdapter extends FragmentStateAdapter {
        Context context;

        public addpartyextraAdapter(@NonNull FragmentActivity fragmentActivity, Context context) {
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
