package com.example.myapplication.Bandak;

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
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class viewpartydetails extends AppCompatActivity {
    TextView PartyDetails_balance,PartyDetails_principal,PartyDetails_interest,

    PartyDetails_Address,
           PartyDetails_Asofdate,PartyDetails_telephone,
           PartyDetails_name,
            PartyDetails_opening,
           PartyDetails_contact,
           PartyDetails_Additional_info,

           PartyDetails_paymentmethod;
    String asofdate,additionalinfo,partyname,contact;
    float balance, interest,principal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpartydetails);
        getintentdata();
        setTitle(partyname+"Party Details");
        setTextviewsToID();
        getpartyinfo();


    }

    public void setTextviewsToID() {

        PartyDetails_telephone=findViewById(R.id.PartyDetailstelephone);
        PartyDetails_principal = findViewById(R.id.PartyDetailsprincipal);
        PartyDetails_interest = findViewById(R.id.PartyDetailsinterest);
        PartyDetails_Asofdate = findViewById(R.id.PartyDetailsasofdate);
        PartyDetails_Address = findViewById(R.id.PartyDetailsaddress);
        PartyDetails_opening=findViewById(R.id.partyDetailsopening);
       PartyDetails_name = findViewById(R.id.PartyDetailsname);
       PartyDetails_contact = findViewById(R.id.PartyDetailscontact);
        PartyDetails_Additional_info = findViewById(R.id.PartyDetailsadditionalinfo);
        PartyDetails_contact.setOnClickListener(this::checkpermission);
        PartyDetails_telephone.setOnClickListener(this::checkpermission);
    }

    public void getintentdata() {
        Intent i = getIntent();
        partyname = i.getStringExtra("name");
    }



    public void getpartyinfo(){
        BandakAppDatabaseAdapter bandakAppDatabaseAdapter=new BandakAppDatabaseAdapter(this);
        Cursor cursor=bandakAppDatabaseAdapter.showallpartycomplete(partyname);
        cursor.moveToFirst();
        PartyDetails_Address.setText(cursor.getString(1));
        PartyDetails_contact.setText(cursor.getString(2));
        PartyDetails_Additional_info.setText(cursor.getString(3));
        PartyDetails_opening.setText(String.format("%.2f",cursor.getFloat(8)));
        PartyDetails_principal.setText(String.format("%.2f",cursor.getFloat(4)));
        PartyDetails_interest.setText(String.format("%.2f",cursor.getFloat(5)));
        PartyDetails_Asofdate.setText(cursor.getString(6));
        PartyDetails_telephone.setText(cursor.getString(7));
        PartyDetails_name.setText(partyname);
        //PartyDetails_balance.setText(String.format("%.2f",));





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
        i.setData(Uri.parse("tel:"+PartyDetails_contact.getText().toString()));
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