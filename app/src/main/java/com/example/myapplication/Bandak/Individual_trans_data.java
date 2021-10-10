package com.example.myapplication.Bandak;

public class Individual_trans_data {
    public String date;
    public float amount;
    public float rate;
    public String desc;
    public int type;
    public int id;

    public Individual_trans_data(String date, float amount, float rate, String desc,int type ,int id) {
       this.id=id;
        this.type=type;
        this.date = date;
        this.amount = amount;
        this.rate = rate;
        this.desc = desc;
    }
    public int  compareto(Object o)
    {
        Individual_trans_data adapterData= (Individual_trans_data) o;
        if(adapterData.date.equals(this.date) && adapterData.amount==(this.amount)  && adapterData.desc.equals(this.desc )&& adapterData.rate==(this.rate) ) {
            return 0;
        }
        return  1;
    }
}
