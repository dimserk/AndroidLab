package aaa.dimserk.androidlab

import aaa.dimserk.nativelib.NativeLib
import androidx.lifecycle.ViewModel

class UIViewModel : ViewModel() {
    // context доступен через LocalContext

    private val nativeLib = NativeLib()

    fun getHelloMessage() = nativeLib.getHello()

    suspend fun longTask() = nativeLib.longTask()
}