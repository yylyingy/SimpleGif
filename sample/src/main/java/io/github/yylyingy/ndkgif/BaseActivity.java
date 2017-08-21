package io.github.yylyingy.ndkgif;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author yangyinglong on 2017/4/22 12:06.
 * @FileName:
 * @Description
 * @Copyright Copyright (c) 2017 Tuandai Inc. All Rights Reserved.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    protected void onDestroy() {
        IMMLeaks.fixFocusedViewLeak(getApplication());
        super.onDestroy();
    }
}
