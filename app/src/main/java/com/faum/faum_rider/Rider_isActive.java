package com.faum.faum_rider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.faum.faum_expert.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Rider_isActive extends AppCompatActivity {

    TextView tvRiderActiveConfirmation;
    CheckBox cbRiderActiveConfirmation;
    DatabaseReference riderActiveRefrence = FirebaseDatabase.getInstance().getReference("Rider Active Confirmation");
    FirebaseUser riderUser = FirebaseAuth.getInstance().getCurrentUser();
    Boolean check =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_is_active);
        tvRiderActiveConfirmation = (TextView)findViewById(R.id.tvRiderActiveConfirmation);
        cbRiderActiveConfirmation = (CheckBox)findViewById(R.id.cbRiderActiveConfirmation);


        cbRiderActiveConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddRiderConfirmation();
            }
        });

        riderActiveRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dSnapshot : dataSnapshot.getChildren()){
                    try {
                        check = dSnapshot.child(riderUser.getUid()).getValue(Rider_Database.class).getRiderisAcvtive();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    finally {
                        if(check==true){
                            cbRiderActiveConfirmation.setChecked(check);
                        }else{
                            cbRiderActiveConfirmation.setChecked(check);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void AddRiderConfirmation(){
        Boolean checkConfirmation;
        Rider_Database rider_database =  new Rider_Database();

        if(cbRiderActiveConfirmation.isChecked()==true){
            checkConfirmation = true;
            rider_database.Rider_Databse(checkConfirmation,riderUser.getUid());
        }else{
            checkConfirmation = false;
            rider_database.Rider_Databse(checkConfirmation,riderUser.getUid());
        }

        riderActiveRefrence.child(riderUser.getUid()).setValue(rider_database);
    }
}
