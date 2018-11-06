package com.jhl.ativ.vist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LuggageArrivedActivity extends AppCompatActivity {

    private int MILLISINFUTURE;
    private static final int COUNT_DOWN_INTERVAL=1000;
    private int count;
    private TextView countTxt;
    private CountDownTimer countDownTimer;
    TextView textView4;
    Intent intent1,intent2;
    String testID1, testID2, testID3;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luggage_arrived);
        textView4 = (TextView)findViewById(R.id.textView6);
        countTxt = (TextView) findViewById(R.id.textView2);

        intent1 = getIntent();


        testID1 = intent1.getExtras().getString("testID1");
        testID2 = intent1.getExtras().getString("testID2");
        testID3 = intent1.getExtras().getString("testID3");

        img=(ImageView)findViewById(R.id.bag);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        img.setAnimation(animation);

        try {
            Time_Check(testID1,testID2,testID3);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }




    public void Time_Check(String a, String b, String c) throws ParseException {

        textView4.setText(c);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = dateformat.format(Calendar.getInstance().getTime());

        String time = compareTime(datetime, c);
        //(리더기찍힌시간+20)-현재시간=남은시간

        if(time.equals("")){

            intent2 = new Intent(LuggageArrivedActivity.this,LastActivity.class);
            startActivity(intent2);
            finish();

        } else {

            countDownTimer();
            countDownTimer.start();
        }

    }

    private String compareTime(String currentTime, String readTime) throws ParseException {

        String strDate = readTime;
        String strDate1 = currentTime;
        SimpleDateFormat what = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date comparedate = what.parse(strDate);
        java.util.Date currentdate = what.parse(strDate1);

        Calendar currentCalendar = Calendar.getInstance();
        Calendar createdCalendar = Calendar.getInstance();

        createdCalendar.setTime(comparedate);
        currentCalendar.setTime(currentdate);
        long diffTimeMillis = createdCalendar.getTimeInMillis()+1200000 - currentCalendar.getTimeInMillis();
        long result = Math.abs(diffTimeMillis);

        MILLISINFUTURE =(int)result;
        count = MILLISINFUTURE/1000;

        String printStr = "";

        if (diffTimeMillis / (24 * 60 * 60 * 1000) > 0)

            printStr = diffTimeMillis / (24 * 60 * 60 * 1000) + " " + "일 전";

        else if (diffTimeMillis / (60 * 60 * 1000) > 0)

            printStr = diffTimeMillis / (60 * 60 * 1000) + " " + "시간 전";

        else if (diffTimeMillis / (60 * 1000) > 0)
            printStr =diffTimeMillis / (60 * 1000) + " " + "분 " + (result % (60 * 1000)) / 1000 + "초 남았습니다.";

        else if (diffTimeMillis / 1000 > 0)

            printStr = diffTimeMillis / 1000 + " " + "초 남았습니다.";
        else if (diffTimeMillis <= 0)
            printStr = "";

        return printStr;

    }


    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                //int a=(MILLISINFUTURE/1000)-(count/2);
                countTxt.setText(String.valueOf(count /60)+" 분 "+String.valueOf(count%60)+" 초 ");
                count=count-1;

            }
            public void onFinish() {
                countTxt.setText(String.valueOf("Finish ."));
                intent2 = new Intent(LuggageArrivedActivity.this,LastActivity.class);
                startActivity(intent2);
                finish();

            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            countDownTimer.cancel();
        } catch (Exception e) {
        }
        countDownTimer = null;
    }

    public void onBackPressed() {

        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("나갔다 들어오시겠습니까?");
        alert_ex.setTitle("수화물 이동중");
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
}





