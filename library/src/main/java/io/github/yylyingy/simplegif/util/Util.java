package io.github.yylyingy.simplegif.util;

import android.os.Looper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Yangyl on 2017/3/6.
 */

public class Util {

    public static void assertMainThread(){
        if (!isOnMainThread()){
            throw new IllegalArgumentException("You must call this method on the main thread!");
        }
    }

    public static boolean isOnMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnBackgroundThread(){
        return !isOnMainThread();
    }

    public static void assertBackgroundThread(){
        if (!isOnBackgroundThread()){
            throw new IllegalArgumentException("You must call this method on a background thread!");
        }
    }

    /**
     * Returns a copy of the given list that is safe to iterate over and perform actions that may
     * modify the original list.
     *
     * <p> See #303 and #375. </p>
     */
    public static <T> List<T> getSnapshot(Collection<T> other) {
        // toArray creates a new ArrayList internally and this way we can guarantee entries will not
        // be null. See #322.
        List<T> result = new ArrayList<T>(other.size());
        for (T item : other) {
            result.add(item);
        }
        return result;
    }
}
