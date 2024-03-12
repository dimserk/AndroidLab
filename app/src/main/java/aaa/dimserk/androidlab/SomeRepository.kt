package aaa.dimserk.androidlab

import android.app.Application
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SomeRepository @Inject constructor(
    private val context: Application,
) {

    fun foo(): String {
        return "foo" + context.filesDir
    }
}