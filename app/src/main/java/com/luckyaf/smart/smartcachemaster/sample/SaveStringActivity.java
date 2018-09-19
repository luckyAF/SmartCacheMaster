package com.luckyaf.smart.smartcachemaster.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.luckyaf.smart.smartcache.SmartCache;
import com.luckyaf.smart.smartcache.model.Response;
import com.luckyaf.smart.smartcachemaster.base.BaseActivity;

import io.reactivex.functions.Consumer;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/16
 */
public class SaveStringActivity extends BaseActivity{

    public static final String CACHE_KEY_STRING = "cache_key_string";

    @Override
    public void initData() {
        dateType = "String";
        addSubscribe(SmartCache.observe(CACHE_KEY_STRING,String.class).subscribe(new Consumer<Response<String>>() {
            @Override
            public void accept(Response<String> stringResponse) throws Exception {
                Log.d("stringResponse",stringResponse.toString());
                if(stringResponse.isUpdate()){
                    txtNow.setText(stringResponse.getData());
                }else{
                    txtInit.setText(stringResponse.getData());
                }
            }
        }));

    }
    @Override
    public void initPage(){
        dateType = "string";
    }

    @Override
    public void setButton1() {
        button1.setText("设置String 变成当前更新页码");
    }

    @Override
    public void setButton2() {
        button2.setText("设置String 为 null");
    }

    @Override
    public void setButton3() {
        button3.setText("设置string 为 int值");
    }

    @Override
    public void clickButton1() {
        SmartCache.put(CACHE_KEY_STRING,"更新页" + index);
    }

    @Override
    public void clickButton2() {
        SmartCache.put(CACHE_KEY_STRING,null);
    }

    @Override
    public void clickButton3() {
        SmartCache.put(CACHE_KEY_STRING,2018);

    }

    @Override
    public void jump() {
        Intent intent = new Intent(mContext,SaveStringActivity.class);
        Bundle params = new Bundle();
        params.putInt(INTENT_PAGE_INDEX,index + 1);
        intent.putExtras(params);
        startActivity(intent);
    }
}
