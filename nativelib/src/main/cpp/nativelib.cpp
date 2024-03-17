#include <jni.h>

#include <thread>
#include <chrono>

extern "C"
JNIEXPORT jstring JNICALL
Java_aaa_dimserk_nativelib_NativeLib_getHello(
    JNIEnv *env,
    jobject)
{
    const char *message = "Hello world from C++!";
    return env->NewStringUTF(message);
}
extern "C"
JNIEXPORT void JNICALL
Java_aaa_dimserk_nativelib_NativeLib_longTaskJNI(
    JNIEnv*,
    jobject)
{
    std::this_thread::sleep_for(std::chrono::milliseconds(3000L));
}