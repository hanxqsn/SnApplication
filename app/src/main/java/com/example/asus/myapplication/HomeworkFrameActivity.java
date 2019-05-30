package com.example.asus.myapplication;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class HomeworkFrameActivity extends FragmentActivity {

    private RadioButton rbFooting,rbSprint,rbMarathon;
    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    String TAG="HomeworkFrameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_frame);
        mFragments = new Fragment[3];
        fragmentManager = getSupportFragmentManager();
        mFragments[0]=fragmentManager.findFragmentById(R.id.footing_fragment);
        mFragments[1]=fragmentManager.findFragmentById(R.id.sprint_fragment);
        mFragments[2]=fragmentManager.findFragmentById(R.id.marathon_fragment);

        fragmentTransaction = fragmentManager.beginTransaction().
                hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
        fragmentTransaction.show(mFragments[0]).commit();

        rbFooting=findViewById(R.id.footingButton);
        rbSprint=findViewById(R.id.sprintButton);
        rbMarathon=findViewById(R.id.marathonButton);
        rbFooting.setBackgroundResource(R.drawable.shape3);

        radioGroup=findViewById(R.id.RunBottomGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i(TAG, "onCheckedChanged: radioGroup,checkedId=" + checkedId);
                fragmentTransaction = fragmentManager.beginTransaction().
                        hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]);
                rbFooting.setBackgroundResource(R.drawable.shape2);
                rbSprint.setBackgroundResource(R.drawable.shape2);
                rbMarathon.setBackgroundResource(R.drawable.shape2);

                switch (checkedId){
                    case R.id.footingButton:
                        fragmentTransaction.show(mFragments[0]).commit();
                        rbFooting.setBackgroundResource(R.drawable.shape3);
                        break;
                    case R.id.sprintButton:
                        fragmentTransaction.show(mFragments[1]).commit();
                        rbSprint.setBackgroundResource(R.drawable.shape3);
//                        Log.i(TAG, "onCheckedChanged: click 功能!");
                        break;
                    case R.id.marathonButton:
                        fragmentTransaction.show(mFragments[2]).commit();
                        rbMarathon.setBackgroundResource(R.drawable.shape3);
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
