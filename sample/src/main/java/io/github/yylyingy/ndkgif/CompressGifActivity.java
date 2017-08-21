package io.github.yylyingy.ndkgif;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;

import io.github.yylyingy.simplegif.GifDecodeInfoHandle;
import io.github.yylyingy.simplegif.GifEncodeInfoHandle;
import io.github.yylyingy.simplegif.SimpleGif;

public class CompressGifActivity extends BaseActivity {
    Button mButton;
    private static final int IMAGE = 1;
    ImageView mImageView;
    Slider transform;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compress_gif);
        mButton = (Button) findViewById(R.id.selectPicture);
        transform = (Slider) findViewById(R.id.transform);
        mImageView = (ImageView) findViewById(R.id.image);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用相册
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            try {
                showImage(imagePath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            c.close();
        }
    }

    //加载图片
    private void showImage(String imaePath) throws FileNotFoundException {
//        Bitmap bm = BitmapFactory.decodeFile(imaePath);
//        ((ImageView)findViewById(R.id.image)).setImageBitmap(bm);
        String dstFile = "result.gif";
        final String filePath = Environment.getExternalStorageDirectory() + File.separator + dstFile;
        SimpleGif.with(this).load(imaePath).into(mImageView);
        GifEncodeInfoHandle encodeInfoHandle = new GifEncodeInfoHandle();
        //设置想要的大小
        int newWidth=80;
        int newHeight=80;
        GifDecodeInfoHandle decodeInfoHandle = new GifDecodeInfoHandle();
        decodeInfoHandle.load(imaePath);
        encodeInfoHandle.init(newWidth,newHeight,filePath, GifEncodeInfoHandle.EncodingType.ENCODING_TYPE_NORMAL_LOW_MEMORY);
        Bitmap bitmap = null;
        for (int i = 0;i < decodeInfoHandle.frameNum();i ++) {
            bitmap = decodeInfoHandle.frame(i);
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            //计算压缩的比率
            float scaleWidth=((float)newWidth)/width;
            float scaleHeight=((float)newHeight)/height;
            //获取想要缩放的matrix
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth,scaleHeight);
            //获取新的bitmap
            bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
            encodeInfoHandle.encodeFrame(bitmap,decodeInfoHandle.delay(i));
        }
        encodeInfoHandle.close();
    }

    private Bitmap changeBitmapSize() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e("width","width:"+width);
        Log.e("height","height:"+height);
        //设置想要的大小
        int newWidth=30;
        int newHeight=30;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width;
        float scaleHeight=((float)newHeight)/height;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();
        Log.e("newWidth","newWidth"+bitmap.getWidth());
        Log.e("newHeight","newHeight"+bitmap.getHeight());
        return bitmap;
    }
}
