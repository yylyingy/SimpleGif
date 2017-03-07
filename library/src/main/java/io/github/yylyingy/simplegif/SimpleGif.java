package io.github.yylyingy.simplegif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import io.github.yylyingy.simplegif.manager.RequestManagerRetriever;

/**
 * Created by Yangyl on 2017/3/4.
 */

public class SimpleGif {
    private Context mContext;
    private SimpleGif(Context context){
        mContext  = context;
    }
    public static RequestManager with(Context context){
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(context);
    }

    public static RequestManager with(FragmentActivity activity) {
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(activity);
    }

    public static RequestManager with(Fragment fragment){
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(fragment);
    }

    public static RequestManager with(android.app.Fragment fragment){
        RequestManagerRetriever retriever = RequestManagerRetriever.get();
        return retriever.get(fragment);
    }

//    public RendTask load(String file){
//        String gifFormat = ".gif";
//        StringBuilder fileFormat = new StringBuilder();
//        for (int i = file.length() - 4;i < file.length();i ++){
//            fileFormat.append(file.charAt(i));
//        }
//
//        if (fileFormat.toString().equals(gifFormat)) {
//
//            return mRendTask.init(file);
//        }else {
//            return mRendTask.init(null);
//        }
//    }

}
