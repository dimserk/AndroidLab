package aaa.dimserk.androidlab

import android.util.Log

class Logger {
    companion object {
        private const val TAG = "LOGGER_TAG"

        fun verbose(message: String) {
            Log.v(TAG, message)
        }

        fun debug(message: String) {
            Log.d(TAG, message)
        }
    }
}