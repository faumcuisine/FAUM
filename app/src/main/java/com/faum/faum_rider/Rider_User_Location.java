package com.faum.faum_rider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.nfc.Tag;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.faum.faum_expert.R;
import com.faum.faum_expert.Transaction_Confirmation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.Locale.getDefault;

public class Rider_User_Location extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    Geocoder geocoder;
    List<Address> addresses;
    DatabaseReference TransactionConfiramtion = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
    DatabaseReference TransactionConfiramtionforRider = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
    FirebaseUser riderUser = FirebaseAuth.getInstance().getCurrentUser();
    String  cookID,riderID,cooklong,cooklat,riderlat,riderlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_and_user_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etDestination = (EditText) findViewById(R.id.etDestination);


        /*Double lat1 = 25.698907;
        Double lat2 = 66.487723;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat1,lat2,1);
            String address = addresses.get(0).getAddressLine(0);
            String area = addresses.get(0).getLocality();
            String fulladdress = area;
            etOrigin.setText(fulladdress);
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        readData(new MyCallBack() {
            @Override
            public void onCallback(String cooklat,String cooklong) {
                //Log.d("TAG", value);


                sendRequestforconverting(cooklat,cooklong);
            }

        });

        readData2(new MyCallBack2() {
            @Override
            public void onCallback(String riderlat,String riderlong) {
                //Log.d("TAG", value);


                sendRequestforconverting2(riderlat,riderlong);

            }

        });

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }



    public void readData2(final MyCallBack2 myCallBack){

        TransactionConfiramtionforRider.child(riderUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getCookID();
                    riderID = dSnapshot.getValue(Transaction_Confirmation.class).getUserID();
                    String cookid = cookID;
                    String riderid = riderID;

                    DatabaseReference CookerAndRiderLocationInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
                    //  DatabaseReference chekCookInfo = CookerAndRiderLocationInformation.getParent().child("Expert Location Information").child(cookid);
                    DatabaseReference chekRiderInfo = CookerAndRiderLocationInformation.getParent().child("User Location Information").child(riderid);

if(riderid != null && cookid != null) {
    chekRiderInfo.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            riderlat = dataSnapshot.child("l").child("0").getValue().toString();
            riderlong = dataSnapshot.child("l").child("1").getValue().toString();


            myCallBack.onCallback(riderlat, riderlong);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
else {
    etOrigin.setText("");
    etDestination.setText("");
}


                }

            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void readData(final MyCallBack myCallBack){

        TransactionConfiramtion.child(riderUser.getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getCookID();
                    riderID = dSnapshot.getValue(Transaction_Confirmation.class).getRiderID();
                    String cookid = cookID;
                    String riderid = riderID;

                    DatabaseReference CookerAndRiderLocationInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
                    DatabaseReference chekCookInfo = CookerAndRiderLocationInformation.getParent().child("Expert Location Information").child(cookid);
                    DatabaseReference chekRiderInfo = CookerAndRiderLocationInformation.getParent().child("Rider Location Information").child(riderid);

                   if (riderid != null && cookid != null) {
                       chekCookInfo.addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               cooklat = dataSnapshot.child("l").child("0").getValue().toString();
                               cooklong = dataSnapshot.child("l").child("1").getValue().toString();


                               myCallBack.onCallback(cooklat, cooklong);
                           }

                           @Override
                           public void onCancelled(DatabaseError databaseError) {

                           }
                       });
                   }
                   else {
                       etOrigin.setText("");
                       etDestination.setText("");
                   }

                }

            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void sendRequestforconverting2(String riderlat,String riderlong) {
        Double latitude2 =  Double.valueOf(riderlat).doubleValue();
        Double longitude2 =  Double.valueOf(riderlong).doubleValue();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude2,longitude2,1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            //addresses = geocoder.getFromLocation(latitude2,longitude2,1);
            //String address = addresses.get(0).getAddressLine(0);
            //String area = addresses.get(0).getLocality();
            //String area = addresses.get(0).getSubLocality();
            //String area2 = addresses.get(0).getAdminArea();
            //String area3 = addresses.get(0).getSubAdminArea();
            //String area4 = addresses.get(0).getCountryName();
            //String fulladdress = area;
            etOrigin.setText(add.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequestforconverting(String cooklat,String cooklong){
        Double latitude1 =  Double.valueOf(cooklat).doubleValue();
        Double longitude1 =  Double.valueOf(cooklong).doubleValue();
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude1,longitude1,1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
        //    addresses = geocoder.getFromLocation(latitude1,longitude1,1);
          //  String address = addresses.get(0).getAddressLine(0);
            //String area = addresses.get(0).getLocality();
            //String area = addresses.get(0).getSubLocality();
            //String area2 = addresses.get(0).getAdminArea();
            //String area3 = addresses.get(0).getSubAdminArea();
            //String area4 = addresses.get(0).getCountryName();
            //String fulladdress = area;
            etDestination.setText(add.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private interface MyCallBack {
        void onCallback(String cooklat,String cooklong);
    }
    private  interface MyCallBack2 {
        void onCallback(String riderlat,String riderlong);
    }


    private void sendRequest() {
        // LatLng org = new LatLng(25.107616, 66.757017);

        //LatLng dest = new LatLng(25.698907, 66.487723);


        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //LatLng hcmus = new LatLng(10.762963, 106.682394);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        //originMarkers.add(mMap.addMarker(new MarkerOptions()
        //      .title("Đại học Khoa học tự nhiên")
        //    .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()

                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()

                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}
