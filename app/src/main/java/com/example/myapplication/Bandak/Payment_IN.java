package com.example.myapplication.Bandak;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

import androidx.appcompat.app.AppCompatActivity;

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


public class Payment_IN extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog dialog;
    ArrayAdapter partynamesAdapter;
    TextWatcher validateinput;
    ArrayList<String> mpartylist = new ArrayList<>();
    Button PISaveButton;
    AutoCompleteTextView PIPartyName, payment_method_spinner;
    BandakAppDatabaseAdapter PIDatabaseAdapter;
    TextInputEditText PIamount, PInarration, PIdate;
    ImageButton addnewpartybutton,pidatebutton;
    String date, payment_method, narration, payment_in_partyname;
    ArrayList<PaymentInData> newlist;
    PaymentInadapter adapter;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_in);

        attach_views_to_id();
        PIattachAdapters();
        PIsettextwatcher();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncgetintentPI().execute();
    }

    public void  attach_views_to_id()
  {
      payment_method_spinner = findViewById(R.id.PIamountspinner2);
      PIamount = findViewById(R.id.PIamount);
      PInarration = findViewById(R.id.PInarrtion);
        pidatebutton=findViewById(R.id.PIdateButton);
      PIdate = findViewById(R.id.PIdate);
      PISaveButton = findViewById(R.id.PIsavebutton);
      PIPartyName = findViewById(R.id.transname);
      addnewpartybutton = findViewById(R.id.Payment_INaddnewpartybutton);
      PISaveButton.setOnClickListener(this::PISave);
      pidatebutton.setOnClickListener(this::dateonClick);
      PISaveButton.setEnabled(false);
      addnewpartybutton.setOnClickListener(this::imagebuttonclick);
  }



    public void PIattachAdapters()
{
    ArrayAdapter<String> spinneradapter2 = new ArrayAdapter<>(this, R.layout.simplelist, getResources().getStringArray(R.array.spinner2));
    payment_method_spinner.setAdapter(spinneradapter2);
    partynamesAdapter = new ArrayAdapter(this, R.layout.simplelist, R.id.simplelisttext, mpartylist);
    PIPartyName.setAdapter(partynamesAdapter);

}
public void PIsettextwatcher()
{
    validateinput = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (PIdate.getText().toString().trim().length() == 0 || PIamount.getText().toString().trim().length() == 0|| PIPartyName.getText().toString().trim().length() == 0) {
                PISaveButton.setEnabled(false);
            } else {
                PISaveButton.setEnabled(true);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    PIdate.addTextChangedListener(validateinput);
    PIPartyName.addTextChangedListener(validateinput);
    PIamount.addTextChangedListener(validateinput);

}

    public void PISave(View v) {
        date = PIdate.getText().toString().trim();
        narration = PInarration.getText().toString().trim();
        String amount=PIamount.getText().toString().trim();
        float final_amount=Float.parseFloat(amount);
        payment_method = payment_method_spinner.getText().toString();
        payment_in_partyname = PIPartyName.getText().toString().trim();
        BandakAppDatabaseAdapter bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        int id= bandakAppDatabaseAdapter.insertNewPaymentin(final_amount,date,payment_method,narration,payment_in_partyname);
       // new RecievableFragment(0).managerfunction();
        onBackPressed();
    }


    public void onDateSet(DatePicker view, int myear, int mmonth, int mday) {
        DateConverter converter = new DateConverter();

        String formatteddate=formatdate(converter.ADTOBS(myear, mmonth, mday));
        if(formatteddate==null)
        {
            PIdate.setText(null);
        }
        else
        {
            PIdate.setText(formatteddate);
        }
    }
    public String formatdate(String fdate)
    {
        String datetime=null;
        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat d= new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convertedDate = inputFormat.parse(fdate);
            datetime = d.format(convertedDate);

        }catch (ParseException e)
        {

        }
        return  datetime;


    }
    public void dateonClick(View v) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();

    }

    public void imagebuttonclick(View v) {
        Intent i = new Intent(this, bndkmnaddparty.class);
        startActivity(i);
    }



    class AsyncgetintentPI extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);




        }

        @Override
        protected Void doInBackground(Void... voids) {
            getallparty();
            return null;
        }
        public void getallparty()
        {
            mpartylist.clear();

            PIDatabaseAdapter = new BandakAppDatabaseAdapter(context);
            Cursor cursor = PIDatabaseAdapter.showrecordsNewParty();
            while (cursor.moveToNext()) {
                mpartylist.add(cursor.getString(0));
            }


        }

    }

}

