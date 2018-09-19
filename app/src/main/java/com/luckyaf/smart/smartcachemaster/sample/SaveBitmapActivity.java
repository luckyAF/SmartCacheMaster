package com.luckyaf.smart.smartcachemaster.sample;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.luckyaf.smart.smartcache.SmartCache;
import com.luckyaf.smart.smartcache.model.Response;
import com.luckyaf.smart.smartcachemaster.R;
import com.luckyaf.smart.smartcachemaster.base.BaseActivity;

import io.reactivex.functions.Consumer;

/**
 * 类描述：
 *
 * @author Created by luckyAF on 2018/9/17
 */
public class SaveBitmapActivity extends BaseActivity {

    public static final String CACHE_KEY_BITMAP = "cache_key_bitmap";
    private ImageView imgInit;
    private ImageView imgNow;

    @Override
    public void initData() {
        imgInit = findViewById(R.id.img_init);
        imgNow = findViewById(R.id.img_now);
        txtInit.setVisibility(View.GONE);
        txtNow.setVisibility(View.GONE);
        imgInit.setVisibility(View.VISIBLE);
        imgNow.setVisibility(View.VISIBLE);

        addSubscribe(SmartCache.observe(CACHE_KEY_BITMAP,Bitmap.class).subscribe(new Consumer<Response<Bitmap>>() {
            @Override
            public void accept(Response<Bitmap> response) throws Exception {
                if (response.isUpdate()) {
                    imgNow.setImageBitmap(response.getData());
                } else {
                    imgInit.setImageBitmap(response.getData());
                }
            }
        }));

    }

    @Override
    public void initPage() {
        dateType = "bitmap";
    }

    @Override
    public void setButton1() {
        button1.setText("设置Bitmap");
    }

    @Override
    public void setButton2() {
        button2.setText("设置Bitmap 为 null");
    }

    @Override
    public void setButton3() {
        button3.setText("设置 为 int值");
    }

    @Override
    public void clickButton1() {

        Resources res = getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.img_test);
       // SmartCache.put(CACHE_KEY_BITMAP, bitmap);
    }

    @Override
    public void clickButton2() {
        SmartCache.put(CACHE_KEY_BITMAP, null);
    }

    @Override
    public void clickButton3() {
        SmartCache.put(CACHE_KEY_BITMAP, 2018);

    }

    @Override
    public void jump() {
        Intent intent = new Intent(mContext, SaveBitmapActivity.class);
        Bundle params = new Bundle();
        params.putInt(INTENT_PAGE_INDEX, index + 1);
        intent.putExtras(params);
        startActivity(intent);
    }
}
