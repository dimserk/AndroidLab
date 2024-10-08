package aaa.dimserk.androidlab

import aaa.dimserk.nativelib.NativeLib
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UIViewModel : ViewModel() {
    // context доступен через LocalContext

    val currentNavRoute = MutableLiveData(NavRoutes.ABOUT_SCREEN)

    fun changeCurrentNavRoute(newRoute: String) {
        currentNavRoute.value = newRoute
    }

    private val nativeLib = NativeLib()

    fun getHelloMessage() = nativeLib.getHello()

    suspend fun longTask() = nativeLib.longTask()
}