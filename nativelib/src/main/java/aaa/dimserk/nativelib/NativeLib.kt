package aaa.dimserk.nativelib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NativeLib {

    /**
     * Поле кода возврата методов JNI
     */
    private var jniReturnCode = 0

    external fun getHello(): String

    private external fun longTaskJNI()

    suspend fun longTask() = withContext(Dispatchers.Default) {
        longTaskJNI()

        if (jniReturnCode != -1)
            throw Exception("Something went wrong")
    }

    companion object {
        init {
            System.loadLibrary("nativelib")
        }
    }
}