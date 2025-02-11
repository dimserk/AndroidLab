package aaa.dimserk.androidlab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

enum class NavRoute(
    @StringRes val screenTitleId: Int,
    @DrawableRes val screenIconId: Int,
) {
    MAIN_SCREEN(screenTitleId = R.string.main_screen_title, screenIconId = R.drawable.home),
    SETTINGS_SCREEN(screenTitleId = R.string.settings_screen_title, screenIconId = R.drawable.settings)
}