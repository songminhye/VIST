package com.jhl.ativ.vist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class flyActivity extends AppCompatActivity {

    Query query;
    public String testID1;
    public String testID2;
    public String testID3;
    Intent intent,intent1;
    String key,loginId;
    ImageView img;
    Query query2;
    public static flyActivity activity = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly);
        activity = this;

        intent = getIntent();
        key = intent.getExtras().getString("key");

        query2 = FirebaseDatabase.getInstance().getReference().child("rfid").orderByChild("ID").equalTo(key);
        query2.addValueEventListener(valueEventListener2);//<-쿼리 쓸때

        img=(ImageView)findViewById(R.id.flyicon2);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        img.setAnimation(animation);

    }

    ValueEventListener valueEventListener2 = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild(key)) {
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()) {

                        testID1 = dataSnapshot.child(key).child("ID").getValue(String.class);
                        testID2 = dataSnapshot.child(key).child("NUMBER").getValue(String.class);
                        testID3 = dataSnapshot.child(key).child("TIME").getValue(String.class);

                    }


                    intent1 = new Intent(flyActivity.this, LuggageArrivedActivity.class);
                    intent1.putExtra("testID1", testID1);
                    intent1.putExtra("testID2", testID2);
                    intent1.putExtra("testID3", testID3);

                    startActivity(intent1);
                    finish();

                    //datacheck에서의 기능을 똑같이 수행한다.



        }

        else {
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                loginId = auto.getString("inputId",null);
                if(loginId==null){
                    finish();
                }
                //loginid가 null이 되면 flyactivity를 종료한다. (data값 삭제할때를 위해)
            }



        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(),"잠시후 다시 실행해 주세요",Toast.LENGTH_LONG).show();
        mHandler.postDelayed(mMyTask, 1000);

        //뒤로 버튼을 누를때 toast를 띄우고 앱을 종료한다.

        super.onBackPressed();
    }


    private Handler mHandler = new Handler();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Runnable mMyTask = new Runnable() {
        @Override public void run() {

            finishAffinity();
            System.runFinalization();
            System.exit(0);

        }

    };




}
