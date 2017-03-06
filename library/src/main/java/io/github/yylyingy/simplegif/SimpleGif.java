package io.github.yylyingy.simplegif;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by Yangyl on 2017/3/4.
 */

public class SimpleGif {
    private Context mContext;
    private RendTask mRendTask;
    private SimpleGif(Context context){
        mContext  = context;
        mRendTask = new RendTask(mContext);
    }
    public static SimpleGif with(@NonNull Context context){
        return new SimpleGif(context);
    }

    public static SimpleGif with(Fragment fragment){
        return null;
    }

    public RendTask load(String file){
        String gifFormat = ".gif";
        StringBuilder fileFormat = new StringBuilder();
        for (int i = file.length() - 4;i < file.length();i ++){
            fileFormat.append(file.charAt(i));
        }

        if (fileFormat.toString().equals(gifFormat)) {

            return mRendTask.init(file);
        }else {
            return mRendTask.init(null);
        }
    }

}
