package com.example.asus.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class Rate2Activity extends AppCompatActivity {

    EditText dollar;
    EditText euro;
    EditText won;

    float drate;
    float erate;
    float wrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate2);

        dollar=findViewById(R.id.dollarEdit);
        euro=findViewById(R.id.euroEdit);
        won=findViewById(R.id.wonEdit);

        Intent intent = getIntent();
        drate=intent.getFloatExtra("dollar_rate",0.0f);
        erate=intent.getFloatExtra("euro_rate",0.0f);
        wrate=intent.getFloatExtra("won_rate",0.0f);

        dollar.setText(""+drate);
        euro.setText(""+erate);
        won.setText(""+wrate);

    }

    public void save(View btn){
        float newDollar=Float.parseFloat(dollar.getText().toString());
        float newEuro=Float.parseFloat(euro.getText().toString());
        float newWon=Float.parseFloat(won.getText().toString());

        Intent intent2=getIntent();
        Bundle bdl=new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent2.putExtras(bdl);
        setResult(2,intent2);

        finish();
    }
}
