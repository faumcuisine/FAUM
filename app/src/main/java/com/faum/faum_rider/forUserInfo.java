package com.faum.faum_rider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.faum.faum_expert.R;
import com.faum.faum_expert.Transaction_Confirmation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class forUserInfo extends AppCompatActivity {

    TextView tvCookName,tvCookNum,tvCookAddress ;
    Button GotoCookerMap;
    DatabaseReference TransactionConfiramtion = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
    FirebaseUser riderUser = FirebaseAuth.getInstance().getCurrentUser();
    String rid = riderUser.getUid();
    String  cookID,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chef_info);
        tvCookName = (TextView)findViewById(R.id.tv1);
        tvCookNum = (TextView)findViewById(R.id.tv2);
        tvCookAddress = (TextView)findViewById(R.id.tv3);
        GotoCookerMap = (Button)findViewById(R.id.gotoCookerMap);

        tvCookName.setText("No Delivery");

        tvCookNum.setVisibility(View.INVISIBLE);
        tvCookAddress.setVisibility(View.INVISIBLE);
        GotoCookerMap.setVisibility(View.INVISIBLE);

        GotoCookerMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMapActivity();
            }
        });
        TransactionConfiramtion.child(riderUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    cookID = dSnapshot.getValue(Transaction_Confirmation.class).getUserID();
                    String cookid = cookID;
                    DatabaseReference CookerPersonalInformation = FirebaseDatabase.getInstance().getReference("Transaction Confirmation for Rider");
                    DatabaseReference chekCookInfo = CookerPersonalInformation.getParent().child("User Contact Information").child(cookid);


                    chekCookInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.child("landline").getValue().toString();
                            String num = dataSnapshot.child("cell").getValue().toString();
                            String address = dataSnapshot.child("raddress").getValue().toString();
                            tvCookNum.setVisibility(View.VISIBLE);
                            tvCookAddress.setVisibility(View.VISIBLE);
                            GotoCookerMap.setVisibility(View.VISIBLE);
                            tvCookName.setText("Chef Name"+ name);
                            tvCookNum.setText("Number "+ num);
                            tvCookAddress.setText("Address "+ address);
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

    private void gotoMapActivity(){
        Intent intent = new Intent(forUserInfo.this,Rider_User_Location.class);
        startActivity(intent);
    }
}
