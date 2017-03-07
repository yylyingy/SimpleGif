package io.github.yylyingy.simplegif.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.github.yylyingy.simplegif.RequestManager;
import io.github.yylyingy.simplegif.util.Util;

/**
 * Created by Yangyl on 2017/3/7.
 * SingleTon.
 * A collection of static methods for creating new {@link RequestManager}s or retrieving existing
 * ones from activities and fragment.
 */

public class RequestManagerRetriever implements Handler.Callback{
    private static final String TAG = "RMRetriever";
    static final String FRAGMENT_TAG = RequestManagerRetriever.class.getPackage().toString();
    /**
     * The singleton instance of {@link RequestManagerRetriever}.
     */
    private static final RequestManagerRetriever INSTANCE = new RequestManagerRetriever();
    private static final int ID_REMOVE_FRAGMENT_MANAGER = 1;
    private static final int ID_REMOVE_SUPPORT_FRAGMENT_MANAGER = 2;
    /**The top application level {@link RequestManager}   */
    private volatile RequestManager applicationManager;

    private final Map<android.app.FragmentManager,RequestManagerFragment>
            pendingRequestManagerFragments  = new HashMap<>();
    private final Map<android.support.v4.app.FragmentManager,SupportRequestManagerFragment>
            pendingSupportRequestManagerFragments = new HashMap<>();

    private final Handler handler;

    public static RequestManagerRetriever get(){
        return INSTANCE;
    }

    private RequestManagerRetriever(){
        handler = new Handler(Looper.getMainLooper(),this);
    }

    private RequestManager getApplicationManager(Context context){
        if (applicationManager == null){
            synchronized (this){
                if (applicationManager == null){
                    applicationManager = new RequestManager(context.getApplicationContext(),
                            new ApplicationLifecycle());
                }
            }
        }
        return applicationManager;
    }

    public RequestManager get(Context context){
        if (context == null){
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }else if (Util.isOnMainThread() && !(context instanceof Application)){
            if (context instanceof FragmentActivity){
                return get((FragmentActivity) context);
            }else if (context instanceof Activity){
                return get((Activity)context);
            }else if(context instanceof ContextWrapper){
                get(((ContextWrapper) context).getBaseContext());
            }
        }
        return getApplicationManager(context);

    }

    public RequestManager get(FragmentActivity activity){
        if (Util.isOnBackgroundThread() || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return get(activity.getApplicationContext());
        } else {
            assertNotDestroyed(activity);
            FragmentManager fm = activity.getSupportFragmentManager();
            return supprotFragmentGet(activity, fm);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public RequestManager get(Activity activity){
        if (Util.isOnBackgroundThread()){
            return get(activity.getApplicationContext());
        }
        assertNotDestroyed(activity);
        android.app.FragmentManager fm = activity.getFragmentManager();
        return fragmentGet(activity,fm);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public RequestManager get(android.app.Fragment fragment){
        if (fragment.getActivity() == null){
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (Util.isOnBackgroundThread()){
            return get(fragment.getActivity().getApplicationContext());
        }else {
            android.app.FragmentManager fm = fragment.getChildFragmentManager();
            return fragmentGet(fragment.getActivity(),fm);
        }

    }

    public RequestManager get(Fragment fragment){
        if (fragment.getActivity() == null){
            throw new IllegalArgumentException("You cannot start a load on a fragment before it is attached");
        }
        if (Util.isOnBackgroundThread()){
            return get(fragment.getActivity().getApplicationContext());
        }else {
            FragmentManager fm = fragment.getChildFragmentManager();
            return supprotFragmentGet(fragment.getActivity(),fm);
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    RequestManager supprotFragmentGet(Context context,FragmentManager fm){
        SupportRequestManagerFragment current = getSupportRequestFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null){
            requestManager = new RequestManager(context,current.getLifecycle());
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }

    SupportRequestManagerFragment getSupportRequestFragment(final FragmentManager fm){
        SupportRequestManagerFragment current = (SupportRequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null){
            current = pendingSupportRequestManagerFragments.get(fm);
            if (current == null){
                current = new SupportRequestManagerFragment();
                pendingSupportRequestManagerFragments.put(fm,current);
                fm.beginTransaction().add(current,FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_SUPPORT_FRAGMENT_MANAGER,fm).sendToTarget();
            }
        }
        return current;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    RequestManager fragmentGet(Context context,android.app.FragmentManager fm){
        RequestManagerFragment current = getRequestManagerFragment(fm);
        RequestManager requestManager = current.getRequestManager();
        if (requestManager == null){
            requestManager = new RequestManager(context,current.getLifecycle());
            current.setRequestManager(requestManager);
        }
        return requestManager;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    RequestManagerFragment getRequestManagerFragment(final android.app.FragmentManager fm){
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null){
            current = pendingRequestManagerFragments.get(fm);
            if (current == null){
                current = new RequestManagerFragment();
                pendingRequestManagerFragments.put(fm,current);
                fm.beginTransaction().add(current,FRAGMENT_TAG).commitAllowingStateLoss();
                handler.obtainMessage(ID_REMOVE_FRAGMENT_MANAGER,fm).sendToTarget();
            }
        }
        return current;
    }

    @Override
    public boolean handleMessage(Message msg) {
        boolean handled = true;
        Object removed = null;
        Object key = null;
        switch (msg.what){
            case ID_REMOVE_FRAGMENT_MANAGER:
                android.app.FragmentManager fm = (android.app.FragmentManager) msg.obj;
                removed = null;
                removed = pendingRequestManagerFragments.remove(fm);
                break;
            case ID_REMOVE_SUPPORT_FRAGMENT_MANAGER:
                FragmentManager supportFm = (FragmentManager) msg.obj;
                key = supportFm;
                removed = pendingSupportRequestManagerFragments.remove(supportFm);
                break;
            default:
                handled = false;
                break;
        }
        if (handled && removed == null && Log.isLoggable(TAG, Log.WARN)) {
            Log.w(TAG, "Failed to remove expected request manager fragment, manager: " + key);
        }
        return handled;
    }
}






















