package com.example.myapplication.Bandak;

public class show_All_party_Data {
    public float balance;
    public String name;
    public  String address;
    public String contact;

    public show_All_party_Data(String name,String address, String contact ,float balance) {
        this.name = name;
this.address=address;
        this.contact = contact;
        this.balance=balance;
    }
    public int  compareto(Object o)
    {
        show_All_party_Data adapterData= (show_All_party_Data) o;
        if(adapterData.name.equals(this.name) && adapterData.contact==(this.contact)) {
            return 0;
        }
        return  1;
    }
}
