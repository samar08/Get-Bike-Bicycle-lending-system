package com.example.samar.getbike;

public class requests_disp{
    String customer_name;
    String bikeid,ownerid;
    public requests_disp(String cust_name,String bikeid,String ownerid){
        this.customer_name=cust_name;
this.bikeid=bikeid;
this.ownerid=ownerid;
        }

    public String getOwnerid() {
        return ownerid;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getBikeid() {
        return bikeid;
    }
}
