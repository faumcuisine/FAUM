package com.faum.faum_expert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faum.faum_rider.*;
import com.faum.faum_rider.Contact_Info;
import com.faum.faum_rider.Contact_Infrormation;
import com.faum.faum_user.*;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RidersIDS extends AppCompatActivity {

    DatabaseReference riderPRIMARYKEY = FirebaseDatabase.getInstance().getReference("Rider Primary Key");
    DatabaseReference riderLocation = FirebaseDatabase.getInstance().getReference("Rider Location Information");

    FirebaseUser cookerRefrence = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference cookerLocationRefrence = FirebaseDatabase.getInstance().getReference("Expert Location Information");
    DatabaseReference riderActiveRefrence = FirebaseDatabase.getInstance().getReference("Rider Active Confirmation");

    DatabaseReference riderPersonalInformation = FirebaseDatabase.getInstance().getReference("Rider Personal Information");

    DatabaseReference riderContactInformation = FirebaseDatabase.getInstance().getReference("Rider Contact Information");

    DatabaseReference transactionConfirmationRider = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");

    DatabaseReference transactionConfirmationUser = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for User");

    DatabaseReference transactionConfirmationExpert = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Expert");
    DatabaseReference Riderbackup = FirebaseDatabase.getInstance().getReference("Rider Backup");
    String riderFName,riderLName,riderCell;

    GeoFire geoFireCooker= new GeoFire(cookerLocationRefrence);

    GeoFire geoFire = new GeoFire(riderLocation);

    public  static  List<Location> rLocations;
    public static List<String> rIDs;
    public static List<String> activeRiderIDs;
    public String riderid;
    //int riderCounter =0;

    Location cookerLOCATION;
    Location riderLOCATION;

    public static Location colsestRiderLocation;
    public  static float smallestDistance = -1;
    int indexValue = 0;
    public String closestRiderID;

    public String userID;


    TextView riderID,tvRiderFNAME,tvRiderLNAME,tvRiderCell;
    Button btnRiderLocationLocation,btnRiderContact;

    Boolean check = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_ids2);
        riderID =(TextView)findViewById(R.id.tvRiderID);

        tvRiderFNAME = (TextView)findViewById(R.id.tvRiderFNAME);
        tvRiderLNAME = (TextView)findViewById(R.id.tvRiderLNAME);
        tvRiderCell = (TextView)findViewById(R.id.tvRiderCell);
        btnRiderContact = (Button)findViewById(R.id.btnRiderContact);
        riderID.setVisibility(View.INVISIBLE);

        rLocations = new ArrayList<>();
        rIDs =  new ArrayList<>();
        activeRiderIDs = new ArrayList<>();



        SharedPreferences mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);

        userID =  mPrefrences.getString(getString(R.string.DEAL_USER_ID)," ");

        findingValues();


        btnRiderContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RidersIDS.this,Navigation_Drawer.class);
                startActivity(intent);
            }
        });


    }

    public void findingValues(){
        try{
            findingActiverRiderID();
            findingRiderID(new passignriderID() {
                @Override
                public void onCallback(String riderID) {
                    //Log.d("Rider key", riderID);
                    rIDs.add(riderID);
                    Log.d("Rider IDs",rIDs.toString());
                    passingRiderID(riderID, new listData() {
                        @Override
                        public void onCallBack(Location list) {
                            rLocations.add(list);
                            Log.d("Rider Locations",rLocations.toString());
                            if(rIDs.size()==rLocations.size()){
                                findingNearestRider();
                            }
                            /*for(int i =1;i<=rIDs.size();i++){
                                if(i==(rIDs.size())){
                                    findingNearestRider();
                                }
                            }*/
                        }
                    });
                    /*passingRiderID(riderID, new listData() {
                        @Override
                        public void onCallBack(List<Location> list) {
                            Log.d("Rider Locations",list.toString());
                            findingNearestRider();
                        }
                    });*/
                }
            });
            findingCookerLocation();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void findingActiverRiderID(){
        riderActiveRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot datas  : dataSnapshot.getChildren())
                {
                    try {
                        check  = datas.getValue(Rider_Database.class).getRiderisAcvtive();
                    } catch (Exception e) {
                        e.printStackTrace();

                    } finally {

                        if(check==true){
                            riderid =   datas.getValue(Rider_Database.class).getRiderId();
                            activeRiderIDs.addAll(Arrays.asList(riderid));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void findingRiderID(final passignriderID myCallBack){
        riderPRIMARYKEY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot riderSnapshot: dataSnapshot.getChildren()){
                    String riderID= riderSnapshot.getKey();
                    if(activeRiderIDs.contains(riderID)) {
                        myCallBack.onCallback(riderID);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void passingRiderID(String riderID, final listData myCallBack){

        geoFire.getLocation(riderID, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation riderID) {


                riderLOCATION = new Location("");
                riderLOCATION.setLatitude(riderID.latitude);
                riderLOCATION.setLongitude(riderID.longitude);
                //rLocations.add(riderLOCATION);
                //Log.d("Rider location", riderLOCATION.toString());

                myCallBack.onCallBack(riderLOCATION);

                //riderCounter++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void findingCookerLocation(){
        geoFireCooker.getLocation(cookerRefrence.getUid(), new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation cookerID) {

                cookerLOCATION = new Location("");
                cookerLOCATION.setLatitude(cookerID.latitude);
                cookerLOCATION.setLongitude(cookerID.longitude);

                //Log.d("cooker location",cookerLOCATION.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void findingNearestRider(){

        /*if(rLocations.isEmpty()){
            Log.d("Closet Rider  empty",rLocations.toString());
        }else{


            for(Location loc : rLocations){
                Log.d("Closet Rider not empty", String.valueOf(loc));
            }
        }*/

        if(rLocations.isEmpty()){
            Log.d("Closet Rider  empty",rLocations.toString());
        }else{
            try {
                for(Location rlocation : rLocations){
                    float distance=  cookerLOCATION.distanceTo(rlocation);
                    //int distance = Location.distanceBetween(cookLAT,cookLONG,rlocation.getLatitude(),rlocation.getLongitude(),results);
                    if(smallestDistance == -1 || distance < smallestDistance){
                        colsestRiderLocation = rlocation;
                        smallestDistance = distance;
                        //comparingValues();
                        Log.d("Closet Rider Location",colsestRiderLocation.toString());
                    }
                }
                comparingValues();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public interface passignriderID{
        void onCallback(String riderID);
    }
    private interface listData{
        void onCallBack(Location list);

    }
    public void comparingValues(){
        try{
            indexValue = rLocations.indexOf(colsestRiderLocation);
            Log.d("Index value Location",String.valueOf(indexValue));
            closestRiderID = rIDs.get(indexValue);
            Log.d("Index value IDS",closestRiderID);
            findingRiderInformation();
           // riderID.setText(closestRiderID);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void findingRiderInformation(){

        try{
            //riderID.setText(closestRiderID);

            riderContactInformation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        try{
                            riderCell = dataSnapshot1.child(closestRiderID).getValue(com.faum.faum_rider.Contact_Info.class).getRAddress();
                            riderFName = dataSnapshot1.child(closestRiderID).getValue(com.faum.faum_rider.Contact_Info.class).getCell();
                            riderLName = dataSnapshot1.child(closestRiderID).getValue(com.faum.faum_rider.Contact_Info.class).getLandline();
                            tvRiderFNAME.setText(riderFName);
                            tvRiderCell.setText(riderCell);
                            tvRiderLNAME.setText(riderLName);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        transactionSuccessful();

    }

    public void transactionSuccessful(){
        Transaction_Confirmation transaction_confirmation = new Transaction_Confirmation();
        transaction_confirmation.Transaction_Confirmation(userID,cookerRefrence.getUid(),closestRiderID);

        try{

            transactionConfirmationRider.child(closestRiderID).child(closestRiderID).setValue(transaction_confirmation);
            transactionConfirmationExpert.child(cookerRefrence.getUid()).child(cookerRefrence.getUid()).setValue(transaction_confirmation);
            transactionConfirmationUser.child(userID).child(userID).setValue(transaction_confirmation);
            Riderbackup.child(closestRiderID).child(closestRiderID).setValue(transaction_confirmation);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}