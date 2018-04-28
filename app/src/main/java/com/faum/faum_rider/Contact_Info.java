package com.faum.faum_rider;

/**
 * Created by arsal on 03-Oct-17.
 */

public class Contact_Info {
    private String landline,cell,raddress;

    public Contact_Info(){

    }

    public Contact_Info(String landline,String cell, String raddress){
        this.cell = cell;
        this.landline = landline;
        this.raddress = raddress;
    }


    public String getLandline() {
        return landline;
    }

    public String getCell() {
        return cell;
    }

    public String getRAddress() {
        return raddress;
    }
}


