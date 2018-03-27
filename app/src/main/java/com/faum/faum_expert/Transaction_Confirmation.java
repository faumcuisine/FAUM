package com.faum.faum_expert;

/**
 * Created by arsal on 07-Mar-18.
 */

public class Transaction_Confirmation {

    String userID,cookID,riderID;

    public void Transaction_Confirmation(){

    }
    public void Transaction_Confirmation(String userID,String cookID,String riderID){
        this.cookID = cookID;
        this.riderID = riderID;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getCookID() {
        return cookID;
    }

    public String getRiderID() {
        return riderID;
    }
}
