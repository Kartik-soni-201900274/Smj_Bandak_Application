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


public class Payment_out extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    DatePickerDialog dialog;
    ArrayAdapter POpartynamesAdapter;
    TextWatcher validateinput;
    ArrayList<String> mpartylist = new ArrayList<>();
    Button POSaveButton;
    AutoCompleteTextView PO_AddPartyName, payment_method_spinner;
    BandakAppDatabaseAdapter PO_AddDatabaseAdapter;
    TextInputEditText PO_Addamount, PO_Addnarration, PO_Adddate, PO_Addrate;

    String date, payment_method, narration, payment_in_partyname;
    ArrayList<PaymentInData> newlist;
    PaymentInadapter adapter;
    ImageButton POaddpartybutton,podatebutton;
    Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_out);

        attach_views_to_id();
        POAttachAdapters();
        createtextwatcher();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new AsyncgetintentPO().execute();
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
    public void attach_views_to_id(){
        payment_method_spinner = findViewById(R.id.POamountspinner2);
        PO_Addamount = findViewById(R.id.POamount);
        PO_Addnarration = findViewById(R.id.POnarrtion);
        PO_Addrate=findViewById(R.id.POrate);
        PO_Adddate = findViewById(R.id.POdate);
        POSaveButton = findViewById(R.id.POsavebutton);
        PO_AddPartyName = findViewById(R.id.POname);
        POaddpartybutton=findViewById(R.id.POaddnewpartybutton);
        POaddpartybutton.setOnClickListener(this::imagebuttonclick);
        POSaveButton.setEnabled(false);
        POSaveButton.setOnClickListener(this::paymentoutSave);
        podatebutton=findViewById(R.id.podatebutton);
        podatebutton.setOnClickListener(this::dateonClick);

    }

public void POAttachAdapters()
{
    ArrayAdapter<String> spinneradapter2 = new ArrayAdapter<>(this, R.layout.simplelist, getResources().getStringArray(R.array.spinner2));
    payment_method_spinner.setAdapter(spinneradapter2);
    POpartynamesAdapter = new ArrayAdapter(this, R.layout.simplelist, R.id.simplelisttext, mpartylist);
    PO_AddPartyName.setAdapter(POpartynamesAdapter);
}

public void createtextwatcher()
{
    validateinput = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (PO_Adddate.getText().toString().trim().length() == 0 || PO_Addamount.getText().toString().trim().length() == 0|| PO_AddPartyName.getText().toString().trim().length() == 0||PO_Addrate.getText().toString().trim().length() == 0) {
                POSaveButton.setEnabled(false);
            } else {
                POSaveButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    PO_Adddate.addTextChangedListener(validateinput);
    PO_AddPartyName.addTextChangedListener(validateinput);
    PO_Addamount.addTextChangedListener(validateinput);
   PO_Addrate .addTextChangedListener(validateinput);

}

    public void paymentoutSave(View v) {
        date = PO_Adddate.getText().toString();
        narration = PO_Addnarration.getText().toString().trim();
        String amount=PO_Addamount.getText().toString().trim();
        String rate = PO_Addrate.getText().toString();
        float final_rate=Float.parseFloat(rate);
        float final_amount=Float.parseFloat(amount);
        payment_method = payment_method_spinner.getText().toString();
        payment_in_partyname = PO_AddPartyName.getText().toString().trim();
        BandakAppDatabaseAdapter bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        int id= bandakAppDatabaseAdapter.insertNewPaymentOut(final_amount,date,payment_method,final_rate,narration,payment_in_partyname);
        onBackPressed();
    }


    public void onDateSet(DatePicker view, int myear, int mmonth, int mday) {
        DateConverter converter = new DateConverter();

        String formatteddate=formatdate(converter.ADTOBS(myear, mmonth, mday));
        if(formatteddate==null)
        {
            PO_Adddate.setText(null);
        }
        else
        {
            PO_Adddate.setText(formatteddate);
        }
    }

    public void dateonClick(View v) {   final Calendar c = Calendar.getInstance();
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


    class AsyncgetintentPO extends AsyncTask<Void, Void, Void> {
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
            PO_AddDatabaseAdapter = new BandakAppDatabaseAdapter(context);
            Cursor cursor = PO_AddDatabaseAdapter.showrecordsNewParty();
            while (cursor.moveToNext()) {
                mpartylist.add(cursor.getString(0));
            }

        }

    }

}

