package com.example.asus.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity{
    private TextView mFtem;
    private EditText mCtem;
    private Button mTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_layout);

        mFtem=(TextView)findViewById(R.id.textView2);
        mCtem=(EditText)findViewById(R.id.editText);
        mTrans=(Button)findViewById(R.id.button);
        mTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str=mCtem.getText().toString();
                float ctem=Float.parseFloat(str);
                float ftem= (float) (ctem*33.8);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String fftem = decimalFormat.format(ftem);
                mFtem.setText("华氏温度为："+fftem);
            }
        });
    }

}
