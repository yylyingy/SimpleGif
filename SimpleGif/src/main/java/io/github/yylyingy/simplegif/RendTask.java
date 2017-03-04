package io.github.yylyingy.gifencodedecode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Handler;
import android.widget.ImageView;

/**
 * Created by Yangyl on 2017/3/4.
 */

public class RendTask {
    private GifDecodeInfoHandle mDecodeInfo;
    private ImageView imageView;
    private Handler handler = new Handler();
    private Activity displayActivity = null;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!displayActivity.isFinishing()) {
//                draw();
                imageView.setImageBitmap(mDecodeInfo.frame());
                handler.postDelayed(runnable, mDecodeInfo.delay());
            }else {
                handler.removeCallbacks(this);
                mDecodeInfo.destroy();
            }
        }
    };
    RendTask(Context context){
        displayActivity = (Activity) context;
    }

    RendTask init(String file){
        if (file != null){
            mDecodeInfo = new GifDecodeInfoHandle();
            mDecodeInfo.load(file);
        }
        return this;
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;
        if (mDecodeInfo == null) {
            return;
        } else if (imageView == null) {
            throw new RuntimeException("ImageView can not be null");
        } else {

//            开始在imageview里面绘制电影
//            movie = Movie.decodeStream(is);//gif小电影
//            if (movie == null) {
//                throw new IllegalArgumentException("Illegal gif file");
//
//            }
//            if (movie.width() <= 0 || movie.height() <= 0) {
//                return;
//            }
////            需要bitmap
//            bitmap = Bitmap.createBitmap(movie.width(), movie.height(), Bitmap.Config.RGB_565);
//            canvas = new Canvas(bitmap);
//            准备把canvas的小电影显示在imageview里面
            handler.post(runnable);
        }
    }
}
