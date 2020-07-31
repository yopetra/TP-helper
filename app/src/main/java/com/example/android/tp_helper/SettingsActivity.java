package com.example.android.tp_helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.android.tp_helper.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding mBinding;
    private SharedPreferences sPref;
    private int textSpeed;
    private int textSize;
    private int textColor;
    private final int UP = 1;
    private final int DOWN = 2;
    private final int BLACK = 11;
    private final int WHITE = 12;
    private final int YELLOW = 13;
    private final int BLUE = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sPref = getSharedPreferences(getString(R.string.settings_pref), MODE_PRIVATE);
        textSpeed = sPref.getInt(getString(R.string.speed), 5);
        textSize = sPref.getInt(getString(R.string.size), 5);
        textColor = sPref.getInt(getString(R.string.textColor), BLACK);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        mBinding.textViewSpeed.setText(String.valueOf(textSpeed));
        mBinding.textViewSize.setText(String.valueOf(textSize));
        setTextColor(textColor);

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.minus_speed :
                        changeSpeed(DOWN);
                        break;
                    case R.id.plus_speed :
                        changeSpeed(UP);
                        break;
                    case R.id.minus_size:
                        changeSize(DOWN);
                        break;
                    case R.id.plus_size:
                        changeSize(UP);
                        break;
                    case R.id.tc_first:
                        textColor = BLACK;
                        setTextColor(textColor);
                        break;
                    case R.id.tc_second:
                        textColor = WHITE;
                        setTextColor(textColor);
                        break;
                    case R.id.tc_third:
                        textColor = YELLOW;
                        setTextColor(textColor);
                        break;
                    case R.id.tc_forth:
                        textColor = BLUE;
                        setTextColor(textColor);
                        break;
                }
            }
        };

        mBinding.minusSpeed.setOnClickListener(onClickListener);
        mBinding.plusSpeed.setOnClickListener(onClickListener);
        mBinding.minusSize.setOnClickListener(onClickListener);
        mBinding.plusSize.setOnClickListener(onClickListener);
        mBinding.tcFirst.setOnClickListener(onClickListener);
        mBinding.tcSecond.setOnClickListener(onClickListener);
        mBinding.tcThird.setOnClickListener(onClickListener);
        mBinding.tcForth.setOnClickListener(onClickListener);
    }

    private void setTextColor(int textColor) {
        mBinding.tcFirst.setText("");
        mBinding.tcSecond.setText("");
        mBinding.tcThird.setText("");
        mBinding.tcForth.setText("");
         switch(textColor){
             case BLACK:
                 mBinding.tcFirst.setText("V");
                 break;
             case WHITE:
                 mBinding.tcSecond.setText("V");
                 break;
             case YELLOW:
                 mBinding.tcThird.setText("V");
                 break;
             case BLUE:
                 mBinding.tcForth.setText("V");
                 break;
         }
    }

    private void changeSize(int control) {
        if(control == UP){
            textSize++;
            if(textSize > 10){textSize = 10;}
        }

        if(control == DOWN){
            textSize--;
            if(textSize < 1){textSize = 1;}
        }

        mBinding.textViewSize.setText(String.valueOf(textSize));
    }

    private void changeSpeed(int control) {
        if(control == UP){
            textSpeed++;
            if(textSpeed > 10){
                textSpeed = 10;}
        }

        if(control == DOWN){
            textSpeed--;
            if(textSpeed < 1){
                textSpeed = 1;}
        }

        mBinding.textViewSpeed.setText(String.valueOf(textSpeed));
    }


    @Override
    protected void onPause() {
        super.onPause();
        sPref = getSharedPreferences(getString(R.string.settings_pref), MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putInt(getString(R.string.speed), textSpeed);
        editor.putInt(getString(R.string.size), textSize);
        editor.putInt(getString(R.string.textColor), textColor);
        editor.commit();
    }
}