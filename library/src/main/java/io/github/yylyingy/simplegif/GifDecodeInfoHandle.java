package io.github.yylyingy.simplegif;

import android.graphics.Bitmap;

public class GifDecodeInfoHandle {

    static {
        System.loadLibrary("simple_gif");
    }

    private int         currentFrameIndex;
    private int         nextFrameIndex;

    private int         width = 0;
    private int         height = 0;

    private Bitmap      frame;
    private int[]       delays = new int[0];
    private int         frameNum;
    private long        handle;
    private boolean     isDestroy = true;

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
        delays = new int[frameNum];
        for (int i = 0; i < frameNum; ++i) {
            delays[i] = nativeGetDelay(handle, i);
        }
        currentFrameIndex = 0;
        nextFrameIndex    = 0;
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
        frame.recycle();
    }

    /**
     *
     * @return pictures's num.
     */
    public int frameNum() {
        return frameNum;
    }

    public synchronized Bitmap frame(){
        nextFrameIndex ++;
        if (nextFrameIndex == frameNum){
            nextFrameIndex = 0;
        }
        currentFrameIndex = nextFrameIndex - 1;
        if (currentFrameIndex < 0){
            currentFrameIndex = frameNum - 1;
        }
        return frame(currentFrameIndex);
    }

    /**
     * To get one picture's bitmap
     * @param idx the picture's index
     * @return picture's bitmap .
     */
    public synchronized Bitmap frame(int idx) {
        if (frame == null){
            frame = Bitmap.createBitmap(nativeGetWidth(handle),
                    nativeGetHeight(handle),Bitmap.Config.ARGB_8888);
        }else {
            frame.prepareToDraw();
//            frame.recycle();
        }
        if (0 > frameNum) {
            return null;
        }
        if (isDestroy){
            frame = null;
        }else {
//            frame = nativeGetFrame(handle,idx % frameNum);
            renderFrame(handle,idx % frameNum,frame);
        }
        return frame;
    }

    public int delay(){
        return delays[currentFrameIndex];
    }
    /**
     * Current picture's display time
     * @param idx the picture index
     * @return The display time
     */
    public int delay(int idx) {
        return delays[idx];
    }


    private native long nativeInit();
    private native void nativeClose(long handle);

    private native boolean nativeLoad(long handle, String fileName);
    private native boolean nativeLoadFromMemory(long handle,byte[]data,int size);

    private native int nativeGetFrameCount(long handle);

    private native Bitmap nativeGetFrame(long handle, int n);
    private native Bitmap renderFrame(long handle, int n,Bitmap bitmap);
    private native int nativeGetDelay(long handle, int n);

    private native int nativeGetWidth(long handle);
    private native int nativeGetHeight(long handle);
}
