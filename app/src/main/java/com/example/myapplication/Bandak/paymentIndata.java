package com.example.myapplication.Bandak;

public class paymentIndata {
    public String date;
    public String name;
    public float amount;
    public int id;

    public paymentIndata(String date, String name, float amount, int id) {
        this.date = date;
        this.name = name;
        this.amount=amount;
        this.id=id;
    }
    public int  compareto(Object o)
    {
        paymentIndata adapterData= (paymentIndata) o;
        if(adapterData.name.equals(this.name) && adapterData.amount==(this.amount)  && adapterData.date.equals(this.date )&& adapterData.id==(this.id)) {
            return 0;
        }
        return  1;
    }
}
