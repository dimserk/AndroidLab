package aaa.dimserk.nativelib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeLib {

    external fun getHello(): String

    private external fun longTaskJNI()

    suspend fun longTask() = withContext(Dispatchers.Default) {
        longTaskJNI()
    }

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }
}