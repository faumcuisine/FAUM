package com.faum.faum_user;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.faum.faum_expert.R;
import com.faum.faum_expert.notify;
import com.firebase.geofire.GeoFire;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import static com.faum.faum_user.Main2Activity.uid;
import static com.faum.faum_user.Deal_Invoice.orderID;

/**
 * Created by M.UZAIR on 4/24/2018.
 */

public class notification extends AppCompatActivity {
    Button send, disable;
    EditText e1,e2;
    SharedPreferences mPrefrences;
    DatabaseReference userOrderRefrence = FirebaseDatabase.getInstance().getReference("User Confirmed Order");



    DatabaseReference expertContactRefrence = FirebaseDatabase.getInstance().getReference("Expert Basic Information");

    DatabaseReference expertOrderConfirmedRefrence = FirebaseDatabase.getInstance().getReference("Expert Confirmed Order");
    DatabaseReference userOrderConfirmedRefrence = FirebaseDatabase.getInstance().getReference("User Confirmed Order");

    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        send = (Button)findViewById(R.id.button);
        t1 = (TextView)findViewById(R.id.textView);
        e2 = (EditText)findViewById(R.id.editText2);
        disable = (Button) findViewById(R.id.btndeliver2);
        disable.setVisibility(View.INVISIBLE);

        mPrefrences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor mEditor =  mPrefrences.edit();
        /*userOrderRefrence.child(User.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
        String cookerID = mPrefrences.getString(getString(R.string.COOKER_ID)," ");
        //String orderID = mPrefrences.getString(getString(R.string.DEAL_ORDER_ID)," ");
        //e2.setText(orderID);
        //DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Confirmed Order");
        DatabaseReference chekCookInfo = FirebaseDatabase.getInstance().getReference("Expert Contact Information").child(cookerID);
        chekCookInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("landline").getValue().toString();
                String num = dataSnapshot.child("cell").getValue().toString();

                t1.setText(num);
                //e2.setText(num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = t1.getText().toString();
                String sms = e2.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, sms, null, null);
                    Toast.makeText(notification.this,"Message Send!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(notification.this,"Message Failed!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        String userID = uid;
       // String orders = orderID;
        DatabaseReference chekuserInfo = FirebaseDatabase.getInstance().getReference("User Confirmed Order").child(userID).child(orderID);
        chekuserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("dealName").getValue().toString();
                String price = dataSnapshot.child("orderPrice").getValue().toString();
                String qty = dataSnapshot.child("orderQty").getValue().toString();

                e2.setText("Deal Name: "+name+ " Deal Price:"+price+" Deal Qty: "+qty);
                //e2.setText(num);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if(ContextCompat.checkSelfPermission(notification.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(notification.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(notification.this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);
            } else
            {
                ActivityCompat.requestPermissions(notification.this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        } else
        {
            //do nothing
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(notification.this, Manifest.permission.SEND_SMS)
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
}


