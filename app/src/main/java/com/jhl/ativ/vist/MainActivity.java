package com.jhl.ativ.vist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String loginId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        loginId = auto.getString("inputId",null);



        if(loginId!=null){
            Toast.makeText(MainActivity.this,"로그인이 유지됩니다.", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(MainActivity.this,DataCheck.class);
            intent3.putExtra("key",loginId);
            startActivity (intent3);
            finish();
        }

        // loginId 로 자동로그인 데이터를 저장한다.

        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),TaggingActivity.class);

                startActivity(intent);

            }
        });

        // nfc 직접 인식 액티비티로 이동

        ImageButton button2 = (ImageButton) findViewById(R.id.imageButton2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),UserTaggingActivity.class);
                startActivity(intent);

            }
        });

        // nfc 코드를 직접 입력하는 액티비티로 이동
    }


    public void onBackPressed() {

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("정말로 종료하시겠습니까?");
        alert_ex.setTitle("LPF");
        alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert_ex.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });

        AlertDialog alert = alert_ex.create();
        alert.show();

       // super.onBackPressed();
    }

    // 뒤로가기 눌렀을때 앱 종료를 확인하는 팝업창 생성
}
