package io.github.yylyingy.ndkgif;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;


import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.yylyingy.simplegif.SimpleGif;
import io.github.yylyingy.simplegif.request.Request;


public class MainActivity extends BaseActivity {
    private StringBuilder file = new StringBuilder(Environment.getExternalStorageDirectory() + File.separator + "360"
            + File.separator + "simplegif" + File.separator);
    private String [] filesPath = new String[9];
    ImageView imageView;
    Button btnCompress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCompress = (Button) findViewById(R.id.compress);
        imageView = (ImageView) findViewById(R.id.image_view1);
        btnCompress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CompressGifActivity.class));
            }
        });

        for (int i = 2;i < 10;i ++){
            filesPath[i - 2] = file.toString() + "sample" + i + ".gif";
            Log.d(getLocalClassName(),filesPath[i - 2]);
        }
        SimpleGif.with(this).load(setupSampleFile()).into(imageView);
        final Request request = SimpleGif.with(this).load(setupLoveFile()).into((ImageView) findViewById(R.id.image_view2));
//        Glide.with(this).load(filesPath[1]).into((ImageView) findViewById(R.id.image_view3));
        final com.bumptech.glide.request.Request request1 =  Glide.with(this).load(setupLoveFile()).into((ImageView) findViewById(R.id.image_view3)).getRequest();

//        SimpleGif.with(this).load(filesPath[2]).into((ImageView) findViewById(R.id.image_view4));
//        SimpleGif.with(this).load(filesPath[3]).into((ImageView) findViewById(R.id.image_view5));
//        SimpleGif.with(this).load(filesPath[4]).into((ImageView) findViewById(R.id.image_view6));
//        SimpleGif.with(this).load(filesPath[5]).into((ImageView) findViewById(R.id.image_view7));
//        SimpleGif.with(this).load(filesPath[6]).into((ImageView) findViewById(R.id.image_view8));
//        SimpleGif.with(this).load(filesPath[7]).into((ImageView) findViewById(R.id.image_view9));
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5000);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            request.clear();
////                            request.recycle();
////                            request1.clear();
////                            request1.recycle();
//                        }
//                    });
//                    Thread.sleep(5000);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            request.begin();
////                            request1.begin();
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });


    }

    @Override
    protected void onDestroy() {
        Log.d(getLocalClassName(),"Destroy!");
        super.onDestroy();
    }

    private String setupSampleFile() {
        AssetManager assetManager = getAssets();
        String srcFile = "sample1.gif";
        String destFile = getFilesDir().getAbsolutePath() + File.separator + srcFile;
        copyFile(assetManager, srcFile, destFile);
        return destFile;
    }

    private String setupLoveFile() {
        AssetManager assetManager = getAssets();
        String srcFile = "love.gif";
        String destFile = getFilesDir().getAbsolutePath() + File.separator + srcFile;
        copyFile(assetManager, srcFile, destFile);
        return destFile;
    }

    private void copyFile(AssetManager assetManager, String srcFile, String destFile) {
        try {
            InputStream is = assetManager.open(srcFile);
            FileOutputStream os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            is.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
