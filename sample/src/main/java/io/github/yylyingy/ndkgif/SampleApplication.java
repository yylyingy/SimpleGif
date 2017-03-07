package io.github.yylyingy.ndkgif;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Yangyl on 2017/3/7.
 */

public class SampleApplication extends Application {
    public RefWatcher mWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        mWatcher = LeakCanary.install(this);
    }
}
