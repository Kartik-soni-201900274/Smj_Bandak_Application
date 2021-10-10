package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.Bandak.BandakAppDatabaseAdapter;
import com.example.myapplication.Bandak.DateConverter;
import com.example.myapplication.Bandak.DisplayTransParticularparty;
import com.example.myapplication.Bandak.Show_All_Party_ADAPTER;
import com.example.myapplication.Bandak.bndkmnaddparty;
import com.example.myapplication.Bandak.show_All_party_Data;
import com.example.myapplication.Bandak.update_party;
import com.example.myapplication.Bandak.viewpartydetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class showallparty extends AppCompatActivity implements Show_All_Party_ADAPTER.sop_clickinterface {
    RecyclerView recycler;
    TextInputEditText searchtext;
    public ArrayList<show_All_party_Data> sop_Arraylist = new ArrayList<>();
    TextWatcher sop_textwatcher;
    String contact;
    String name, phone, address;
    int pos;
    Context context = this;
    int counter;
    Asynctaskcollectdata asynctaskcollectdata;
    Show_All_Party_ADAPTER.sop_clickinterface sop_clickinterface = this;
    FloatingActionButton fab;
    Show_All_Party_ADAPTER show_all_party_adapter;
    float totalin = 0, totalout = 0;
    String todaysdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showallparty);
        setviewstoid();
        asynctaskcollectdata = new Asynctaskcollectdata();
        addtextWatcher();


    }

    public void ontaskdone() {
        if (show_all_party_adapter == null) {

            managerecycler();
        } else {
            show_all_party_adapter.setdata(sop_Arraylist);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (show_all_party_adapter == null) {

            asynctaskcollectdata = new Asynctaskcollectdata();
            asynctaskcollectdata.execute();
        } else if (show_all_party_adapter != null) {

            sop_Arraylist.clear();
            asynctaskcollectdata = new Asynctaskcollectdata();
            asynctaskcollectdata.execute();
        }
    }

    public void setviewstoid() {
        recycler = findViewById(R.id.soprecyclerView);
        searchtext = findViewById(R.id.sop_search);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), bndkmnaddparty.class);
                startActivity(i);
            }
        });

    }

    public void addtextWatcher() {
        sop_textwatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                show_all_party_adapter.getFilter().filter(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        searchtext.addTextChangedListener(sop_textwatcher);
    }

    public void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            dialcontact();
        }
    }

    public void dialcontact() {
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel:" + contact));
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dialcontact();
            } else {
                Toast.makeText(this, "PERMISSION DENIED", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void oncontactclick(String contact) {
        this.contact = contact;
        checkpermission();
    }

    @Override
    public void onlongclick(CardView v, int pos) {
        this.pos = pos;
        v.setOnCreateContextMenuListener(this::onCreateContextMenu);
        name = sop_Arraylist.get(pos).name;
        phone = sop_Arraylist.get(pos).contact;
        address = sop_Arraylist.get(pos).address;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, 69, 0, "").setIcon(R.drawable.addparty).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        Drawable drawable = menu.getItem(0).getIcon();
        drawable.mutate();
        drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 69) {
            Intent i = new Intent(getApplicationContext(), bndkmnaddparty.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onclick(String name) {
        Intent i = new Intent(this, DisplayTransParticularparty.class);
        i.putExtra("party name", name);
        startActivity(i);
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater inflater = getMenuInflater();
        contextMenu.setHeaderTitle("Choose an option");
        inflater.inflate(R.menu.longpressmenu, contextMenu);

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.lpedit: {
                Intent i = new Intent(this, update_party.class);
                i.putExtra("updatepartyname", name);
                i.putExtra("updatepartyphone", phone);
                i.putExtra("updatepartyaddress", address);

                startActivity(i);
                return true;
            }
            case R.id.lpdelete: {

                BandakAppDatabaseAdapter bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(this);
                bandakAppDatabaseAdapter.deleteparty(name, phone);
                show_all_party_adapter.notifyitemremoved(pos);
                Toast.makeText(this, "Deleted Successfully", Toast.LENGTH_LONG).show();
                return true;
            }
            case R.id.lpedetails: {
                Intent i = new Intent(this, viewpartydetails.class);
                i.putExtra("name", name);
                startActivity(i);
                return true;
            }
            default: {
                return true;
            }
        }
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

    public String gettodaysdate() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateConverter converter = new DateConverter();

        return formatdate(converter.ADTOBS(year, month, day));
    }

    public void managerecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(linearLayoutManager);
        show_all_party_adapter = new Show_All_Party_ADAPTER(sop_Arraylist, context, sop_clickinterface);
        recycler.setAdapter(show_all_party_adapter);
    }

    public long getdifference(String start, String end) throws ParseException {
        Date date1;
        Date date2;

        SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

        //Setting dates
        date1 = dates.parse(start);
        date2 = dates.parse(end);

        //Comparing dates
        long difference = Math.abs(date1.getTime() - date2.getTime());
        return difference / (24 * 60 * 60 * 1000);
    }

    class Asynctaskcollectdata extends AsyncTask<Void, Void, Void> {
ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading ...");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            pDialog.dismiss();
            ontaskdone();


        }

        @Override
        protected Void doInBackground(Void... voids) {
            todaysdate = gettodaysdate();

            try {
                collectdata();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void collectdata() throws ParseException {

            Cursor cursorpartydata,cursorbalance;
            BandakAppDatabaseAdapter bandakAppDatabaseAdapter = new BandakAppDatabaseAdapter(context);
            cursorpartydata = bandakAppDatabaseAdapter.showallparty();

            while (cursorpartydata.moveToNext()) {
                String partyname = cursorpartydata.getString(0);
                totalin = 0;
                totalout = 0;

                cursorbalance = bandakAppDatabaseAdapter.getcombineddata(partyname);

                while (cursorbalance.moveToNext()) {

                    String date = cursorbalance.getString(0);
                    float amount = cursorbalance.getFloat(1);
                    float rate = cursorbalance.getFloat(2);
                    int type = cursorbalance.getInt(4);
                    String narration= cursorbalance.getString(3);
                    if (type == 0) {
                        totalin += amount;
                    } else if (amount != 0) {
                        if(narration.equals("Opening Balance")&& amount!=0)
                        {
                            totalout+=amount;
                            continue;
                        }
                        totalout += (((((getdifference(todaysdate, date)) / (float) 365) * rate * 12 * amount) / (float) 100) + amount);


                    }
                }

                sop_Arraylist.add(new show_All_party_Data(partyname, cursorpartydata.getString(1), cursorpartydata.getString(2),totalout-totalin));


            }


        }

    }
}


