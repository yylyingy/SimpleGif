package io.github.yylyingy.gifencodedecode;

import android.graphics.Bitmap;

public class GifDecoder {

    static {
        System.loadLibrary("androidndkgif");
    }

    private native long nativeInit();
    private native void nativeClose(long handle);

    private native boolean nativeLoad(long handle, String fileName);

    private native int nativeGetFrameCount(long handle);

    private native Bitmap nativeGetFrame(long handle, int n);
    private native int nativeGetDelay(long handle, int n);

    private native int nativeGetWidth(long handle);
    private native int nativeGetHeight(long handle);

    private int width = 0;
    private int height = 0;

    private Bitmap[] bitmaps = new Bitmap[0];
    private int[] delays = new int[0];
    private int frameNum;
    private long handle;
    private boolean isDestroy = true;

    public synchronized boolean load(String fileName) {
        handle = nativeInit();
        isDestroy = false;
        if (!nativeLoad(handle, fileName)) {
            nativeClose(handle);
            return false;
        }
        width = nativeGetWidth(handle);
        height = nativeGetHeight(handle);

        frameNum = nativeGetFrameCount(handle);
        bitmaps = new Bitmap[frameNum];
        delays = new int[frameNum];
        for (int i = 0; i < frameNum; ++i) {
           // bitmaps[i] = nativeGetFrame(handle, i);
            delays[i] = nativeGetDelay(handle, i);
        }

        //nativeClose(handle);
        return true;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public synchronized void destroy(){
        nativeClose(handle);
        isDestroy = true;
    }

    /**
     *
     * @return pictures's num.
     */
    public int frameNum() {
        return frameNum;
    }

    /**
     * To get one picture's bitmap
     * @param idx the picture's index
     * @return picture's bitmap .
     */
    public Bitmap frame(int idx) {
        Bitmap bitmap;
        if (0 == frameNum) {
            return null;
        }
        if (isDestroy){
            bitmap = null;
        }else {
            bitmap = nativeGetFrame(handle,idx % frameNum);
        }
        return bitmap;
    }

    /**
     * Current picture's display time
     * @param idx the picture index
     * @return The display time
     */
    public int delay(int idx) {
        if (0 == frameNum) {
            return 0;
        }
        return delays[idx % frameNum];
    }
}
