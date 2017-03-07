package io.github.yylyingy.simplegif.manager.interfaces;

/**
 * Created by Yangyl on 2017/3/6.
 * An interface for listening to Activity/Fragment lifecycle events
 */

public interface Lifecycle {

    /**
     * Adds the given listener to the set of listeners managed by this Lifecycle implementation.
     * @param listener
     */
    void addListener(LifecycleListener listener);
}
