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

import java.net.URI;

public class ExtendedDisplayPaymentIn extends AppCompatActivity {
    TextView Extended_Payment_In_amount,
            Extended_Payment_In_date,
            Extended_Payment_In_name,
            Extended_Payment_In_contact,
            Extended_Payment_In_narration,
            Extended_Payment_In_paymentmethod;
    String date, payment_method, narration, partyname;
    float amount, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_display_payment_in);
        setTextviewsToID();
        getintentdata();
        getpartyinfo();
        setdatatoviews();

    }

    public void setTextviewsToID() {
        Extended_Payment_In_amount = findViewById(R.id.extendedPayment_Inamount);
        Extended_Payment_In_date = findViewById(R.id.extendedPayment_Indate);
        Extended_Payment_In_name = findViewById(R.id.extendedPayment_Inname);
        Extended_Payment_In_contact = findViewById(R.id.extendedPaymentIncontact);
        Extended_Payment_In_narration = findViewById(R.id.extendedPayment_Innarration);
        Extended_Payment_In_paymentmethod = findViewById(R.id.extendedPayment_Inpaymentmethod);
        Extended_Payment_In_contact.setOnClickListener(this::checkpermission);
    }

    public void getintentdata() {
        Intent i = getIntent();
        partyname = i.getStringExtra("name");
        date = i.getStringExtra("date");
        amount = i.getFloatExtra("amount", 0);
        narration = i.getStringExtra("narration");
        payment_method = i.getStringExtra("spinner");

    }

    public void setdatatoviews() {
        Extended_Payment_In_amount.setText(String.valueOf(amount));
        Extended_Payment_In_date.setText(date);
        Extended_Payment_In_name.setText(partyname);
        Extended_Payment_In_narration.setText(narration);
        Extended_Payment_In_paymentmethod.setText(payment_method);
    }

    public void getpartyinfo(){
        BandakAppDatabaseAdapter bandakAppDatabaseAdapter=new BandakAppDatabaseAdapter(this);
        Cursor cursor=bandakAppDatabaseAdapter.showrecordsNewPartywitharguement(partyname);
        cursor.moveToFirst();
        Extended_Payment_In_contact.setText(cursor.getString(1));
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
        i.setData(Uri.parse("tel:"+Extended_Payment_In_contact.getText().toString()));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100)
        {
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
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