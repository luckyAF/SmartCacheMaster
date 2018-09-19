package com.luckyaf.smart.smartcachemaster.sample;

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
 * @author Created by luckyAF on 2018/9/17
 */
public class SaveJsonArrayActivity  extends BaseActivity {

    public static final String CACHE_KEY_JSONOARRAY = "cache_key_jsonoarray";

    JSONArray jsonArray = new JSONArray();

    @Override
    public void initData() {
        dateType = "String";
        addSubscribe(SmartCache.observe(CACHE_KEY_JSONOARRAY,JSONArray.class).subscribe(new Consumer<Response<JSONArray>>() {
            @Override
            public void accept(Response<JSONArray> response) throws Exception {
                if(response.isUpdate()){
                    jsonArray = response.getData();
                    if(null != jsonArray){
                        txtNow.setText(response.getData().toString());
                    }
                }else{
                    txtInit.setText(response.getData() == null ? "null" :response.getData().toString());
                }
            }
        }));

    }
    @Override
    public void initPage(){
        dateType = "jsonArray";
    }

    @Override
    public void setButton1() {
        button1.setText("添加 当前类型，页面 列表");
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
        if(jsonArray == null){
            jsonArray = new JSONArray();
        }
        JSONObject now = new JSONObject();
        try {
            now.put("type", "JSONOARRAY");
            now.put("page", index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(now);
        //SmartCache.put(CACHE_KEY_JSONOARRAY,jsonArray);
    }

    @Override
    public void clickButton2() {
        SmartCache.put(CACHE_KEY_JSONOARRAY,null);
    }

    @Override
    public void clickButton3() {
        SmartCache.put(CACHE_KEY_JSONOARRAY,2018);

    }

    @Override
    public void jump() {
        Intent intent = new Intent(mContext,SaveJsonArrayActivity.class);
        Bundle params = new Bundle();
        params.putInt(INTENT_PAGE_INDEX,index + 1);
        intent.putExtras(params);
        startActivity(intent);
    }
}
