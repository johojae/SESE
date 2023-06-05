package com.sese.showmethebeer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BeerStoreActivity extends AppCompatActivity {

    // textView 참조를 저장하기 위한 변수.
    TextView textView1 ;
    TextView textView2 ;
    TextView textView3 ;
    // TextView textViews[3] ;  // 개선된 코드 (textView 참조를 저장하기 위한 변수.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // ... 코드 계속

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_store);

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(0);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(1);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView(2);
            }
        });

        textView1 = (TextView) findViewById(R.id.text1);
        textView2 = (TextView) findViewById(R.id.text2);
        textView3 = (TextView) findViewById(R.id.text3);
    }


    private void changeView(int index) {

        FrameLayout frame = (FrameLayout) findViewById(R.id.frame) ;

        // 0 번째 뷰 제거. (뷰가 하나이므로, 0 번째 뷰를 제거하면 모든 뷰가 제거됨.)
        frame.removeViewAt(0) ;

        // index에 해당하는 textView 표시
        switch (index) {
            case 0 :
                frame.addView(textView1) ;
                break ;
            case 1 :
                frame.addView(textView2) ;
                break ;
            case 2 :
                frame.addView(textView3) ;
                break ;
        }

        /* // 개선된 코드 (index에 해당하는 textView 표시.)
            if (index < 3) {
                frame.addView(textViews[index]) ;
            } else {
                // TODO : index error.
            }
        */

        // textView1을 제외한 모든 뷰 제거.
        // frame.removeView(textView1) ;
        frame.removeView(textView2) ;
        frame.removeView(textView3) ;
        /* // 개선된 코드 (textView1을 제외한 모든 뷰 제거.)
        frame.removeAllViews() ;
        frame.addView(textView1) ;
        */
    }
}
