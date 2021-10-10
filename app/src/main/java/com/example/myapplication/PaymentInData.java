package com.example.myapplication;

import android.util.Log;

public class PaymentInData {
    public String date;
    public String name;
    public float amount;
    public int id;

    public PaymentInData(String date, String name, float amount, int id) {
        this.date = date;
        this.name = name;
        this.amount=amount;
        this.id=id;

    }
    public int  compareto(Object o)
    {
        PaymentInData adapterData= (PaymentInData) o;

        if(adapterData.name.equals(this.name) && adapterData.amount==(this.amount)  && adapterData.date.equals(this.date )) {
            return 0;
        }
        return  1;
    }
}
