package com.luckyaf.smart.smartcachemaster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.luckyaf.smart.smartcachemaster.sample.OneKeyActivity;
import com.luckyaf.smart.smartcachemaster.sample.SaveBitmapActivity;
import com.luckyaf.smart.smartcachemaster.sample.SaveJsonArrayActivity;
import com.luckyaf.smart.smartcachemaster.sample.SaveJsonObjectActivity;
import com.luckyaf.smart.smartcachemaster.sample.SaveStringActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void oneKey(View v){
        startActivity(new Intent().setClass(this, OneKeyActivity.class));
    }

    public void string(View v) {
        startActivity(new Intent().setClass(this, SaveStringActivity.class));
    }

    public void jsonobject(View v) {
        startActivity(new Intent().setClass(this, SaveJsonObjectActivity.class));
    }

    public void jsonarray(View v) {
        startActivity(new Intent().setClass(this, SaveJsonArrayActivity.class));
    }

    public void bitmap(View v) {
        startActivity(new Intent().setClass(this, SaveBitmapActivity.class));
    }

    public void media(View v) {
        //startActivity(new Intent().setClass(this, SaveMediaActivity.class));
    }

    public void drawable(View v) {
        //startActivity(new Intent().setClass(this, SaveDrawableActivity.class));
    }

    public void object(View v) {
        //startActivity(new Intent().setClass(this, SaveObjectActivity.class));
    }

    public void about(View v) {
       // startActivity(new Intent().setClass(this, AboutActivity.class));
    }

}
