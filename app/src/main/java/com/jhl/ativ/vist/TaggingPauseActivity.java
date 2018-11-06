package com.jhl.ativ.vist;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class TaggingPauseActivity extends AppCompatActivity {




    TaggingActivity tag;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging_pause);
        getWindow().setWindowAnimations(0);

        ImageButton button=findViewById(R.id.recycle);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(TaggingPauseActivity.this, TaggingActivity.class);
                startActivity(intent1);
                finish();

                //버튼을 눌렀을때 다시 taggingactivty로 이동하게 한다.
                //태깅카운트가 멈췄을때 nfc가 인식하지 못하게 하기위해 만든 액티비티이다.
            }
        });





    }


}
