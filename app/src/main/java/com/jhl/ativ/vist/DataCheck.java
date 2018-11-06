
package com.jhl.ativ.vist;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;



public class DataCheck extends AppCompatActivity {



    private static final String TAG = "DataCheck";
    String key;


    Query query;
    public String testID1;
    public String testID2;
    public String testID3;
    Intent intent,intent1,intent2,intent3;
    Query query2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        key = intent.getExtras().getString("key");
        //key값을 taggingactivy와 usertaggingactivty에서 받아온다.




        query2 = FirebaseDatabase.getInstance().getReference().child("rfid").orderByChild("ID").equalTo(key);
        query2.addValueEventListener(valueEventListener2);
    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild(key)) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                    testID1 = dataSnapshot.child(key).child("ID").getValue(String.class);
                    testID2 = dataSnapshot.child(key).child("NUMBER").getValue(String.class);
                    testID3 = dataSnapshot.child(key).child("TIME").getValue(String.class);

                    //database에 key값을 가진 짐이 리더기에 지나갈때 생성되는 정보를 하나씩 받아온다.

                }


                    intent1 = new Intent(DataCheck.this, LuggageArrivedActivity.class);
                    intent1.putExtra("testID1", testID1);
                    intent1.putExtra("testID2", testID2);
                    intent1.putExtra("testID3", testID3);

                    //값들을 Luggagearrivedactivity로 전달한다.

                    startActivity(intent1);
                    finish();


            } else {

                intent2 = new Intent(DataCheck.this,flyActivity.class);
                intent2.putExtra("key",key);
                startActivity(intent2);
                finish();

                //data값을 못찾은 경우
                //key값을 flyactivty로 보내고 flyactivity를 실행한다.

            }



        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "onCancelled", databaseError.toException());

        }
    };




}