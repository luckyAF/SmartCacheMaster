package com.luckyaf.smart.smartcachemaster.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.luckyaf.smart.smartcache.SmartCache;
import com.luckyaf.smart.smartcache.model.Response;
import com.luckyaf.smart.smartcachemaster.base.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.functions.Consumer;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/16
 */
public class SaveJsonObjectActivity extends BaseActivity {

    public static final String CACHE_KEY_JSONOBJECT = "cache_key_jsonobject";

    @Override
    public void initData() {
        dateType = "String";
        addSubscribe(SmartCache.<JSONObject>get(CACHE_KEY_JSONOBJECT).subscribe(new Consumer<Response<JSONObject>>() {
            @Override
            public void accept(Response<JSONObject> response) throws Exception {
                if(response.isUpdate()){
                    txtNow.setText(response.getData().toString());
                }else{
                    txtInit.setText(response.getData().toString());
                }
            }
        }));

    }
    @Override
    public void initPage(){
        dateType = "jsonObject";
    }
    @Override
    public void setButton1() {
        button1.setText("设置成：类型 ，最新更新页面");
    }

    @Override
    public void setButton2() {
        button2.setText("设置 为 null");
    }

    @Override
    public void setButton3() {
        button3.setText("设置 为 int值");
    }

    @Override
    public void clickButton1() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "JsonObject");
            jsonObject.put("page", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SmartCache.put(CACHE_KEY_JSONOBJECT,jsonObject);
    }

    @Override
    public void clickButton2() {
        SmartCache.put(CACHE_KEY_JSONOBJECT,null);
    }

    @Override
    public void clickButton3() {
        SmartCache.put(CACHE_KEY_JSONOBJECT,2018);

    }

    @Override
    public void jump() {
        Intent intent = new Intent(mContext,SaveJsonObjectActivity.class);
        Bundle params = new Bundle();
        params.putInt(INTENT_PAGE_INDEX,index + 1);
        intent.putExtras(params);
        startActivity(intent);
    }
}
