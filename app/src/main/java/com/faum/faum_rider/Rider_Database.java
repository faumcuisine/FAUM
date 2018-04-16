package com.faum.faum_rider;

/**
 * Created by arsal on 06-Mar-18.
 */

public class Rider_Database {
    Boolean riderisAcvtive;
    String riderId;

    public void Rider_Database(){

    }

    public String getRiderId() {
        return riderId;
    }

    public void Rider_Databse(Boolean isactive, String riderID){
        this.riderisAcvtive = isactive;
        this.riderId = riderID;
    }

    public Boolean getRiderisAcvtive() {
        return riderisAcvtive;
    }

}
