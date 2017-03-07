package io.github.yylyingy.simplegif;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.File;

import io.github.yylyingy.simplegif.manager.RequestTracker;
import io.github.yylyingy.simplegif.manager.interfaces.Lifecycle;
import io.github.yylyingy.simplegif.manager.interfaces.LifecycleListener;
import io.github.yylyingy.simplegif.util.Util;

/**
 * Created by Yangyl on 2017/3/7.
 */

public class RequestManager implements LifecycleListener{
    private final Context context;
    private final Lifecycle lifecycle;
//    private final RequestManagerTreeNode treeNode;
    private final RequestTracker  requestTracker;
//    private final Glide glide;
//    private final OptionsApplier optionsApplier;
//    private DefaultOptions options;

    public RequestManager(Context context, Lifecycle lifecycle) {
        this(context, lifecycle, new RequestTracker());
    }

    RequestManager(Context context, final Lifecycle lifecycle,
                   RequestTracker requestTracker) {
        this.context = context.getApplicationContext();
        this.lifecycle = lifecycle;
//        this.treeNode = treeNode;
        this.requestTracker = requestTracker;
//        this.glide = Glide.get(context);
//        this.optionsApplier = new OptionsApplier();

//        ConnectivityMonitor connectivityMonitor = factory.build(context,
//                new RequestManagerConnectivityListener(requestTracker));

        // If we're the application level request manager, we may be created on a background thread. In that case we
        // cannot risk synchronously pausing or resuming requests, so we hack around the issue by delaying adding
        // ourselves as a lifecycle listener by posting to the main thread. This should be entirely safe.
        if (Util.isOnBackgroundThread()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    lifecycle.addListener(RequestManager.this);
                }
            });
        } else {
            lifecycle.addListener(this);
        }
//        lifecycle.addListener(connectivityMonitor);
    }

    public void pauseRequests(){
        Util.assertBackgroundThread();
        requestTracker.pauseRequests();
    }

    public RendTask load(String fileAbsolutePath){
        RendTask rendTask = new RendTask(fileAbsolutePath,requestTracker);
        return rendTask;
    }

    public RendTask load(File file){
        RendTask rendTask = new RendTask(file.toString(),requestTracker);
        return rendTask;
    }
    /**
     * Callback for when {@link Fragment#onStart()}} or {@link Activity#onStart()} is called.
     */
    @Override
    public void onStart() {
        requestTracker.resumeRequests();

    }

    /**
     * Callback for when {@link Fragment#onStop()}} or {@link Activity#onStop()}} is called.
     */
    @Override
    public void onStop() {
        requestTracker.pauseRequests();
    }

    /**
     * Callback for when {@link Fragment#onDestroy()}} or {@link Activity#onDestroy()} is
     * called.
     */
    @Override
    public void onDestroy() {
        requestTracker.clearRequests();
    }

    public void onLowMemory(){

    }
}
