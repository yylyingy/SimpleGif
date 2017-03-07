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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;


import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.yylyingy.simplegif.SimpleGif;


public class MainActivity extends AppCompatActivity {
    private boolean useDither = true;
    private static final int DISPLAY_GIF = 0x123;
    private StringBuilder file = new StringBuilder(Environment.getExternalStorageDirectory() + File.separator + "360"
            + File.separator + "simplegif" + File.separator);
    private String [] files = new String[9];
    ImageView imageView;
    private boolean isThreadNeedRunnine = true;
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
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.image_view1);
        for (int i = 2;i < 10;i ++){
            files[i - 2] = file.toString() + "sample" + i + ".gif";
            Log.d(getLocalClassName(),files[i - 2]);
        }
        SimpleGif.with(this).load(setupSampleFile()).into(imageView);
        SimpleGif.with(this).load(files[0]).into((ImageView) findViewById(R.id.image_view2));
        SimpleGif.with(this).load(files[1]).into((ImageView) findViewById(R.id.image_view3));
        SimpleGif.with(this).load(files[2]).into((ImageView) findViewById(R.id.image_view4));
        SimpleGif.with(this).load(files[3]).into((ImageView) findViewById(R.id.image_view5));
        SimpleGif.with(this).load(files[4]).into((ImageView) findViewById(R.id.image_view6));
        SimpleGif.with(this).load(files[5]).into((ImageView) findViewById(R.id.image_view7));
        SimpleGif.with(this).load(files[6]).into((ImageView) findViewById(R.id.image_view8));
        SimpleGif.with(this).load(files[7]).into((ImageView) findViewById(R.id.image_view9));

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
        isThreadNeedRunnine = false;
        super.onDestroy();
        ((SampleApplication)getApplication()).mWatcher.watch(this);
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
