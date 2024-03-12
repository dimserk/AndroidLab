package aaa.dimserk.nativelib

class NativeLib {

    external fun getHello(): String

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }
}