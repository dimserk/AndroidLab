package aaa.dimserk.androidlab

import aaa.dimserk.nativelib.NativeLib
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UIViewModel : ViewModel() {
    // context доступен через LocalContext

    val changableString = MutableLiveData("foo")

    private val nativeLib = NativeLib()

    private val _isLongTaskRunning = MutableLiveData(false)
    val isLongTaskRunning: LiveData<Boolean> = _isLongTaskRunning

    suspend fun longTask() {
            Logger.verbose("Long task started")
            _isLongTaskRunning.value = true
            nativeLib.longTask()
            Logger.verbose("Long task done")
            _isLongTaskRunning.value = false
    }
}