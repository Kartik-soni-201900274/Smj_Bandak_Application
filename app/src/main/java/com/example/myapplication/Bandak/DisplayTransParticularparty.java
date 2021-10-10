package com.example.myapplication.Bandak;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DisplayTransParticularparty extends AppCompatActivity implements IndividualTransAdapter.ClickInterface, View.OnCreateContextMenuListener, rangedialoglistener{
    RecyclerView recycler;
    BandakAppDatabaseAdapter bandakDatabaseAdapter;
    ArrayList<Individual_trans_data> Individual_Trans_ArrayList = new ArrayList<>();
    ActivityResultLauncher<Intent> editactivitylauncher;
    TextView total_amount,drcr;
    IndividualTransAdapter individualtransAdapter;
    TextInputLayout Datetolayout;
    Button donebutton;
    TextInputEditText search, datefromedittext, datetoedittext;
    TextWatcher dttpwatcher;
    IndividualTransAdapter.ClickInterface clickInterface =this;
    Cursor cursor;
    String name;
Context context=this;
    DatePickerDialog dialog;
   DateRangeDialog dateRangeDialog;
    int counter;
    public float totalout=0,totalin=0;
String todaysdate;

    Float amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_trans_particularparty);
        editactivitylauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode()==1)
            {
                Toast.makeText(this,"fhsdkfjdh",Toast.LENGTH_LONG).show();
           // updaterecycler();
            }
            else if(result.getResultCode()==0)
            {
               Intent i=result.getData();
               int deleteditempos=i.getIntExtra("adptpos",-1);
               //updaterecyclerafterdelete(deleteditempos);
            }
        });

        setviewstoid();
        name = getIntent().getStringExtra("party name");
        setTitle(name+" Ledger");


        //settotal();
        addtextWatcher();
    }
    public void setadapter()
    {
        WrapContentLinearLayoutManager wrapContentLinearLayoutManager=new WrapContentLinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        wrapContentLinearLayoutManager.setStackFromEnd(true);
        individualtransAdapter = new IndividualTransAdapter(Individual_Trans_ArrayList, this, this);
        recycler.setLayoutManager(wrapContentLinearLayoutManager);
        recycler.setAdapter(individualtransAdapter);
    }

    public void ontaskdone() {
        if(individualtransAdapter!=null)
        {
            individualtransAdapter.setdata(Individual_Trans_ArrayList);
        }
        else
        {
            setadapter();
        }


    }
    public void onResume() {
        super.onResume();
   if(individualtransAdapter==null)
    {
        Asynctaskgetdata asynctaskgetdata =new Asynctaskgetdata();
        asynctaskgetdata.execute();
     }
   else if(individualtransAdapter!=null)
        {

            //settotal();
            Individual_Trans_ArrayList.clear();
            Asynctaskgetdata asynctaskgetdata =new Asynctaskgetdata();
            asynctaskgetdata.execute();



        }

    }
    /*
    public void updaterecycler()
    {
        settotal();
        Individual_Trans_ArrayList.clear();
        getparticulardata(name);
        individualtransAdapter.setdata(Individual_Trans_ArrayList);
    }
public void updaterecyclerafterdelete(int adapterposition)
{
    settotal();
    individualtransAdapter.IndividualTransarraylist.remove(adapterposition);
    individualtransAdapter.notifyItemRemoved(adapterposition);
}
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.individual_party_ledger_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.amtHL: {

                sortamt(" ASC");
                Toast.makeText(this, "Sorted in Descending Order", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.amtLH: {
                sortamt(" DESC");
                Toast.makeText(this, "Sorted in Ascending Order", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.dateDSC: {
                sortdate(" ASC");
                Toast.makeText(this, "Sorted in Ascending Order", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.dateASC: {
                Toast.makeText(this, "Sorted in Descending Order", Toast.LENGTH_SHORT).show();

                sortdate(" DESC");
                return true;
            }
            case R.id.sortbyrange: {
                dateRangeDialog = new DateRangeDialog(this);
                FragmentManager fm = getSupportFragmentManager();
                dateRangeDialog.show(fm, "date_range_dialog");
                // search.setText(dateRangeDialog.datefromedittext.getText().toString());
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    public void setviewstoid() {
        recycler = findViewById(R.id.DPPPrecyclerView);
        search = findViewById(R.id.DTTPdittext);
        total_amount=findViewById(R.id.individualtrans_total);
        drcr=findViewById(R.id.debitorcredit);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);

    }


    @Override
    public void displayextended(int id,int type) {
        String name = null, date = null, narration = null, spinner = null;
        float amount = 0, rate = 0;
        bandakDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        Cursor cursor = bandakDatabaseAdapter.getColoumnWithID(id, type);


        if (type == 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(4);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(3);
            }
            Intent i = new Intent(this, ExtendedDisplayPaymentIn.class);
            i.putExtra("name", name);
            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            startActivity(i);
        } else if (type == 1) {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(5);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(4);
                rate = cursor.getFloat(3);
            }
            Intent i = new Intent(this, extendeddisplayPaymentOut.class);
            i.putExtra("name", name);
            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            i.putExtra("rate", rate);
            startActivity(i);
        }
;
    }





    public void addtextWatcher() {
        dttpwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                individualtransAdapter.getFilter().filter(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            } 
        };
        search.addTextChangedListener(dttpwatcher);
    }








    @Override
    public void edit(int id,int adapterposition,int type) {



        bandakDatabaseAdapter = new BandakAppDatabaseAdapter(this);
        Cursor cursor = bandakDatabaseAdapter.getColoumnWithID(id, type);
        String name = null, date = null, narration = null, spinner = null;
        float amount = 0, rate = 0;

        if (type == 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(4);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(3);
                id=cursor.getInt(5);
            }
            Intent i = new Intent(this, Fragment_payment_in.class);
            i.putExtra("name", name);

            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            i.putExtra("id",id);
            i.putExtra("pos",type);
            i.putExtra("adptpos",adapterposition);


            startActivity(i);
        } else  {
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
                date = cursor.getString(1);
                narration = cursor.getString(5);
                spinner = cursor.getString(2);
                amount = cursor.getFloat(4);
                rate = cursor.getFloat(3);
                id=cursor.getInt(6);

            }
            Intent i = new Intent(this,Fragment_payment_out.class);
            i.putExtra("name", name);
            i.putExtra("date", date);
            i.putExtra("amount", amount);
            i.putExtra("narration", narration);
            i.putExtra("spinner", spinner);
            i.putExtra("rate", rate);
            i.putExtra("id",id);
            i.putExtra("pos",type);
            i.putExtra("adptpos",adapterposition);
            startActivity(i);
        }


    }
    @Override
    public void donbuttonclick(String datefrom, String dateto) {


        if (dateto.length() == 0 && datefrom.length() != 0) {
            diplaydatefrom(datefrom);
        } else if (datefrom.length() != 0 && dateto.length() != 0) {
            diplaydatefrom_to(datefrom, dateto);
        } else if ((datefrom.length() == 0 && dateto.length() == 0)) {
            Toast.makeText(this, "Failed:Date range not provided", Toast.LENGTH_LONG).show();
        }
    }

    public void sortdate(String selector) {

        Individual_Trans_ArrayList.clear();
        cursor = bandakDatabaseAdapter.sortdate(name, selector);

        while (cursor.moveToNext()) {
            Individual_Trans_ArrayList.add(new Individual_trans_data(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5)));
        }
        individualtransAdapter.setdata(Individual_Trans_ArrayList);
;

    }

    public void sortamt(String selector) {

        Individual_Trans_ArrayList.clear();
        cursor = bandakDatabaseAdapter.sortamount(name,  selector);
        while (cursor.moveToNext()) {
            Individual_Trans_ArrayList.add(new Individual_trans_data(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5)));
        }
        individualtransAdapter.setdata(Individual_Trans_ArrayList);
;
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

    public void diplaydatefrom(String date) {

        Individual_Trans_ArrayList.clear();
        cursor = bandakDatabaseAdapter.onlyfrom(name, date);
        while (cursor.moveToNext()) {
            Individual_Trans_ArrayList.add(new Individual_trans_data(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5)));
        }
        individualtransAdapter.refreshlist(Individual_Trans_ArrayList);

    }

    public void diplaydatefrom_to(String from, String to) {
        Individual_Trans_ArrayList.clear();
        cursor = bandakDatabaseAdapter.fromandto(name, from, to);
        while (cursor.moveToNext()) {
            Individual_Trans_ArrayList.add(new Individual_trans_data(cursor.getString(0), cursor.getFloat(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5)));
        }
        individualtransAdapter.refreshlist(Individual_Trans_ArrayList);
        ;
    }







    public static class DateRangeDialog extends DialogFragment {
        Button dialogdonebutton, dialogbackbutton;
        TextInputEditText datefromedittext, datetoedittext;
        TextInputLayout datetolayout, datefromlayout;
        TextView text;
        rangedialoglistener listener;

        public DateRangeDialog(rangedialoglistener listener) {
            this.listener = listener;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.daterangedialog, container);
            datefromedittext = v.findViewById(R.id.datefrom);
            datetolayout = v.findViewById(R.id.datetolayout);
            datetoedittext = v.findViewById(R.id.dateto);
            dialogdonebutton = v.findViewById(R.id.rangedonebutton);
            dialogbackbutton = v.findViewById(R.id.rangebackbutton);
            datetolayout.setVisibility(View.INVISIBLE);
            datetoedittext.setVisibility(View.INVISIBLE);
            datefromlayout = v.findViewById(R.id.datefromlayout);
            datefromedittext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    datetolayout.setVisibility(View.VISIBLE);
                    datetoedittext.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            dialogdonebutton.setOnClickListener(this::dontbuttonlistener);
            dialogbackbutton.setOnClickListener(this::backbuttonlistener);
            text = v.findViewById(R.id.rangetextviewmiddle);
            text.setVisibility(View.INVISIBLE);
            return v;
        }

        public void dontbuttonlistener(View v) {
            listener.donbuttonclick(datefromedittext.getText().toString(), datetoedittext.getText().toString());
            getDialog().onBackPressed();

        }

        public void backbuttonlistener(View v) {
            getDialog().onBackPressed();
        }

        @Override
        public void onResume() {
            super.onResume();
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            params.width = 1070;
            params.height = 750;
            getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        }

        }
    public String gettodaysdate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateConverter converter = new DateConverter();

        return formatdate(converter.ADTOBS(year, month, day));
    }


    public long getdifference(String start,String end) throws ParseException {
        Date date1;
        Date date2;

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

        //Setting dates
        date1 = dates.parse(start);
        date2 = dates.parse(end);

        //Comparing dates
        long difference = Math.abs(date1.getTime() - date2.getTime());
        return  difference / (24 * 60 * 60 * 1000);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        menu.add(0, 69, 0, "").setIcon(R.drawable.ic_icons8_share_64).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        Drawable drawable = menu.getItem(0).getIcon();
//        drawable.mutate();
//        drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//

class Asynctaskgetdata extends AsyncTask<Void,Void,Void>
{
    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);

       total_amount.setText(String.format("%.2f",totalout-totalin));

        if(totalout-totalin>0)
        {
            drcr.setText("Dr");
        }
        else
        {
            drcr.setText("Cr");
        }
        ontaskdone();

    }

    @Override
    protected Void doInBackground(Void... voids) {
        todaysdate=gettodaysdate();
        try {
            getparticulardata();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void getparticulardata() throws ParseException {
        bandakDatabaseAdapter = new BandakAppDatabaseAdapter(context);
        cursor =bandakDatabaseAdapter.getcombineddata(name);
        totalin=0;
        totalout=0;
        while (cursor.moveToNext()) {
            String partyname=name;
            String date=cursor.getString(0);
            float amount=cursor.getFloat(1);
            float rate= cursor.getFloat(2);
            int type= cursor.getInt(4);
            String narration= cursor.getString(3);
            if(type==0)
            {
                totalin+=amount;
            }
            else if(amount!=0)
            {
                if(narration.equals("Opening Balance")&& amount!=0)
                {
                    totalout+=amount;

                }
                else
                {
                    totalout+=(((((getdifference(todaysdate,date))/(float)365)*rate*12*amount)/(float)100)+amount);

                }


            }
            Individual_Trans_ArrayList.add(new Individual_trans_data(date,amount ,rate,narration,type, cursor.getInt(5)));
        }

    }



}
}

interface rangedialoglistener {
    void donbuttonclick(String datefrom, String dateto);

}