package com.faum.faum_rider;

/**
 * Created by M.UZAIR on 3/29/2018.
 */

public class info_At_Rider {
    String riderID,cookID,userID;
    public info_At_Rider(){

    }
    public  info_At_Rider(String cookID,String riderID,String userID){
        this.cookID =cookID;
        this.riderID=riderID;
        this.userID =userID;
    }
    public String getCookInfo(){ return  cookID; }
}
