#include "io_github_yylyingy_simplegif_GifDecodeInfoHandle.h"
#include "GifDecoder.h"
#include <string.h>
#include <wchar.h>
#include <android/bitmap.h>
#include <assert.h>
#include "openssllib/include/openssl/des.h"

#ifdef __cplusplus
extern "C" {
#endif
#define JNIREG_CLASS "io/github/yylyingy/simplegif/GifDecodeInfoHandle"

static JNINativeMethod gMethods[] = {
        {"nativeInit","()L;",(void*)nativeInit},
//        {"nativeDestroy","(L;)V;",(void *) nativeDestroy}
};
//__attribute__((section (".simpletext")))
jlong nativeInit(JNIEnv *env, jobject)
{
    return (jlong)new GifDecoder();
}
/*
* Register several native methods for one class.
*/
static int registerNativeMethods(JNIEnv* env, const char* className,
                                 JNINativeMethod* gMethods, int numMethods)
{
    jclass clazz;
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        return JNI_FALSE;
    }
    if (env->RegisterNatives(clazz, gMethods, numMethods) < 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}
static int registerNatives(JNIEnv* env)
{
    if (!registerNativeMethods(env, JNIREG_CLASS, gMethods,
                               sizeof(gMethods) / sizeof(gMethods[0])))
        return JNI_FALSE;

    return JNI_TRUE;
}
//jint JNI_OnLoad(JavaVM* vm,void* reserved){
//    //anti_debug();
//    JNIEnv* env;
//    if (vm->GetEnv((void**)(&env), JNI_VERSION_1_6) != JNI_OK)
//
//    {
//        return -1;
//    }
//    assert(env != NULL);
//
//    if (!registerNatives(env)) {//注册
//        return -1;
//    }
//
//    return JNI_VERSION_1_6;
//}

__attribute__((section (".simpletext")))
JNIEXPORT jlong JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeInit
  (JNIEnv *env, jobject)
{
    return (jlong)new GifDecoder();
}

__attribute__((section (".simpletext")))
JNIEXPORT void JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeClose
  (JNIEnv *, jobject, jlong handle)
{
    delete (GifDecoder*)handle;
}

__attribute__((section (".simpletext")))
JNIEXPORT jboolean JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeLoad
  (JNIEnv * env, jobject, jlong handle, jstring fileName)
{
    const char* fileNameChars = env->GetStringUTFChars(fileName, 0);
    bool result = (jboolean) ((GifDecoder*)handle)->load(fileNameChars);
    env->ReleaseStringUTFChars(fileName, fileNameChars);
    return (jboolean) result;
}

__attribute__((section (".simpletext")))
JNIEXPORT jboolean JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeLoadFromMemory(
    JNIEnv *env,jobject thiz,jlong handle,jbyteArray data,jint size)
{
    uint8_t* nativeData = (uint8_t *) (*env).GetByteArrayElements(data, 0);
    bool result = ((GifDecoder*)handle)->loadFromMemory(nativeData, (uint32_t) size);
    (*env).ReleaseByteArrayElements(data, (jbyte *) nativeData, 0);
    return (jboolean) result;
}

__attribute__((section (".simpletext")))
JNIEXPORT jint JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeGetFrameCount
  (JNIEnv *, jobject, jlong handle)
{
    return ((GifDecoder*)handle)->getFrameCount();
}

__attribute__((section (".simpletext")))
JNIEXPORT jobject JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeGetFrame
  (JNIEnv *env, jobject, jlong handle, jint idx)
{
    GifDecoder* decoder = (GifDecoder*)handle;
    int imgWidth = decoder->getWidth();
    int imgHeight = decoder->getHeight();

    // Creaing Bitmap Config Class
    jclass bmpCfgCls = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID bmpClsValueOfMid = env->GetStaticMethodID(bmpCfgCls, "valueOf", "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");
    jobject jBmpCfg = env->CallStaticObjectMethod(bmpCfgCls, bmpClsValueOfMid, env->NewStringUTF("ARGB_8888"));

    // Creating a Bitmap Class
    jclass bmpCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapMid = env->GetStaticMethodID(bmpCls, "createBitmap", "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jobject jBmpObj = env->CallStaticObjectMethod(bmpCls, createBitmapMid, imgWidth, imgHeight, jBmpCfg);

    void* bitmapPixels;
    if (AndroidBitmap_lockPixels(env, jBmpObj, &bitmapPixels) < 0) {
        return 0;
    }
    //uint32_t* src = (uint32_t*) bitmapPixels;
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(bitmapPixels, decoder->getFrame(idx), pixelsCount);
    AndroidBitmap_unlockPixels(env, jBmpObj);

    return jBmpObj;
}

__attribute__((section (".simpletext")))
JNIEXPORT jobject JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_renderFrame
        (JNIEnv *env, jobject, jlong handle, jint idx,jobject jBmpObj){
    GifDecoder* decoder = (GifDecoder*)handle;
    int imgWidth = decoder->getWidth();
    int imgHeight = decoder->getHeight();
    void* bitmapPixels;
    if (AndroidBitmap_lockPixels(env, jBmpObj, &bitmapPixels) < 0) {
        return 0;
    }
    //uint32_t* src = (uint32_t*) bitmapPixels;
    int stride = imgWidth * 4;
    int pixelsCount = stride * imgHeight;
    memcpy(bitmapPixels, decoder->getFrame(idx), pixelsCount);
    AndroidBitmap_unlockPixels(env, jBmpObj);
    return jBmpObj;
}

__attribute__((section (".simpletext")))
JNIEXPORT jint JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeGetDelay
        (JNIEnv *, jobject, jlong handle, jint idx)
{
    return ((GifDecoder*)handle)->getDelay(idx);
}

__attribute__((section (".simpletext")))
JNIEXPORT jint JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeGetWidth
  (JNIEnv *, jobject, jlong handle)
{
    return ((GifDecoder*)handle)->getWidth();
}

__attribute__((section (".simpletext")))
JNIEXPORT jint JNICALL Java_io_github_yylyingy_simplegif_GifDecodeInfoHandle_nativeGetHeight
  (JNIEnv *, jobject, jlong handle)
{
    return ((GifDecoder*)handle)->getHeight();
}




#ifdef __cplusplus
}
#endif
