package com.example.asus.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private Button b3utton;
    private Button b2utton;
    private Button b1utton;
    private TextView scoreShow;
    int score=0;

    private Button b3uttonb;
    private Button b2uttonb;
    private Button b1uttonb;
    private TextView scoreShowb;
    int scoreb=0;

    private Button buttonSet;
    private static final String TAG = "Main2Activity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_INDEX2 = "index2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Log.d(TAG,"onCreate called");
        if(savedInstanceState!=null){
            score=savedInstanceState.getInt(KEY_INDEX,score);
            scoreb=savedInstanceState.getInt(KEY_INDEX2,scoreb);
        }

        b3utton=(Button)findViewById(R.id.b3utton);
        b2utton=(Button)findViewById(R.id.b2utton);
        b1utton=(Button)findViewById(R.id.b1utton);
        scoreShow=(TextView) findViewById(R.id.textView4);

        b3uttonb=(Button)findViewById(R.id.b3uttonb);
        b2uttonb=(Button)findViewById(R.id.b2uttonb);
        b1uttonb=(Button)findViewById(R.id.b1uttonb);
        scoreShowb=(TextView) findViewById(R.id.textView4b);

        buttonSet=(Button) findViewById(R.id.buttonSet);

        scoreShow.setText(""+score);
        scoreShowb.setText(""+scoreb);

        b3utton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    score=score+3;
                    scoreShow.setText(""+score);
            }
        });
        b3uttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreb=scoreb+3;
                scoreShowb.setText(""+scoreb);
            }
        });
        b2utton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = score + 2;
                scoreShow.setText("" + score);
            }
        });
        b2uttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    scoreb=scoreb+2;
                    scoreShowb.setText(""+scoreb);
            }
        });
        b1utton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score = score + 1;
                scoreShow.setText("" + score);
            }
        });
        b1uttonb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scoreb = scoreb + 1;
                scoreShowb.setText("" + scoreb);
            }
        });
        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score=0;
                scoreShow.setText(""+score);
                scoreb=0;
                scoreShowb.setText(""+scoreb);
                Toast.makeText(Main2Activity.this,R.string.reset_toast,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart called");
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause called");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop called");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy called");
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG,"savedInstanceState");
        savedInstanceState.putInt(KEY_INDEX,score);
        savedInstanceState.putInt(KEY_INDEX2,scoreb);
    }
}
