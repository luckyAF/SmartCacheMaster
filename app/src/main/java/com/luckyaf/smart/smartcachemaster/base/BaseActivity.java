package com.luckyaf.smart.smartcachemaster.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.luckyaf.smart.smartcachemaster.R;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/14
 */
public abstract  class BaseActivity extends AppCompatActivity {

    public static final String INTENT_PAGE_INDEX = "intent_page_index";

    public String dateType;
    public int index;


    public TextView txtDataType;
    public Button button1;
    public Button button2;
    public Button button3;
    public TextView txtInit;
    public TextView txtNow;
    public Button btnJump;


    private CompositeDisposable compositeDisposable;

    public Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;
        initPage();
        Bundle bundle = getIntent().getExtras();
        initParams(bundle);
        initView();
        initData();
    }

    public void initPage(){

    }

    /**
     * 初始化参数
     *
     * @param bundle bundle
     */
    public  void initParams(Bundle bundle){
        index = bundle == null ? 0 : bundle.getInt(INTENT_PAGE_INDEX,0);
    }


    /**
     * 初始化view
     */
    public  void initView(){
        txtDataType = findViewById(R.id.txt_data_type);
        button1 = findViewById(R.id.btn_1);
        button2 = findViewById(R.id.btn_2);
        button3 = findViewById(R.id.btn_3);
        txtInit = findViewById(R.id.txt_init);
        txtNow = findViewById(R.id.txt_now);
        btnJump = findViewById(R.id.btn_jump);

        txtDataType.setText("数据类型" + dateType + " 页面 " + index);
        button1.setOnClickListener(v -> clickButton1());
        button2.setOnClickListener(v -> clickButton2());
        button3.setOnClickListener(v -> clickButton3());
        btnJump.setOnClickListener(v -> jump());

        setButton1();
        setButton2();
        setButton3();

    }

    public abstract void initData();


    public abstract void setButton1();
    public abstract void setButton2();
    public abstract void setButton3();

    public abstract void clickButton1();
    public abstract void clickButton2();
    public  abstract void clickButton3();

    public abstract void jump();

//
//    public void jump(Class<?> clazz){
//        Intent intent = new Intent(this,clazz);
//        Bundle params = new Bundle();
//        params.putString(INTENT_DATA_TYPE,dateTyoe);
//        params.putInt(INTENT_PAGE_INDEX,index + 1);
//        startActivity(intent,params);
//
//    }


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
