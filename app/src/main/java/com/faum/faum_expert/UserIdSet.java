package com.faum.faum_expert;

/**
 * Created by arsal on 10-Nov-17.
 */

public class UserIdSet {

    private String email,id,phonenumber;

    public UserIdSet(){}

    public  UserIdSet(String email,String uid){
        this.email = email;
        this.id = uid;
    }
    public UserIdSet(String uid){
        this.id=uid;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
