package io.github.yylyingy.simplegif.manager;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;

import io.github.yylyingy.simplegif.RequestManager;
import io.github.yylyingy.simplegif.manager.interfaces.Lifecycle;

/**
 * Created by Yangyl on 2017/3/7.
 */

public class SupportRequestManagerFragment extends Fragment {
    private RequestManager requestManager;
    private ActivityFragmentLifecycle lifecycle;
    public SupportRequestManagerFragment(){
        this(new ActivityFragmentLifecycle());
    }
    @SuppressLint("ValidFragment")
    public SupportRequestManagerFragment(ActivityFragmentLifecycle lifecycle){
        this.lifecycle = lifecycle;
    }

    ActivityFragmentLifecycle getLifecycle(){
        return this.lifecycle;
    }

    /**
     * Return the current {@link RequestManager} or null if not is set.
     * @return
     */
    public RequestManager getRequestManager() {
        return requestManager;
    }

    /**
     * Sets the current {@link RequestManager}
     * @param requestManager The manager to set.
     */
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // If an activity is re-created, onLowMemory may be called before a manager is ever set.
        // See #329.
        if (requestManager != null) {
            requestManager.onLowMemory();
        }
    }
}
