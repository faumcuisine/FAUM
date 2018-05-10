package com.faum.faum_expert;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class notify extends AppCompatActivity {
    EditText e2;
    TextView e1;
    Button send, disable;
    DatabaseReference TransactionConfiramtion = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Expert");
    FirebaseUser expert = FirebaseAuth.getInstance().getCurrentUser();
    String eid = expert.getUid();
    String  cookID,riderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        send = (Button)findViewById(R.id.button);
        e1 = (TextView)findViewById(R.id.textView);
        e2 = (EditText)findViewById(R.id.editText2);
        disable = (Button) findViewById(R.id.btndeliver2);
        disable.setEnabled(false);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = e1.getText().toString();
                String sms = e2.getText().toString();
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, sms, null, null);
                    Toast.makeText(notify.this,"sent!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(notify.this,"Failed!",Toast.LENGTH_SHORT).show();
                }


            }
        });
        if(ContextCompat.checkSelfPermission(notify.this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(notify.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(notify.this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);
            } else
            {
                ActivityCompat.requestPermissions(notify.this,
                        new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        } else
        {
            //do nothing
        }

        TransactionConfiramtion.child(expert.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getCookID();
                    String cookid = cookID;
                    DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Expert");
                    DatabaseReference chekCookInfo = CookerPersonalInformation.getParent().child("Expert Contact Information").child(cookid);


                    chekCookInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("landline").getValue().toString();
                            String num = dataSnapshot.child("cell").getValue().toString();
                            String address = dataSnapshot.child("raddress").getValue().toString();

                            e2.setText(name+ num+ address);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        TransactionConfiramtion.child(expert.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    riderID = dSnapshot.getValue(Transaction_Confirmation.class).getRiderID();
                    String riderid = riderID;
                    DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Expert");
                    DatabaseReference chekCookInfo = CookerPersonalInformation.getParent().child("Rider Contact Information").child(riderid);


                    chekCookInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //String name = dataSnapshot.child("landline").getValue().toString();
                            String num = dataSnapshot.child("cell").getValue().toString();
                            //String address = dataSnapshot.child("raddress").getValue().toString();

                            e1.setText( num);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

    });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(notify.this, Manifest.permission.SEND_SMS)
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

