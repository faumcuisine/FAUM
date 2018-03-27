package com.faum.faum_user;

/**
 * Created by arsal on 02-Nov-17.
 */

public class NewDeal_Database {

    private String uId;

    private String  dealId,newDealCategory , dishName , dealDescription ;
    //private int dealName;
    private String dealName;



    private String estimateTime ,newDealPrice ,newDealSize ;

    private Boolean eightToten ,twelveTotwo ,sixToeight, nineToeleven;


    private Boolean monday,tuesday,wednesday,thursday , friday ;

    private Boolean checkBoxConfirmation;


    public NewDeal_Database(){

    }

    public NewDeal_Database(String uid,String dealId,String DealName,String NewDealCategory, String DishName, String DealDescription){
        this.uId = uid;
        this.dealId = dealId;
        this.dealName = DealName;
        this.newDealCategory = NewDealCategory;
        this.dishName = DishName;
        this.dealDescription = DealDescription;
    }

    public String getuId() {
        return uId;
    }

    public String getDealId() {
        return dealId;
    }

    public String getDealName() {
        return dealName;
    }

    public String getNewDealCategory() {
        return newDealCategory;
    }

    public String getDishName() {
        return dishName;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public NewDeal_Database(String estimateTime, String newDealPrice, String newDealSize){
        this.estimateTime = estimateTime;
        this.newDealPrice = newDealPrice;
        this.newDealSize = newDealSize;
    }

    public String getEstimateTime() {
        return estimateTime;
    }

    public String getNewDealPrice() {
        return newDealPrice;
    }

    public String getNewDealSize() {
        return newDealSize;
    }

    public  NewDeal_Database(Boolean eightToten, Boolean twelveTotwo, Boolean sixToeight, Boolean nineToeleven){
        this.eightToten = eightToten;
        this.twelveTotwo = twelveTotwo;
        this.sixToeight  = sixToeight;
        this.nineToeleven = nineToeleven;
    }

    public Boolean getEightToten() {
        return eightToten;
    }

    public Boolean getTwelveTotwo() {
        return twelveTotwo;
    }

    public Boolean getSixToeight() {
        return sixToeight;
    }

    public Boolean getNineToeleven() {
        return nineToeleven;
    }

    public NewDeal_Database(Boolean Monday, Boolean Tuesday, Boolean Wednesday, Boolean Thursday, Boolean Friday){
        this.monday = Monday;
        this.tuesday = Tuesday;
        this.wednesday = Wednesday;
        this.thursday = Thursday;
        this.friday = Friday;

    }

    public Boolean getMonday() {
        return monday;
    }

    public Boolean getTuesday() {
        return tuesday;
    }

    public Boolean getWednesday() {
        return wednesday;
    }

    public Boolean getThursday() {
        return thursday;
    }

    public Boolean getFriday() {
        return friday;
    }


    public NewDeal_Database(Boolean checkBoxConfirmation,String dealId){
        this.dealId = dealId;
        this.checkBoxConfirmation = checkBoxConfirmation;
    }

    public Boolean getCheckBoxConfirmation() {
        return checkBoxConfirmation;
    }


}


