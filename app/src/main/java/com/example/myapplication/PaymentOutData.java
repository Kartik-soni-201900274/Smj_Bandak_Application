package com.example.myapplication;

public class PaymentOutData {
    public String date;
    public String name;
    public float amount;
    public float rate;
    public int id;

    public PaymentOutData( String name, float amount,String date, float rate,int id) {
        this.date = date;
        this.name = name;
        this.amount=amount;
        this.rate=rate;
        this.id=id;

    }
    public int  compareto(Object o)
    {
        PaymentOutData adapterData= (PaymentOutData) o;
        if(adapterData.name.equals(this.name) && adapterData.amount==(this.amount) && adapterData.rate==(this.rate) && adapterData.date.equals(this.date )) {
            return 0;
        }
        return  1;
    }
}
