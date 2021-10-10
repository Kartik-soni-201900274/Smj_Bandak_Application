package com.example.myapplication.Bandak;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.PaymentInData;
import com.example.myapplication.PaymentInadapter;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Fragment_payment_in extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog dialog;
    TextWatcher validateinput;
    rangedialoglistener rangedialoglistener;


    Button paymentin_deletebutton,paymentin_updatebutton;
    AutoCompleteTextView edit_paymentin_PartyName;
    BandakAppDatabaseAdapter edit_paymentin_DatabaseAdapter;
    TextInputEditText edit_paymentin_amount, edit_paymentin_narration, edit_paymentin_date, edit_paymentin_rate;
    Spinner edit_paymentin_spinner;
    String date, payment_method, narration, payment_in_partyname;
    ArrayList<PaymentInData> newlist;
    PaymentInadapter adapter;
    ImageButton editaddpartybutton, fpidatebutton;
    float amount;
    int pos, id,adaptpos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_payment_in);
        new AsyncgetintentFPI().execute();
        attach_views_to_id();
        PIattachAdapters();
        settexttoviews();
        createtextwatcher();

        edit_paymentin_DatabaseAdapter = new BandakAppDatabaseAdapter(this);

    }



    public void settexttoviews() {
        int id;
        edit_paymentin_PartyName.setText(payment_in_partyname);
        edit_paymentin_date.setText(date);
        edit_paymentin_amount.setText(String.format("%.2f",(amount)));
        edit_paymentin_narration.setText(narration);
        if(payment_method!=null)
        {
            if (payment_method.equalsIgnoreCase("Cash"))
                id = 0;
            else if (payment_method.equalsIgnoreCase("Cheque"))
                id = 1;
            else if (payment_method.equalsIgnoreCase("Bank Transfer"))
                id = 2;
            else
                id = 3;
            edit_paymentin_spinner.setSelection(id);
        }
       else
        {
            edit_paymentin_spinner.setSelection(0);
        }


    }


    public void PIattachAdapters() {
        ArrayAdapter<String> spinneradapter2 = new ArrayAdapter<>(this, R.layout.simplelist, getResources().getStringArray(R.array.spinner2));
        edit_paymentin_spinner.setAdapter(spinneradapter2);


    }

    public void dateonClick(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(this, this, year, month, day);

        dialog.show();

    }

    public void onDateSet(DatePicker view, int myear, int mmonth, int mday) {
        DateConverter converter = new DateConverter();
        edit_paymentin_date.setText(formatdate(converter.ADTOBS(myear, mmonth, mday)));
    }

    public void editpaymentin(View v) {


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

    public void createtextwatcher() {
        validateinput = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edit_paymentin_date.getText().toString().trim().length() == 0 || edit_paymentin_amount.getText().toString().trim().length() == 0 || edit_paymentin_PartyName.getText().toString().trim().length() == 0) {
                    paymentin_updatebutton.setEnabled(false);
                } else {
                    paymentin_updatebutton.setEnabled(true);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        edit_paymentin_date.addTextChangedListener(validateinput);
        edit_paymentin_PartyName.addTextChangedListener(validateinput);
        edit_paymentin_amount.addTextChangedListener(validateinput);
    }

    public void editupdatebuttononclick(View v) {


        String name = edit_paymentin_PartyName.getText().toString().trim();
        String date = edit_paymentin_date.getText().toString().trim();
        String narration =edit_paymentin_narration.getText().toString();
        String payment_method=edit_paymentin_spinner.getSelectedItem().toString();
        float amount = Float.parseFloat(edit_paymentin_amount.getText().toString());
        edit_paymentin_DatabaseAdapter.updatewithid(payment_in_partyname,id, pos, amount, date, payment_method, (float) 0, narration,name);
        Toast.makeText(this,"Update Successful",Toast.LENGTH_LONG).show();
        setResult(1);
        finish();
    }
    public void deletebuttonclick(View v)
    {
        edit_paymentin_DatabaseAdapter.deleteindividualtrans(id, pos);
        Intent i= new Intent();
        i.putExtra("adptpos",adaptpos);
        setResult(0,i);
        finish();

    }
    @Override
    public void onBackPressed() {

        setResult(5);
        finish();
    }

    public void attach_views_to_id() {
        edit_paymentin_spinner = findViewById(R.id.editamountspinner2);
        edit_paymentin_amount = findViewById(R.id.editamount);
        edit_paymentin_narration = findViewById(R.id.editnarrtion);
        edit_paymentin_date = findViewById(R.id.editdate);
        paymentin_deletebutton=findViewById(R.id.editFIdeletebutton);
        paymentin_updatebutton=findViewById(R.id.editFIdonebutton);
        edit_paymentin_PartyName = findViewById(R.id.editname);
        paymentin_deletebutton.setOnClickListener(this::deletebuttonclick);
        fpidatebutton = findViewById(R.id.fpidatebutton);
        fpidatebutton.setOnClickListener(this::dateonClick);
        paymentin_updatebutton.setOnClickListener(this::editupdatebuttononclick);

    }


    class AsyncgetintentFPI extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


        }

        @Override
        protected Void doInBackground(Void... voids) {
            getintent();
            return null;
        }
        public void getintent() {
            Intent i = getIntent();
            payment_in_partyname = i.getStringExtra("name");
            id = i.getIntExtra("id", -1);
            date = i.getStringExtra("date");
            amount = i.getFloatExtra("amount", 0);
            narration = i.getStringExtra("narration");
            payment_method = i.getStringExtra("spinner");
            pos = i.getIntExtra("pos", 0);
            adaptpos= i.getIntExtra("adptpos",-1);



        }

    }
}