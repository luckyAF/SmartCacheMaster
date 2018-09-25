package com.luckyaf.smart.smartcachemaster.sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.luckyaf.smart.smartcache.SmartCache;
import com.luckyaf.smart.smartcache.model.Response;
import com.luckyaf.smart.smartcachemaster.R;
import com.luckyaf.smart.smartcachemaster.bean.UserBean;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class OneKeyActivity extends AppCompatActivity {

    private static String ONE_KEY = "one_key";

    TextView txtBoolean;
    TextView txtInt;
    TextView txtDouble;
    TextView txtString;
    TextView txtObject;

    private CompositeDisposable compositeDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_key);

        txtBoolean = findViewById(R.id.txt_boolean);
        txtInt = findViewById(R.id.txt_int);
        txtDouble = findViewById(R.id.txt_double);
        txtString = findViewById(R.id.txt_string);
        txtObject = findViewById(R.id.txt_object);

        findViewById(R.id.btn_boolean).setOnClickListener(v -> SmartCache.put(ONE_KEY,false));
        findViewById(R.id.btn_int).setOnClickListener(v -> SmartCache.put(ONE_KEY,2018));
        findViewById(R.id.btn_double).setOnClickListener(v -> SmartCache.put(ONE_KEY,3.14));
        findViewById(R.id.btn_string).setOnClickListener(v -> SmartCache.put(ONE_KEY,"string"));
        findViewById(R.id.btn_object).setOnClickListener(v -> SmartCache.put(ONE_KEY,new UserBean("xiang",26)));


        addSubscribe(
                SmartCache
                        .observe(ONE_KEY,Boolean.class)
                        .subscribe(response -> txtBoolean.setText("boolean = " + response.getData())));
        addSubscribe(
                SmartCache
                        .observe(ONE_KEY,Integer.class)
                        .subscribe(response -> txtInt.setText("Integer = " + response.getData())));
        addSubscribe(
                SmartCache
                        .observe(ONE_KEY,Double.class)
                        .subscribe(response -> txtDouble.setText("Double = " + response.getData())));
        addSubscribe(
                SmartCache
                        .observe(ONE_KEY,String.class)
                        .subscribe(response -> txtString.setText("String = " + response.getData())));
        addSubscribe(
                SmartCache
                        .observe(ONE_KEY,UserBean.class)
                        .subscribe(response -> txtObject.setText("UserBean = " + response.getData())));

        }


    public void addSubscribe(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        super.onDestroy();
    }

}
