package aaa.dimserk.androidlab.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen() {
    Column {
        Button(
            onClick = {
                //viewModel.changeCurrentNavRoute(NavRoutes.ABOUT_SCREEN)
            }
        ) {
            Text(text = "To about")
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}