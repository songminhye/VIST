package com.jhl.ativ.vist;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserTaggingActivity extends AppCompatActivity {

    String key,Check_string,Check_key;
    private DatabaseReference UserDatabase ;
    private FirebaseDatabase UserFirebase;
    private Query query;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tagging);

        editText = (EditText) findViewById(R.id.editText);
        ImageButton button = (ImageButton) findViewById(R.id.saveButton);




        UserFirebase = FirebaseDatabase.getInstance();
        UserDatabase= UserFirebase.getReference("User");


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                if (editText.getText().toString().length() == 0 ) {

                    Toast toast = Toast.makeText(getApplicationContext(), "TAG 번호를 입력하세요", Toast.LENGTH_LONG);
                    toast.show();

                    //입력창에 아무것도 입력하지 않으면 토스트를 띄운다.

                } else {

                    key = editText.getText().toString();
                    query = UserDatabase.orderByKey().equalTo(key);
                    query.addListenerForSingleValueEvent(valueEventListener);

                    //입력창에서 받은 번호를 key에 저장한다.
                    //그 key를 이용하여 firebase의 database에서 값을 찾는다.

                }
              ;
            }


        });



    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.hasChild(key)) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Check_key = dataSnapshot.child(key).getKey();


                    if (key.equals(Check_key)) {
                        FirebaseDatabase.getInstance().getReference().child("User").child(key).setValue(key);

                        Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                        SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoLogin = auto.edit();
                        autoLogin.putString("inputId", key);
                        autoLogin.commit();

                        //User에서 정보가 key값과 같은것이 있는지 확인하는 작업
                        //값이 찾아지면 로그인 되었다는 토스트를 띄운다.
                        //loginId에 역시 값을 넣어준다.

                        query.removeEventListener(valueEventListener);



                        mHandler.postDelayed(mMyTask, 1000);


                    }
                }

            } else {

                Toast.makeText(getApplicationContext(), "TAG를 다시 확인하세요", Toast.LENGTH_LONG).show();
                mHandler.postDelayed(returnTask, 1000);
                //받아 온 값이 database에 없으면 다시 값을 받아오게 한다.


            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private Handler mHandler = new Handler();

    //성공시
    private Runnable mMyTask = new Runnable() {
        @Override public void run() {

            Intent intent1 = new Intent(UserTaggingActivity.this, DataCheck.class);
            intent1.putExtra("key", key);
            startActivity(intent1);
            finish();
            // datacheck로 이동

        }

    };
    // 실패시
    private Runnable returnTask = new Runnable() {
        @Override
        public void run() {

            Intent intent2 = new Intent(UserTaggingActivity.this,UserTaggingActivity.class);
            startActivity(intent2);
            finish();
            //usertaggingactivty로 다시 이동
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(UserTaggingActivity.this,MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);

        super.onBackPressed();

        //뒤로가기 눌렀을때 mainactivty로 가게한다.
    }




}
