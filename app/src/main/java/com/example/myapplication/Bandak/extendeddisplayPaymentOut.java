package com.example.myapplication.Bandak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

public class extendeddisplayPaymentOut extends AppCompatActivity {
    TextView extended_Payment_Out_amount,
            extended_Payment_Out_date,
            extended_Payment_Out_name,
            extended_Payment_Out_contact,
            extended_Payment_Out_narration,
            extended_Payment_Out_rate,
            extended_Payment_Out_paymentmethod;
    String date, payment_method, narration, partyname;
    float amount, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extendeddisplayindivtrans);
        setTextviewsToID();
        getintentdata();
        getpartyinfo();
        setdatatoviews();

    }

    public void setTextviewsToID() {
       extended_Payment_Out_amount = findViewById(R.id.extendedPayment_outamount);
       extended_Payment_Out_date = findViewById(R.id.extendedPayment_outdate);
       extended_Payment_Out_name = findViewById(R.id.extendedPayment_outname);
       extended_Payment_Out_contact = findViewById(R.id.extendedPayment_outcontact);
       extended_Payment_Out_narration = findViewById(R.id.extendedPayment_outnarration);
       extended_Payment_Out_paymentmethod = findViewById(R.id.extendedPayment_outpaymentmethod);
       extended_Payment_Out_rate=findViewById(R.id.extendedPayment_outrate);
    }

    public void getintentdata() {
        Intent i = getIntent();
        partyname = i.getStringExtra("name");
        date = i.getStringExtra("date");
        amount = i.getFloatExtra("amount", 0);
        narration = i.getStringExtra("narration");
        payment_method = i.getStringExtra("spinner");
        rate=i.getFloatExtra("rate",0);

    }

    public void setdatatoviews() {
       extended_Payment_Out_amount.setText(String.valueOf(amount));
       extended_Payment_Out_date.setText(date);
       extended_Payment_Out_name.setText(partyname);
       extended_Payment_Out_narration.setText(narration);
       extended_Payment_Out_paymentmethod.setText(payment_method);
        extended_Payment_Out_rate.setText(String.valueOf(rate));
        extended_Payment_Out_contact.setOnClickListener(this::checkpermission);
    }

    public void getpartyinfo() {
        BandakAppDatabaseAdapter bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        Cursor cursor = bandakAppDatabaseAdapter.showrecordsNewPartywitharguement(partyname);
        cursor.moveToFirst();
        extended_Payment_Out_contact.setText(cursor.getString(1));
        cursor.close();

    }
    public void checkpermission(View v)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        }
        else
        {
            dialcontact();
        }
    }
    public void dialcontact()
    {
        Intent i=new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:"+extended_Payment_Out_contact.getText().toString()));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                dialcontact();
            }
            else
            {
                Toast.makeText(this,"PERMISSION DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }
}