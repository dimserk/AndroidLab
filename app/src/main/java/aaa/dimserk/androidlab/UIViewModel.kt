package aaa.dimserk.androidlab

import aaa.dimserk.nativelib.NativeLib
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel

class UIViewModel : ViewModel() {

    private val nativeLib = NativeLib()

    fun getHelloMessage(): String {
        return nativeLib.getHello()
    }

    suspend fun longTask() {
        nativeLib.longTask()
    }

    private val context = LocalContext
}