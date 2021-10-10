package com.example.myapplication.Bandak;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.security.acl.LastOwnerException;

public class BandakAppDatabaseAdapter {
    Context context2;
    BandakAppDatabase db;
    int counter_id = 1;
    public static final int pi = 0;
    public static final int po = 1;


    public BandakAppDatabaseAdapter(Context context) {
        db = new BandakAppDatabase(context);
        this.context2 = context;
    }

    public Cursor getColoumnWithID(int id, int position) {

        SQLiteDatabase sdb = db.getWritableDatabase();

        if (position == 0) {

            String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentIn_DATE, BandakAppDatabase.COLOUMN_PaymentIn_PAYMENT_METHOD, BandakAppDatabase.COLOUMN_PaymentIn_amount, BandakAppDatabase.COLOUMN_PaymentIn_NARRATION, BandakAppDatabase.COLOUMN_PaymentIn_ID};
            String Selection = BandakAppDatabase.COLOUMN_PaymentIn_ID + " =?";
            String[] selectionargs = {String.valueOf(id)};
            Cursor cursor = sdb.query(BandakAppDatabase.PaymentIn_TABLE_NAME, coloumns, Selection, selectionargs, null, null, null);
            return cursor;
        } else {
            String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentOUT_DATE, BandakAppDatabase.COLOUMN_PaymentOUT_PAYMENT_METHOD, BandakAppDatabase.COLOUMN_PaymentOUT_RATE, BandakAppDatabase.COLOUMN_PaymentOUT_amount, BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION, BandakAppDatabase.COLOUMN_PaymentOut_ID};
            String Selection = BandakAppDatabase.COLOUMN_PaymentOut_ID + " =?";
            String[] selectionargs = {String.valueOf(id)};
            Cursor cursor = sdb.query(BandakAppDatabase.PaymentOut_TABLE_NAME, coloumns, Selection, selectionargs, null, null, null);
            return cursor;
        }
    }


    public long insertNewParty(String name, String phone, String address, String telephone,float openingblnc,float principal, float interest, String additional, String openingblncdate ) {
        ContentValues cv = new ContentValues();
        cv.put(BandakAppDatabase.COLOUMN_PARTYNAME, name);
        cv.put(BandakAppDatabase.COLOUMN_PARTYPHONE, phone);
        cv.put(BandakAppDatabase.COLOUMN_PARTYADDRESS, address);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_TELEPHONE, telephone);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_ADDITIONAL_INFO, additional);

        cv.put(BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE, interest);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE, openingblnc);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_PRINCIPALBALANCE, principal);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE_DATE, openingblncdate);
        cv.put(BandakAppDatabase.COLOUMN_PID, counter_id++);
        SQLiteDatabase sdb = db.getWritableDatabase();
        long id = sdb.insert(BandakAppDatabase.PARTY_INFO_TABLE_NAME, null, cv);
        return id;

    }
    public Cursor showrecordsNewPartywitharguement(String keyword) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PARTYNAME, BandakAppDatabase.COLOUMN_PARTYPHONE,
                BandakAppDatabase.COLOUMN_PARTY_TELEPHONE,
                BandakAppDatabase.COLOUMN_PARTYADDRESS,BandakAppDatabase.COLOUMN_PARTY_ADDITIONAL_INFO,
                BandakAppDatabase.COLOUMN_PARTY_PRINCIPALBALANCE,BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE,
                BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE_DATE };
        String SelectionArgs = BandakAppDatabase.COLOUMN_PARTYNAME + " =?";
        String[] selection = {keyword};
        Cursor cursor = sdb.query(BandakAppDatabase.PARTY_INFO_TABLE_NAME, coloumns, SelectionArgs, selection, null, null, null);
        return cursor;
    }


    public Cursor displaytransparticularpartyPO(String Partyname) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentOUT_DATE, BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentOUT_amount, BandakAppDatabase.COLOUMN_PaymentOut_ID};
        String Selection = BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME + " =?";
        String[] selectionargs = {Partyname};
        Cursor cursor = sdb.query(BandakAppDatabase.PaymentOut_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_PaymentOUT_DATE + " DESC");
        return cursor;
    }

    public Cursor displaytransparticularpartyPI(String Partyname) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentIn_DATE, BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentIn_amount, BandakAppDatabase.COLOUMN_PaymentIn_ID};
        String Selection = BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME + " =?";
        String[] selectionargs = {Partyname};
        Cursor cursor = sdb.query(BandakAppDatabase.PaymentIn_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_PaymentIn_DATE + " DESC");
        return cursor;
    }

    public int insertNewPaymentin(Float amount, String date, String payment_method, String narration, String payment_in_partyname) {
        ContentValues cv = new ContentValues();

        cv.put(BandakAppDatabase.COLOUMN_PaymentIn_PAYMENT_METHOD, payment_method);
        cv.put(BandakAppDatabase.COLOUMN_PaymentIn_NARRATION, narration);
        cv.put(BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME, payment_in_partyname);
        cv.put(BandakAppDatabase.COLOUMN_PaymentIn_DATE, date);
        cv.put(BandakAppDatabase.COLOUMN_PaymentIn_amount, amount);


        SQLiteDatabase sdb = db.getWritableDatabase();

        int id = (int) sdb.insert(BandakAppDatabase.PaymentIn_TABLE_NAME, null, cv);
        if (id >= 0) {
            ContentValues cv_combined = new ContentValues();

            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PAYMENT_METHOD, payment_method);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, narration);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, payment_in_partyname);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_amount, amount);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_type, pi);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_ID, id);

            sdb.insert(BandakAppDatabase.COMBINED_TABLE_NAME, null, cv_combined);
        }

        Toast.makeText(context2, "added successfully", Toast.LENGTH_LONG).show();
        return id;

    }

    public int insertNewPaymentOut(Float amount, String date, String payment_method, Float rate, String narration, String payment_out_partyname) {
        ContentValues cv = new ContentValues();

        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_PAYMENT_METHOD, payment_method);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION, narration);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, payment_out_partyname);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE, date);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_RATE, rate);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount, amount);
        SQLiteDatabase sdb = db.getWritableDatabase();
        int id = (int) sdb.insert(BandakAppDatabase.PaymentOut_TABLE_NAME, null, cv);

        if (id >= 0) {
            ContentValues cv_combined = new ContentValues();

            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PAYMENT_METHOD, payment_method);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, narration);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, payment_out_partyname);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_amount, amount);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_type, po);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_ID, id);
            cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_RATE, rate);

            sdb.insert(BandakAppDatabase.COMBINED_TABLE_NAME, null, cv_combined);
        }


        Toast.makeText(context2, "added successfully", Toast.LENGTH_LONG).show();
        return id;

    }


    public Cursor showrecordsNewParty() {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PARTYNAME, BandakAppDatabase.COLOUMN_PARTYPHONE, BandakAppDatabase.COLOUMN_PARTYADDRESS, BandakAppDatabase.COLOUMN_PID};
        Cursor cursor = sdb.query(BandakAppDatabase.PARTY_INFO_TABLE_NAME, coloumns, null, null, null, null, null);
        return cursor;
    }


    public Cursor showrecordsPayment_in() {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentIn_amount, BandakAppDatabase.COLOUMN_PaymentIn_DATE, BandakAppDatabase.COLOUMN_PaymentIn_ID, BandakAppDatabase.COLOUMN_PaymentIn_NARRATION};
        Cursor cursor = sdb.query(BandakAppDatabase.PaymentIn_TABLE_NAME, coloumns, null, null, null, null, null);
        return cursor;
    }

    public Cursor showrecordsPayment_out() {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, BandakAppDatabase.COLOUMN_PaymentOUT_amount, BandakAppDatabase.COLOUMN_PaymentOUT_DATE, BandakAppDatabase.COLOUMN_PaymentOUT_RATE,BandakAppDatabase.COLOUMN_PaymentOut_ID,BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION};
        Cursor cursor = sdb.query(BandakAppDatabase.PaymentOut_TABLE_NAME, coloumns, null, null, null, null, null);
        return cursor;
    }


    public Cursor sortdate(String Partyname, String selector) {

        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_COMBINED_DATE, BandakAppDatabase.COLOUMN_COMBINED_amount, BandakAppDatabase.COLOUMN_COMBINED_RATE, BandakAppDatabase.COLOUMN_COMBINED_NARRATION, BandakAppDatabase.COLOUMN_COMBINED_type, BandakAppDatabase.COLOUMN_COMBINED_ID};
        String Selection = BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME + " =?";
        String[] selectionargs = {Partyname};

        Cursor cursor = sdb.query(BandakAppDatabase.COMBINED_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_COMBINED_DATE + selector);
        return cursor;


    }

    public Cursor sortamount(String Partyname, String selector) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_COMBINED_DATE, BandakAppDatabase.COLOUMN_COMBINED_amount, BandakAppDatabase.COLOUMN_COMBINED_RATE, BandakAppDatabase.COLOUMN_COMBINED_NARRATION, BandakAppDatabase.COLOUMN_COMBINED_type, BandakAppDatabase.COLOUMN_COMBINED_ID};
        String Selection = BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME + " =?";
        String[] selectionargs = {Partyname};

        Cursor cursor = sdb.query(BandakAppDatabase.COMBINED_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_COMBINED_amount + selector);
        return cursor;

    }

    public Cursor onlyfrom(String name, String date) {
        Cursor cursor = null;

        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_COMBINED_DATE, BandakAppDatabase.COLOUMN_COMBINED_amount, BandakAppDatabase.COLOUMN_COMBINED_RATE, BandakAppDatabase.COLOUMN_COMBINED_NARRATION, BandakAppDatabase.COLOUMN_COMBINED_type, BandakAppDatabase.COLOUMN_COMBINED_ID};
        String Selection = BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME + " =?" + " AND  " + BandakAppDatabase.COLOUMN_COMBINED_DATE + " >=?";
        String[] selectionargs = {name, date};
        cursor = sdb.query(BandakAppDatabase.COMBINED_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_COMBINED_DATE + " DESC");


        return cursor;
    }

    public Cursor fromandto(String name, String from, String to) {
        Cursor cursor = null;

        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_COMBINED_DATE, BandakAppDatabase.COLOUMN_COMBINED_amount, BandakAppDatabase.COLOUMN_COMBINED_RATE, BandakAppDatabase.COLOUMN_COMBINED_NARRATION, BandakAppDatabase.COLOUMN_COMBINED_type, BandakAppDatabase.COLOUMN_COMBINED_ID};
        String Selection = BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME + " =?" + " AND " + BandakAppDatabase.COLOUMN_COMBINED_DATE + " BETWEEN ? AND ?";
        String[] selectionargs = {name, from, to};
        cursor = sdb.query(BandakAppDatabase.COMBINED_TABLE_NAME, coloumns, Selection, selectionargs, null, null, BandakAppDatabase.COLOUMN_COMBINED_DATE + " DESC");


        return cursor;
    }

    public int deleteindividualtrans(int id, int pos) {
        int rowid = -1;
        SQLiteDatabase sdb = db.getWritableDatabase();
        if (pos == 0) {
            String where = BandakAppDatabase.COLOUMN_PaymentIn_ID + " =?";
            String[] whereArgs = {String.valueOf(id)};
            rowid = sdb.delete(BandakAppDatabase.PaymentIn_TABLE_NAME, where, whereArgs);
            deletecombinedwithid(id, pos);
        } else if (pos == 1) {
            String where = BandakAppDatabase.COLOUMN_PaymentOut_ID + " =?";
            String[] whereArgs = {String.valueOf(id)};
            rowid = sdb.delete(BandakAppDatabase.PaymentOut_TABLE_NAME, where, whereArgs);
            deletecombinedwithid(id, pos);
        }
        return rowid;
    }

    public void updatewithid(String oldname,int id, int pos, Float amount, String date, String payment_method, Float rate, String narration, String payment_in_partyname) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        updatepartyname(oldname,payment_in_partyname);

        if (pos == 0) {
            ContentValues cv = new ContentValues();
            cv.put(BandakAppDatabase.COLOUMN_PaymentIn_PAYMENT_METHOD, payment_method);
            cv.put(BandakAppDatabase.COLOUMN_PaymentIn_NARRATION, narration);

            cv.put(BandakAppDatabase.COLOUMN_PaymentIn_DATE, date);
            cv.put(BandakAppDatabase.COLOUMN_PaymentIn_amount, amount);
            String where = BandakAppDatabase.COLOUMN_PaymentIn_ID + " =?";
            String[] whereArgs = {String.valueOf(id)};
            sdb.update(BandakAppDatabase.PaymentIn_TABLE_NAME, cv, where, whereArgs);
            updatepartyinterest(amount,payment_in_partyname);
            updatecombinedwithid(id, pos, amount, date, payment_method, rate, narration, payment_in_partyname);
        } else {
            ContentValues cv = new ContentValues();

            cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_PAYMENT_METHOD, payment_method);
            cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION, narration);

            cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE, date);
            cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_RATE, rate);
            cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount, amount);
            String where = BandakAppDatabase.COLOUMN_PaymentOut_ID + " =?";
            String[] whereArgs = {String.valueOf(id)};
            updatepartyprincipal(amount,payment_in_partyname);
            sdb.update(BandakAppDatabase.PaymentOut_TABLE_NAME, cv, where, whereArgs);
            updatecombinedwithid(id, pos, amount, date, payment_method, rate, narration, payment_in_partyname);
        }
    }
    public void updatepartyname(String oldname,String newname)
    {
        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BandakAppDatabase.COLOUMN_PARTYNAME, newname);
        String where = BandakAppDatabase.COLOUMN_PARTYNAME + " =?";
        String[] whereArgs = {oldname};
        sdb.update(BandakAppDatabase.PARTY_INFO_TABLE_NAME, cv, where, whereArgs);
    }
    public Cursor getsumpi(String name) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String query = " select SUM( combined_amount )  as totalpi from combined" +
                " where combined_payment_type=0 AND combined_PartyName=?";
        String[] selectionargs = {name};
        Cursor cursor = sdb.rawQuery(query, selectionargs);
        return cursor;
    }

    public Cursor getsumpo(String name) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String query = "select SUM( combined_amount )  as totalpo from combined" +
                " where combined_payment_type=1 AND combined_PartyName=?";

        String[] selectionargs = {name};
        Cursor cursor = sdb.rawQuery(query, selectionargs);
        return cursor;
    }

    public Cursor showonlynames(int pos) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        Cursor cursor = null;
        if (pos == 0) {
            String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME};
            cursor = sdb.query(BandakAppDatabase.PaymentIn_TABLE_NAME, coloumns, null, null, null, null, null);
        } else {
            String[] coloumns = {BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME};
            cursor = sdb.query(BandakAppDatabase.PaymentOut_TABLE_NAME, coloumns, null, null, null, null, null);
        }
        return cursor;
    }

    public Cursor showallparty() {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PARTYNAME, BandakAppDatabase.COLOUMN_PARTYADDRESS, BandakAppDatabase.COLOUMN_PARTYPHONE};
        Cursor cursor = sdb.query(BandakAppDatabase.PARTY_INFO_TABLE_NAME, coloumns, null, null, null, null, BandakAppDatabase.COLOUMN_PARTYNAME + " ASC");
        return cursor;

    }

    public Cursor showallpartycomplete(String name) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String[] coloumns = {BandakAppDatabase.COLOUMN_PARTYNAME,
                BandakAppDatabase.COLOUMN_PARTYADDRESS,
                BandakAppDatabase.COLOUMN_PARTYPHONE,
                BandakAppDatabase.COLOUMN_PARTY_ADDITIONAL_INFO,
                BandakAppDatabase.COLOUMN_PARTY_PRINCIPALBALANCE,
                BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE,
                BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE_DATE,
                BandakAppDatabase.COLOUMN_PARTY_TELEPHONE,
                BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE,};
        String where=BandakAppDatabase.COLOUMN_PARTYNAME+" =?";
        String whereargs[]={name};
        Cursor cursor = sdb.query(BandakAppDatabase.PARTY_INFO_TABLE_NAME, coloumns, where, whereargs, null, null,null);
        return cursor;

    }

    public int updateparty(String sname, String sphone,String name, String phone, String address, String telephone, float principal, float interest, String additional, String openingblncdate,float openingbalannce) {
        SQLiteDatabase sdb = db.getWritableDatabase();

        String where = BandakAppDatabase.COLOUMN_PARTYNAME + " =? AND " + BandakAppDatabase.COLOUMN_PARTYPHONE + " =?";
        ContentValues cv = new ContentValues();
        cv.put(BandakAppDatabase.COLOUMN_PARTYNAME, name);
        cv.put(BandakAppDatabase.COLOUMN_PARTYPHONE, phone);
        cv.put(BandakAppDatabase.COLOUMN_PARTYADDRESS, address);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_TELEPHONE, telephone);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_ADDITIONAL_INFO, additional);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE, openingbalannce);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE, interest);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_PRINCIPALBALANCE, principal);
        cv.put(BandakAppDatabase.COLOUMN_PARTY_OPENING_BALANCE_DATE, openingblncdate);
        String[] whereargs = {sname, sphone};
        return sdb.update(BandakAppDatabase.PARTY_INFO_TABLE_NAME, cv, where, whereargs);



    }



    public void updateinterestandprincipal(String name,float interest,float principal,String date)
    {
        String opening="Opening Principal";
        String intereststring="Opening Interest";
        SQLiteDatabase sdb = db.getWritableDatabase();
        if(interest==0.00)
        {
            ContentValues cv_po=new ContentValues();
            cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount,principal);
            cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE,date);
            String where_po=BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION +" =?";
            String[] whereargs_po = {name,opening};
            sdb.update(BandakAppDatabase.PaymentOut_TABLE_NAME, cv_po, where_po, whereargs_po);

            ContentValues cv_cout=new ContentValues();
            cv_cout.put(BandakAppDatabase.COLOUMN_COMBINED_amount,principal);
            cv_cout.put(BandakAppDatabase.COLOUMN_COMBINED_DATE,date);
            String where_cout=BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_COMBINED_NARRATION + " =?";
            String[] whereargs_cout = {name,opening};
            sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv_cout, where_cout, whereargs_cout);

        }
        else if(principal==0.00)
        {
            ContentValues cv_pi=new ContentValues();
            cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_amount,interest);
            cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_DATE,date);
            String where_pi=BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_PaymentIn_NARRATION+" =?";
            String[] whereargs_pi = {name,intereststring};
            sdb.update(BandakAppDatabase.PaymentIn_TABLE_NAME, cv_pi, where_pi, whereargs_pi);


            ContentValues cv_cin=new ContentValues();
            cv_cin.put(BandakAppDatabase.COLOUMN_COMBINED_amount,interest);
            cv_cin.put(BandakAppDatabase.COLOUMN_COMBINED_DATE,date);
            String where_cin=BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_COMBINED_NARRATION +" =?";
            String[] whereargs_cin = {name,intereststring};
            sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv_cin, where_cin, whereargs_cin);
        }
        else if(principal!=0.00 && interest!=0.00 )
        {
            ContentValues cv_po=new ContentValues();
            cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount,principal);
            cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE,date);
            String where_po=BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION+" =?";
            String[] whereargs_po = {name,opening};
            sdb.update(BandakAppDatabase.PaymentOut_TABLE_NAME, cv_po, where_po, whereargs_po);

            ContentValues cv_cout=new ContentValues();
            cv_cout.put(BandakAppDatabase.COLOUMN_COMBINED_amount,principal);
            cv_cout.put(BandakAppDatabase.COLOUMN_COMBINED_DATE,date);
            String where_cout=BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_COMBINED_NARRATION + " =?";
            String[] whereargs_cout = {name,opening};
            sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv_cout, where_cout, whereargs_cout);

            ContentValues cv_pi=new ContentValues();
            cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_amount,interest);
            cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_DATE,date);
            String where_pi=BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_PaymentIn_NARRATION+" =?";
            String[] whereargs_pi = {name,intereststring};
            sdb.update(BandakAppDatabase.PaymentIn_TABLE_NAME, cv_pi, where_pi, whereargs_pi);


            ContentValues cv_cin=new ContentValues();
            cv_cin.put(BandakAppDatabase.COLOUMN_COMBINED_amount,interest);
            cv_cin.put(BandakAppDatabase.COLOUMN_COMBINED_DATE,date);
            String where_cin=BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME +" =? AND "+BandakAppDatabase.COLOUMN_COMBINED_NARRATION + " =?";
            String[] whereargs_cin = {name,intereststring};
            sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv_cin, where_cin, whereargs_cin);
        }




    }


    public void deleteparty(String name, String phone) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String where = BandakAppDatabase.COLOUMN_PARTYNAME + " =? AND " + BandakAppDatabase.COLOUMN_PARTYPHONE + " =?";
        String[] whereargs = {name, phone};
        sdb.delete(BandakAppDatabase.PARTY_INFO_TABLE_NAME, where, whereargs);

    }

    public void updatecombinedwithid(int id, int type, Float amount, String date, String payment_method, Float rate, String narration, String partyname) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues cv_combined = new ContentValues();
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PAYMENT_METHOD, payment_method);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, narration);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, partyname);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_amount, amount);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_type, type);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_ID, id);
        cv_combined.put(BandakAppDatabase.COLOUMN_COMBINED_RATE, rate);
        String where = BandakAppDatabase.COLOUMN_COMBINED_ID + " =? AND " + BandakAppDatabase.COLOUMN_COMBINED_type + " =?";
        String[] whereArgs = {String.valueOf(id), String.valueOf(type)};
        sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv_combined, where, whereArgs);
        sdb.close();
    }

    public void deletecombinedwithid(int id, int type) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String where = BandakAppDatabase.COLOUMN_COMBINED_ID + " =? AND " + BandakAppDatabase.COLOUMN_COMBINED_type + " =?";
        String[] whereArgs = {String.valueOf(id), String.valueOf(type)};
        sdb.delete(BandakAppDatabase.COMBINED_TABLE_NAME, where, whereArgs);
        sdb.close();
    }


    public Cursor getcombineddata(String name) {
        SQLiteDatabase sdb = db.getWritableDatabase();
        String where=BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME+" =?";
        String [] wherargs={name};
        String[] coloumns = {BandakAppDatabase.COLOUMN_COMBINED_DATE,
                BandakAppDatabase.COLOUMN_COMBINED_amount,
                BandakAppDatabase.COLOUMN_COMBINED_RATE,
                BandakAppDatabase.COLOUMN_COMBINED_NARRATION,
                BandakAppDatabase.COLOUMN_COMBINED_type,
                BandakAppDatabase.COLOUMN_COMBINED_ID};
        Cursor cursor = sdb.query(BandakAppDatabase.COMBINED_TABLE_NAME, coloumns, where, wherargs, null, null, null);
        return cursor;
    }
public void addinterest(float interest, String date, String name)
{
    ContentValues cv_pi = new ContentValues();
    cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_NARRATION, "Opening Interest");
    cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_PARTYNAME, name);
    cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_DATE, date);
    cv_pi.put(BandakAppDatabase.COLOUMN_PaymentIn_amount, interest);

    SQLiteDatabase sdb = db.getWritableDatabase();
    int id = (int) sdb.insert(BandakAppDatabase.PaymentIn_TABLE_NAME, null, cv_pi);
    if (id >= 0) {
        ContentValues cv_combined_pi = new ContentValues();
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, "Opening Interest");
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, name);
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_amount, interest);
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_type, pi);
        cv_combined_pi.put(BandakAppDatabase.COLOUMN_COMBINED_ID, id);

        sdb.insert(BandakAppDatabase.COMBINED_TABLE_NAME, null, cv_combined_pi);
    }

}
    public void addopeningbalance(float openingbalance, String date, String name)
    {

        SQLiteDatabase sdb = db.getWritableDatabase();

        ContentValues cv_po = new ContentValues();
        cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION, "Opening Balance");
        cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, name);
        cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE, date);
        cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount, openingbalance);
        cv_po.put(BandakAppDatabase.COLOUMN_PaymentOUT_RATE, 0);

     long id=   sdb.insert(BandakAppDatabase.PaymentOut_TABLE_NAME, null, cv_po);


        ContentValues cv_combined_po = new ContentValues();
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, "Opening Balance");
        cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, name);
        cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
        cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_amount, openingbalance);
        cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_type, po);
        cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_ID,id);





            sdb.insert(BandakAppDatabase.COMBINED_TABLE_NAME, null, cv_combined_po);


    }


public void updatepartyinterest(float interest,String name)
{

    SQLiteDatabase sdb = db.getWritableDatabase();
    ContentValues cv = new ContentValues();
    cv.put(BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE,interest);
    String where=BandakAppDatabase.COLOUMN_PARTYNAME+" =?";
    String whereargs[]={name};
    sdb.update(BandakAppDatabase.PARTY_INFO_TABLE_NAME,cv,where,whereargs);

}
    public void updatepartyprincipal(float principal,String name)
    {

        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BandakAppDatabase.COLOUMN_PARTY_INTERESTBALANCE,principal);
        String where=BandakAppDatabase.COLOUMN_PARTYNAME+" =?";
        String whereargs[]={name};
        sdb.update(BandakAppDatabase.PARTY_INFO_TABLE_NAME,cv,where,whereargs);

    }
    public void updatepartyopeningbalance(float openingbalance,String name,String date) {

        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(BandakAppDatabase.COLOUMN_COMBINED_amount, openingbalance);
        cv.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
        String where = BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME + " =? AND " + BandakAppDatabase.COLOUMN_COMBINED_NARRATION + " =?";
        String whereargs[] = {name, "Opening Balance"};
        sdb.update(BandakAppDatabase.COMBINED_TABLE_NAME, cv, where, whereargs);

        ContentValues cvpo = new ContentValues();


        cvpo.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount, openingbalance);
        cvpo.put(BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME, name);

        cvpo.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE, date);
        String wherepo=BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME+" =? AND "+BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION+" =?";
        String whereargspo[]={name,"Opening Balance"};
        sdb.update(BandakAppDatabase.PaymentOut_TABLE_NAME,cvpo,wherepo,whereargspo);
    }



    public void addprincipal(float principal, String date, String name)
    {
        SQLiteDatabase sdb = db.getWritableDatabase();
        ContentValues cv = new ContentValues();


        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_NARRATION,"Opening Principal");
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_PARTYNAME,name);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_DATE, date);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_RATE, 0.0);
        cv.put(BandakAppDatabase.COLOUMN_PaymentOUT_amount, principal);
        int id_po = (int) sdb.insert(BandakAppDatabase.PaymentOut_TABLE_NAME, null, cv);
        if (id_po >= 0) {
            ContentValues cv_combined_po = new ContentValues();
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_NARRATION, "Opening Principal");
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_PARTYNAME, name);
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_DATE, date);
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_amount, principal);
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_type, po);
            cv_combined_po.put(BandakAppDatabase.COLOUMN_COMBINED_ID, id_po);

            sdb.insert(BandakAppDatabase.COMBINED_TABLE_NAME, null, cv_combined_po);
        }

    }


    public void setopeningbalance(float openingbalance,float interest, float principal, String date, String name) {
        //-------------------------------INTEREST-----------------------------------------------------
        {
            addinterest(interest,date,name);
            addprincipal(principal,date,name);
            addopeningbalance(openingbalance,date,name);

        }





        //-------------------------------PRINCIPAL-----------------------------------------------------

    }

    static class BandakAppDatabase extends SQLiteOpenHelper {
        private static final int DATABSE_VERSION = 24;
        private static final String DATABASE_NAME = "BandakAppdatabase";
        private static final String PARTY_INFO_TABLE_NAME = "PartyInfo";
        private static final String COLOUMN_PARTYNAME = "PartyName";
        private static final String COLOUMN_PARTYPHONE = "PartyPhone";
        private static final String COLOUMN_PARTYADDRESS = "PartyAddress";
        private static final String COLOUMN_PARTY_TELEPHONE = "Partytelephone";
        private static final String COLOUMN_PARTY_ADDITIONAL_INFO = "Partyadditionalinfo";
        private static final String COLOUMN_PARTY_PRINCIPALBALANCE = "Partyprincipalbalance";
        private static final String COLOUMN_PARTY_INTERESTBALANCE = "interestbalance";
        private static final String COLOUMN_PARTY_OPENING_BALANCE = "openingbalancedate";
        private static final String COLOUMN_PARTY_OPENING_BALANCE_DATE = "openingbalance";
        private static final String COLOUMN_PID = "pid";
        private static final String CREATE_TABLE_PARTYINFO = "CREATE TABLE " + PARTY_INFO_TABLE_NAME + "("
                + COLOUMN_PID + " INTEGER,"
                + COLOUMN_PARTYNAME + " VARCHAR(256) PRIMARY KEY,"
                + COLOUMN_PARTYPHONE + " VARCHAR(15),"
                + COLOUMN_PARTYADDRESS + " TEXT,"
                + COLOUMN_PARTY_TELEPHONE + " VARCHAR(15),"
                + COLOUMN_PARTY_ADDITIONAL_INFO + " TEXT,"
                + COLOUMN_PARTY_PRINCIPALBALANCE + " REAL,"
                + COLOUMN_PARTY_OPENING_BALANCE + " REAL,"
                + COLOUMN_PARTY_INTERESTBALANCE + " REAL,"
                + COLOUMN_PARTY_OPENING_BALANCE_DATE + " DATE)";


        //----------------------------------------------------------------------------


        private static final String PaymentIn_TABLE_NAME = "PaymentIn";
        private static final String COLOUMN_PaymentIn_PARTYNAME = "Payment_in_PartyName";
        private static final String COLOUMN_PaymentIn_DATE = "PaymentIn_Date";
        private static final String COLOUMN_PaymentIn_NARRATION = "PaymentIn_Narration";
        private static final String COLOUMN_PaymentIn_amount = "PaymentIn_amount";
        private static final String COLOUMN_PaymentIn_PAYMENT_METHOD = "PaymentIn_Payment_Type";
        private static final String COLOUMN_PaymentIn_ID = "PI_id";
        private static final String CREATE_TABLE_PAYMENT_IN = "CREATE TABLE " +

                PaymentIn_TABLE_NAME + "("
                + COLOUMN_PaymentIn_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLOUMN_PaymentIn_DATE + " DATE,"
                + COLOUMN_PaymentIn_PAYMENT_METHOD + " VARCHAR(20),"
                + COLOUMN_PaymentIn_amount + " REAL,"
                + COLOUMN_PaymentIn_NARRATION + " TEXT,"
                + COLOUMN_PaymentIn_PARTYNAME + " VARCHAR(256),"
                + "FOREIGN KEY (" + COLOUMN_PaymentIn_PARTYNAME + ")" + "REFERENCES " + PARTY_INFO_TABLE_NAME + "(" + COLOUMN_PARTYNAME + ") ON UPDATE CASCADE ON DELETE CASCADE);";


        //----------------------------------------------------------------------------


        private static final String PaymentOut_TABLE_NAME = "PaymentOut";
        private static final String COLOUMN_PaymentOUT_PARTYNAME = "Payment_out_PartyName";
        private static final String COLOUMN_PaymentOUT_DATE = "PaymentOut_Date";
        private static final String COLOUMN_PaymentOUT_NARRATION = "Paymentout_Narration";
        private static final String COLOUMN_PaymentOUT_amount = "Paymentout_amount";
        private static final String COLOUMN_PaymentOUT_PAYMENT_METHOD = "Paymentout_Payment_Type";
        private static final String COLOUMN_PaymentOUT_RATE = "Paymentout_rate";
        private static final String COLOUMN_PaymentOut_ID = "Paymentout_id";
//        private static final String COLOUMN_TODAYS_INTEREST = "todays_interest";
//        private static final String COLOUMN_TODAYS_DATE = "todays_date";

        private static final String CREATE_TABLE_PaymentOUT = "CREATE TABLE " +
                PaymentOut_TABLE_NAME + "("
                + COLOUMN_PaymentOut_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLOUMN_PaymentOUT_DATE + " DATE,"
                + COLOUMN_PaymentOUT_PAYMENT_METHOD + " VARCHAR(20),"
                + COLOUMN_PaymentOUT_amount + " REAL,"
                + COLOUMN_PaymentOUT_RATE + " REAL,"
                + COLOUMN_PaymentOUT_NARRATION + " TEXT,"
                + COLOUMN_PaymentOUT_PARTYNAME + " VARCHAR(256),"
//                + COLOUMN_TODAYS_DATE + " DATE,"
//                + COLOUMN_TODAYS_INTEREST +" REAL GENERATED ALWAYS AS (((("+COLOUMN_TODAYS_DATE+"-"+COLOUMN_PaymentOUT_DATE+")/365)*"+COLOUMN_PaymentOUT_RATE+COLOUMN_PaymentOUT_amount+")/100) STORED,"
                + "FOREIGN KEY (" + COLOUMN_PaymentOUT_PARTYNAME + ")" + "REFERENCES " + PARTY_INFO_TABLE_NAME + "(" + COLOUMN_PARTYNAME + ") ON UPDATE CASCADE ON DELETE CASCADE);";


//------------------------------------------------------------------------------------------------------------------------------

        private static final String COMBINED_TABLE_NAME = "combined";
        private static final String COLOUMN_COMBINED_PARTYNAME = "combined_PartyName";
        private static final String COLOUMN_COMBINED_DATE = "combined_Date";
        private static final String COLOUMN_COMBINED_NARRATION = "combined_Narration";
        private static final String COLOUMN_COMBINED_amount = "combined_amount";
        private static final String COLOUMN_COMBINED_PAYMENT_METHOD = "combined_Payment_method";
        private static final String COLOUMN_COMBINED_RATE = "combined_rate";
        private static final String COLOUMN_COMBINED_ID = "combined_id";
        private static final String COLOUMN_COMBINED_type = "combined_payment_type";
        private static final String CREATE_TABLE_COMBINED = "CREATE TABLE " +
                COMBINED_TABLE_NAME + "("
                + COLOUMN_COMBINED_ID + " INTEGER ,"
                + COLOUMN_COMBINED_type + " INTEGER ,"
                + COLOUMN_COMBINED_DATE + " DATE,"
                + COLOUMN_COMBINED_PAYMENT_METHOD + " VARCHAR(20),"
                + COLOUMN_COMBINED_amount + " REAL,"
                + COLOUMN_COMBINED_RATE + " REAL DEFAULT 0,"
                + COLOUMN_COMBINED_NARRATION + " TEXT,"
                + COLOUMN_COMBINED_PARTYNAME + " VARCHAR(256),"
                + "FOREIGN KEY (" + COLOUMN_COMBINED_PARTYNAME + ")" + "REFERENCES " + PARTY_INFO_TABLE_NAME + "(" + COLOUMN_PARTYNAME + ") ON UPDATE CASCADE ON DELETE CASCADE);";


        //------------------------------------------------------------------------------------------------------------------------------
























        //------------------------------------------------------------------------------------------------------------------------------

        private static final String DROP_TABLE_PARTY_INFO = "DROP TABLE IF EXISTS " + PARTY_INFO_TABLE_NAME;
        private static final String DROP_TABLE_PAYMENT_IN = "DROP TABLE IF EXISTS " + PaymentIn_TABLE_NAME;
        private static final String DROP_TABLE_PaymentOUT = "DROP TABLE IF EXISTS " + PaymentOut_TABLE_NAME;
        private static final String DROP_TABLE_COMBINED = "DROP TABLE IF EXISTS " + COMBINED_TABLE_NAME;
        Context context;


        public BandakAppDatabase(Context context) {
            super(context, DATABASE_NAME, null, DATABSE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_PARTYINFO);
            db.execSQL(CREATE_TABLE_PAYMENT_IN);
            db.execSQL(CREATE_TABLE_PaymentOUT);
            db.execSQL(CREATE_TABLE_COMBINED);
            //Toast.makeText(context, "table created", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL(DROP_TABLE_PARTY_INFO);
            db.execSQL(DROP_TABLE_PAYMENT_IN);
            db.execSQL(DROP_TABLE_PaymentOUT);
            db.execSQL(DROP_TABLE_COMBINED);
           // Toast.makeText(context, "table DROPPED", Toast.LENGTH_LONG).show();
            onCreate(db);

        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

}