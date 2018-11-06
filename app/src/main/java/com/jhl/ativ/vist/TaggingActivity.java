package com.jhl.ativ.vist;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TaggingActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private static String tagNum = null;
    String loginId;
    private TextView tagDesc;
    String key, Check_string, Check_key;
    //
    private TextView time_counter;
    private CountDownTimer countDownTimer;
    private static final int MILLISINFUTURE = 21 * 1000;
    private static final int COUNT_DOWN_INTERVAL = 1000;


    int count = 20;
    int i = 0;


    private DatabaseReference UserDatabase;
    private FirebaseDatabase UserFirebase;
    private Query query;


    Vibrator vibe;
    long[] pattern = {0,700,450,700,450};




    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging);
        getWindow().setWindowAnimations(0);

        tagDesc = (TextView) findViewById(R.id.RFID);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        SharedPreferences pref = getSharedPreferences("volume",MODE_MULTI_PROCESS);
        i = pref.getInt("volume",0);


        UserFirebase = FirebaseDatabase.getInstance();
        UserDatabase = UserFirebase.getReference("User");



        time_counter = findViewById(R.id.count);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(pattern, 0);

        countDownTimer();
        countDownTimer.start();



    }



    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onPause() {
        if (nfcAdapter != null || count == 0) {
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();

    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }

    }

    private void restoreState() {
        int i;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (tag != null) {
            byte[] tagId = tag.getId();
            tagDesc.setText("TagID: " + toHexString(tagId));
            tagNum = toHexString(tagId);

            //NFC가 읽혔을때 TAG아이디를  받아온다.

        }


        key = tagNum;

        // TAG아이드를 key에 저장

        query = UserDatabase.orderByKey().equalTo(key);
        query.addListenerForSingleValueEvent(valueEventListener);

        //firebase의 데이터베이스에서 "User" 에서 key의 넘버와 같은 key를 가진 데이터가 있는지 확인한다.


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild(key)) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    //Check_string = dataSnapshot.child(key).getValue(String.class);
                    Check_key = dataSnapshot.child(key).getKey();

                    //찾은 data를 check_key에 저장


                    if (key.equals(Check_key)) {
                        FirebaseDatabase.getInstance().getReference().child("User").child(key).setValue(key);

                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();

                        SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", key);
                        autoLogin.commit();

                        // check_key가 nfc의 tag아이디와 같으면 자동 로그인 inputid에 정보를 넣어준다.



                        mHandler.postDelayed(mMyTask, 1000);
                        // data를 가져오기 성공했으면 datacheck 엑티비티로 이동

                    }
                }

            } else {

                Toast.makeText(getApplicationContext(), "존재하지 않는 TAG입니다.", Toast.LENGTH_LONG).show();
                mHandler.postDelayed(returnTask, 1000);

                //data를 가져오기를 실패했으면 taggingpauseactivity로 이동
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public static final String CHARS = "0123456789ABCDEF";

    public static String toHexString(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) {
            sb.append(CHARS.charAt((data[i] >> 4) & 0x0F)).append(CHARS.charAt(data[i] & 0x0F));
        }
        return sb.toString();

        //tag값을 16진수로 가져오기
    }


    private Handler mHandler = new Handler();

    //성공시
    private Runnable mMyTask = new Runnable() {
        @Override
        public void run() {

            Intent intent1 = new Intent(TaggingActivity.this, DataCheck.class);
            intent1.putExtra("key", tagNum);
            startActivity(intent1);
            vibe.cancel();
            countDownTimer.cancel();
            finish();

        }

    };
    // 실패시
    private Runnable returnTask = new Runnable() {
        @Override
        public void run() {

            Intent intent2 = new Intent(TaggingActivity.this, TaggingPauseActivity.class);
            startActivity(intent2);
            vibe.cancel();
            countDownTimer.cancel();
            finish();
        }
    };


    public void deleteCount(){

        SharedPreferences pref = getSharedPreferences("volume",MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor1;
        editor1 = pref.edit();
        editor1.clear();
        editor1.commit();



    }

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                time_counter.setText(String.valueOf(count));
                count--;
                // nfcAdapter.enableForegroundDispatch(this.pendingIntent,null,null);
            }

            @Override
            public void onFinish() {
                vibe.cancel();
                i++;
                count=20;


                deleteCount();

                SharedPreferences pref = getSharedPreferences("volume",MODE_MULTI_PROCESS);
                SharedPreferences.Editor editor1;
                editor1 = pref.edit();
                editor1.putInt("volume",i);
                editor1.commit();



                if(i==3) {
                    deleteCount();
                    Intent intent4 = new Intent(getApplicationContext(), UserTaggingActivity.class);
                    startActivity(intent4);
                    finish();
                }
                else {

                    Intent intent3 = new Intent(getApplicationContext(), TaggingPauseActivity.class);
                    startActivity(intent3);

                }
                finish();

                //nfc 직접 태그할때 카운트를 설정하고, 3번 실패하면 직접입력 액티비티로 이동




            }


        };


        }



    @Override
    public void onBackPressed() {
        vibe.cancel();
        countDownTimer.cancel();
        Intent intent5 = new Intent(TaggingActivity.this,MainActivity.class);
        intent5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent5);

        super.onBackPressed();

        //뒤로가기 눌렀을때 진동과 카운트를 종료하고 mainactivity로 이동
    }


}
