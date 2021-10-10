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

public class Fragment_payment_out extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog dialog;
    TextWatcher validateinput;

    Button paymentin_deletebutton, paymentin_updatebutton;
    AutoCompleteTextView edit_paymentout_PartyName;
    BandakAppDatabaseAdapter edit_paymentout_DatabaseAdapter;
    TextInputEditText edit_paymentout_amount, edit_paymentout_narration, edit_paymentout_date, edit_paymentout_rate;
    Spinner edit_paymentout_spinner;
    String date, payment_method, narration, payment_in_partyname;
    ArrayList<PaymentInData> newlist;
    PaymentInadapter adapter;
    ImageButton editaddpartybutton, fpodatebutton;
    float amount, rate;
    int id, pos, adaptpos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_payment_out);
        new AsyncgetintentFPO().execute();
        attach_views_to_id();
        PIattachAdapters();
        settexttoviews();
        createtextwatcher();

        edit_paymentout_DatabaseAdapter = new BandakAppDatabaseAdapter(this);
    }



    public void settexttoviews() {
        int id;
        edit_paymentout_PartyName.setText(payment_in_partyname);
        edit_paymentout_date.setText(date);
        edit_paymentout_amount.setText(String.format("%.2f", (amount)));
        edit_paymentout_narration.setText(narration);
        edit_paymentout_rate.setText(String.valueOf(rate));
        if (payment_method != null) {
            if (payment_method.equalsIgnoreCase("Cash"))
                id = 0;
            else if (payment_method.equalsIgnoreCase("Cheque"))
                id = 1;
            else if (payment_method.equalsIgnoreCase("Bank Transfer"))
                id = 2;
            else
                id = 3;
            edit_paymentout_spinner.setSelection(id);
        } else {
            edit_paymentout_spinner.setSelection(0);
        }

    }



    public void PIattachAdapters() {
        ArrayAdapter<String> spinneradapter2 = new ArrayAdapter<>(this, R.layout.simplelist, getResources().getStringArray(R.array.spinner2));
        edit_paymentout_spinner.setAdapter(spinneradapter2);
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
        edit_paymentout_date.setText(formatdate(converter.ADTOBS(myear, mmonth, mday)));
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
                if (edit_paymentout_date.getText().toString().trim().length() == 0 || edit_paymentout_amount.getText().toString().trim().length() == 0 || edit_paymentout_PartyName.getText().toString().trim().length() == 0) {
                    paymentin_updatebutton.setEnabled(false);
                } else {
                    paymentin_updatebutton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        edit_paymentout_date.addTextChangedListener(validateinput);
        edit_paymentout_PartyName.addTextChangedListener(validateinput);
        edit_paymentout_amount.addTextChangedListener(validateinput);
        if(narration.equals("Opening Balance"))
        {
            edit_paymentout_rate.setEnabled(false);
        }
    }

    public void editupdatebuttononclick(View v) {
        String name = edit_paymentout_PartyName.getText().toString();
        String date = edit_paymentout_date.getText().toString();
        String narration = edit_paymentout_narration.getText().toString();
        String payment_method = edit_paymentout_spinner.getSelectedItem().toString();
        float amount = Float.parseFloat(edit_paymentout_amount.getText().toString());
        float ratenew = Float.parseFloat(edit_paymentout_rate.getText().toString());
        edit_paymentout_DatabaseAdapter.updatewithid(payment_in_partyname,id, pos, amount, date, payment_method, ratenew, narration, name);

        Toast.makeText(this, "Update Successful", Toast.LENGTH_LONG).show();
        setResult(1);
        finish();
    }

    public void deletebuttonclick(View v) {
        Toast.makeText(this, "delete Successful", Toast.LENGTH_LONG).show();
        int res = edit_paymentout_DatabaseAdapter.deleteindividualtrans(id, pos);
        if (res >= 0) {
            Toast.makeText(this, "delete Successful", Toast.LENGTH_LONG).show();
        }
        Intent i = new Intent();
        i.putExtra("adptpos", adaptpos);
        setResult(0, i);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(5);
        finish();
    }
    public void taskdone()
    {

    }
    public void attach_views_to_id() {
        edit_paymentout_spinner = findViewById(R.id.edit_paymentoutamountspinner2);
        edit_paymentout_amount = findViewById(R.id.edit_paymentoutamount);
        edit_paymentout_narration = findViewById(R.id.edit_paymentoutnarrtion);
        edit_paymentout_date = findViewById(R.id.edit_paymentoutdate);
        paymentin_updatebutton = findViewById(R.id.editFodonebutton);
        paymentin_deletebutton = findViewById(R.id.editFodeletebutton);
        edit_paymentout_PartyName = findViewById(R.id.edit_paymentOUtname);

        edit_paymentout_rate = findViewById(R.id.edit_paymentoutrate);
        fpodatebutton = findViewById(R.id.fpodateButton);
        fpodatebutton.setOnClickListener(this::dateonClick);
        paymentin_updatebutton.setOnClickListener(this::editupdatebuttononclick);
        paymentin_deletebutton.setOnClickListener(this::deletebuttonclick);


    }

    class AsyncgetintentFPO extends AsyncTask<Void, Void, Void> {
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
            date = i.getStringExtra("date");
            amount = i.getFloatExtra("amount", 0);
            narration = i.getStringExtra("narration");
            payment_method = i.getStringExtra("spinner");
            rate = i.getFloatExtra("rate", 0);
            id = i.getIntExtra("id", -1);
            pos = i.getIntExtra("pos", 1);
            adaptpos = i.getIntExtra("adptpos", -1);

        }

    }
}