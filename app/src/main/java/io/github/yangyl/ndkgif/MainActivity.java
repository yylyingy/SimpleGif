package io.github.yangyl.ndkgif;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;


import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.yangyl.ndkgif.R;
import io.github.yylyingy.gifencodedecode.SimpleGif;


public class MainActivity extends AppCompatActivity {
    private RefWatcher mWatcherl;
    private boolean useDither = true;
    private static final int DISPLAY_GIF = 0x123;
    ImageView imageView;
    private boolean isThreadNeedRunnine = true;
    Bitmap mBitmap = null;
    Drawable mDrawable = new BitmapDrawable(mBitmap);
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case DISPLAY_GIF:
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWatcherl = LeakCanary.install(getApplication());
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view);
//        Glide.with(this).load(setupSampleFile()).into(imageView);
        SimpleGif.with(this).load(setupSampleFile()).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                String destFile = setupSampleFile();
//
//                final boolean isSucceeded = gifDecoder.load(destFile);
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
//                long displayTime = 0;
//                long startDisplayTime = 0;
//                long lostTime = 0;
//                Bitmap bitmap = null;
//                while (isThreadNeedRunnine){
//                    if (isSucceeded) {
//                        for (int i = 1;i <= gifDecoder.frameNum();i ++){
//                            long wast = System.currentTimeMillis();
//
//                            bitmap = gifDecoder.frame(i);
//                            if (bitmap == null)break;
////                            Matrix matrix = new Matrix();
////                            matrix.postScale(0.5f,0.5f);
////                            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),
////                                    matrix,true);
//                            Message message = Message.obtain();
//                            message.what = DISPLAY_GIF;
//                            message.obj = bitmap;
//                            mHandler.sendMessage(message);
//                            startDisplayTime = System.currentTimeMillis();
//                            displayTime = gifDecoder.delay(i);
//                            while ((lostTime) < displayTime ){
//                                lostTime = System.currentTimeMillis() - startDisplayTime;
//                            }
//                            lostTime = 0;
//                            wast = System.currentTimeMillis() - wast;
//                                try {
//                                    Thread.sleep(gifDecoder.delay(i));
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                        }
//
//                    } else {
//                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }
////                    }
////                });
//            }
//        }).start();
    }

    @Override
    protected void onDestroy() {
        Log.d(getLocalClassName(),"Destroy!");
        isThreadNeedRunnine = false;
        super.onDestroy();
        mWatcherl.watch(this);
    }

    private String setupSampleFile() {
        AssetManager assetManager = getAssets();
        String srcFile = "sample1.gif";
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

//    public void onEncodeGIF(View v) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    encodeGIF();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }

//    private void encodeGIF() throws IOException {
//        String dstFile = "result.gif";
//        final String filePath = Environment.getExternalStorageDirectory() + File.separator + dstFile;
//        int width = 50;
//        int height = 50;
//        int delayMs = 100;
//
//        GifEncoder gifEncoder = new GifEncoder();
//        gifEncoder.init(width, height, filePath, GifEncoder.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
//        gifEncoder.setDither(useDither);
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Paint p = new Paint();
//        int[] colors = new int[] {0xFFFF0000, 0xFFFFFF00, 0xFFFFFFFF};
//        for (int color : colors) {
//            p.setColor(color);
//            canvas.drawRect(0, 0, width, height, p);
//            gifEncoder.encodeFrame(bitmap, delayMs);
//        }
//        gifEncoder.close();
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainActivity.this, "done : " + filePath, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    public void onDisableDithering(View v) {
//        useDither = false;
//    }
}
