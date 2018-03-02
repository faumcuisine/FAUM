package com.faum.faum_expert;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class RidersIDS extends AppCompatActivity {

    DatabaseReference riderPRIMARYKEY = FirebaseDatabase.getInstance().getReference("Rider Primary Key");
    DatabaseReference riderLocation = FirebaseDatabase.getInstance().getReference("Rider Location Information");

    FirebaseUser cookerRefrence = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference cookerLocationRefrence = FirebaseDatabase.getInstance().getReference("Expert Location Information");

    GeoFire geoFireCooker= new GeoFire(cookerLocationRefrence);

    GeoFire geoFire = new GeoFire(riderLocation);

    public  static  List<Location> rLocations;
    public static List<String> rIDs;
    //int riderCounter =0;

    Location cookerLOCATION;
    Location riderLOCATION;

    public static Location colsestRiderLocation;
    public  static float smallestDistance = -1;
    int indexValue = 0;
    public String closestRiderID;


    TextView riderID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riders_ids2);
        riderID =(TextView)findViewById(R.id.tvRiderID);

        rLocations = new ArrayList<>();
        rIDs =  new ArrayList<>();


        findingValues();



    }

    public void findingValues(){
        try{
            findingRiderID(new passignriderID() {
                @Override
                public void onCallback(String riderID) {
                    //Log.d("Rider key", riderID);
                    rIDs.add(riderID);
                    Log.d("Rider IDs",rIDs.toString());
                    passingRiderID(riderID, new listData() {
                        @Override
                        public void onCallBack(List<Location> list) {
                            Log.d("Rider Locations",list.toString());
                            findingNearestRider();
                        }
                    });
                }
            });
            findingCookerLocation();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void findingRiderID(final passignriderID myCallBack){
        riderPRIMARYKEY.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot riderSnapshot: dataSnapshot.getChildren()){
                    try{

                        String riderID= riderSnapshot.getKey();
                        myCallBack.onCallback(riderID);
                    }catch (Exception e){
                        e.printStackTrace();
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
                rLocations.add(riderLOCATION);
                //Log.d("Rider location", riderLOCATION.toString());

                myCallBack.onCallBack(rLocations);

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
                        comparingValues();
                        Log.d("Closet Rider Location",colsestRiderLocation.toString());
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public interface passignriderID{
        void onCallback(String riderID);
    }
    private interface listData{
        void onCallBack(List<Location> list);
    }
    public void comparingValues(){
        try{
            indexValue = rLocations.indexOf(colsestRiderLocation);
            Log.d("Index value Location",String.valueOf(indexValue));
            closestRiderID = rIDs.get(indexValue);
            Log.d("Index value IDS",closestRiderID);
            riderID.setText(closestRiderID);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
