package io.github.yangyl.ndkgif;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.github.yylyingy.gifencodedecode.GifDecoder;

public class MainActivity extends AppCompatActivity {

    private boolean useDither = true;
    private static final int DISPLAY_GIF = 0x123;
    private GifDecoder gifDecoder = new GifDecoder();
    ImageView imageView;
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

        imageView = (ImageView) findViewById(R.id.image_view);

        new Thread(new Runnable() {

            @Override
            public void run() {
                String destFile = setupSampleFile();

                final boolean isSucceeded = gifDecoder.load(destFile);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                long displayTime = 0;
                long startDisplayTime = 0;
                long lostTime = 0;
                while (true){
                    if (isSucceeded) {
                        for (int i = 1;i <= gifDecoder.frameNum();i ++){
                            long wast = System.currentTimeMillis();
                            Bitmap bitmap = gifDecoder.frame(i);
                            if (bitmap == null)break;
                            Message message = Message.obtain();
                            message.what = DISPLAY_GIF;
                            message.obj = bitmap;
                            mHandler.sendMessage(message);
                            startDisplayTime = System.currentTimeMillis();
                            displayTime = gifDecoder.delay(i);
                            while ((lostTime) < displayTime ){
                                lostTime = System.currentTimeMillis() - startDisplayTime;
                            }
                            lostTime = 0;
                            wast = System.currentTimeMillis() - wast;
                            Log.d(getLocalClassName(),"wast" + wast);
//                                try {
//                                    Thread.sleep(gifDecoder.delay(i));
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
//                    }
//                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        gifDecoder.destroy();
        super.onDestroy();
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
