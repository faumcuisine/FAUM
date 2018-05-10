package com.faum.faum_rider;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.faum.faum_expert.R;
import com.faum.faum_expert.Transaction_Confirmation;
import com.faum.faum_expert.notifyToRider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notifyToUser extends AppCompatActivity {

    Button send, deliver;
    EditText e2;
    TextView e1;
    DatabaseReference TransactionConfiramtion = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
    FirebaseUser rider = FirebaseAuth.getInstance().getCurrentUser();
    String rid = rider.getUid();
    String  cookID,riderID,deliveredcookID,userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        send = (Button) findViewById(R.id.button);
        e1 = (TextView) findViewById(R.id.textView);
        e2 = (EditText) findViewById(R.id.editText2);
        deliver = (Button)findViewById(R.id.btndeliver2);
        e1.setText("No Delivery");
        e2.setVisibility(View.INVISIBLE);
        deliver.setVisibility(View.INVISIBLE);
        send.setVisibility(View.INVISIBLE);

        e2.setText("I will reach at !!");

        deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleting();
            }
        });
        gecookidfordeleting(new notifyToUser.MyCallBack(){
            @Override
            public void onCallback(String cookIdd,String riderIDd){

                deilverd(cookIdd,riderIDd);
            }
        });





        TransactionConfiramtion.child(rider.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getUserID();
                    String cookid = cookID;
                    DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
                    DatabaseReference chekCookInfo = CookerPersonalInformation.getParent().child("User Contact Information").child(cookid);

                    if(cookid != null) {
                        chekCookInfo.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //String name = dataSnapshot.child("landline").getValue().toString();
                                String num = dataSnapshot.child("cell").getValue().toString();
                                //String address = dataSnapshot.child("raddress").getValue().toString();
                                //tvCookName.setText("Chef Name"+ name);

                                e1.setText(num);
                                e2.setVisibility(View.VISIBLE);
                                deliver.setVisibility(View.VISIBLE);
                                send.setVisibility(View.VISIBLE);

                                //tvCookAddress.setText("Address "+ address);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        e1.setText("No Delivery");
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = e1.getText().toString();
                String sms = e2.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, sms, null, null);
                    Toast.makeText(notifyToUser.this, "sent!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(notifyToUser.this, "Failed!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        if (ContextCompat.checkSelfPermission(notifyToUser.this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(notifyToUser.this, android.Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(notifyToUser.this,
                        new String[]{android.Manifest.permission.SEND_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(notifyToUser.this,
                        new String[]{android.Manifest.permission.SEND_SMS}, 1);
            }
        } else {
            //do nothing
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(notifyToUser.this, android.Manifest.permission.SEND_SMS)
                            != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permisiion Granted", Toast.LENGTH_SHORT).show();
                    }
                } else
                {
                    Toast.makeText(this, "No Permisiion Granted", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }



    private void gecookidfordeleting ( final notifyToUser.MyCallBack mycallback){
        TransactionConfiramtion.child(rider.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dSnapshot : dataSnapshot.getChildren()) {
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getCookID();
                    String cookid = cookID;
                    riderID =dSnapshot.getValue(Transaction_Confirmation.class).getRiderID();
                    String riderid = riderID;
                    userID = dSnapshot.getValue(Transaction_Confirmation.class).getUserID();
                    String userid = userID;

                    // DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
                    //   DatabaseReference chekCookInfo = CookerPersonalInformation.getParent().child("Expert Contact Information").child(cookid);
                    mycallback.onCallback(cookid,riderid);


                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private interface MyCallBack {
        void onCallback(String cookID,String riderID);
    }
    public void deilverd(String cookID,String riderIDd){
        // DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider").child(riderIDd).child(riderIDd).child("cookID");
        //CookerPersonalInformation.removeValue();
        //e1.setText("");
    }
    public void deleting(){


        TransactionConfiramtion.child(rider.getUid()).removeValue();
        e1.setText("");
    }




}
