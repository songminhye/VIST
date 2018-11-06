package com.jhl.ativ.vist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LastActivity extends AppCompatActivity {

    private PopupWindow mPopupWindow ;
    String loginId;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);


        ImageButton button1 = (ImageButton) findViewById(R.id.imageButton3);
        Button button = (Button)findViewById(R.id.textbutton);
        ImageButton button3 = (ImageButton) findViewById(R.id.exit);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popupView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정

                mPopupWindow.setFocusable(true);
                // 외부 영역 선택히 PopUp 종료

                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


                Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });

                Button ok = (Button) popupView.findViewById(R.id.Ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:01094958211"));
                        startActivity(intent);
                    }
                });

            }
        });

        // 상담원 버튼을 누를때 상담원 전화연결하는 popup창을 띄운다.


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 View popupView = getLayoutInflater().inflate(R.layout.activity_popup, null);
                mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정

                mPopupWindow.setFocusable(true);
                // 외부 영역 선택히 PopUp 종료

                mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);


                Button cancel = (Button) popupView.findViewById(R.id.Cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });

                Button ok = (Button) popupView.findViewById(R.id.Ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:01064443527"));
                        startActivity(intent);
                    }
                });
            }
        });
       //위에와 같은 역할을 한다.


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences auto = getSharedPreferences("auto",MODE_PRIVATE);
                loginId = auto.getString("inputId",null);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();
                i++;

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                final Query appliesQuery = ref.child("rfid").orderByChild("ID").equalTo(loginId);

                appliesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot Snapshot: dataSnapshot.getChildren()){
                            Snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    //    Log.e(TAG,"onCancelled",databaseError.toException());
                    }
                });

                if(i==1) {

                    Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                } else
                {
                    Toast.makeText(getApplicationContext(), "이미 로그아웃했습니다.", Toast.LENGTH_LONG).show();

                }

            }


        });
        //loginid안에 data를 삭제하고 nfc에 태깅할때 생기는 짐정보(날짜,아이디,번호)를 firebase에서 지운다.


    }



    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent(LastActivity.this,MainActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent1);

        super.onBackPressed();

        //뒤로가기 누르면 main으로 간다. 로그아웃을 하지 않고 뒤로가기를 누르면 계속 자동 로그인이된다.
    }

}
