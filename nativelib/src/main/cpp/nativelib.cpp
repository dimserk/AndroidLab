#include <jni.h>

// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("main");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("main")
//      }
//    }

extern "C"
JNIEXPORT jstring JNICALL
Java_aaa_dimserk_nativelib_NativeLib_getHello(
        JNIEnv *env,
        jobject)
{
    const char *message = "Hello world from C++!";
    return env->NewStringUTF(message);
}