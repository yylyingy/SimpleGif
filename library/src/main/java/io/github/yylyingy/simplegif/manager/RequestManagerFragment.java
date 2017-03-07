package io.github.yylyingy.simplegif.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

import io.github.yylyingy.simplegif.RequestManager;

/**
 * Created by Yangyl on 2017/3/7.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RequestManagerFragment extends Fragment {
    private final ActivityFragmentLifecycle lifecycle;
    private RequestManager requestManager;

    public RequestManagerFragment(){
        this(new ActivityFragmentLifecycle());
    }

    @SuppressLint("ValidFragment")
    public RequestManagerFragment(ActivityFragmentLifecycle lifecycle){
        this.lifecycle = lifecycle;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }

    public ActivityFragmentLifecycle getLifecycle() {
        return lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycle.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycle.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }

    /**
     * This method will be called by system when the activity is killed by system.
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (requestManager != null){
            requestManager.onLowMemory();
        }
    }
}
