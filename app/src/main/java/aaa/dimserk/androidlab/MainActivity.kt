package aaa.dimserk.androidlab

import aaa.dimserk.androidlab.ui.MainScreen
import aaa.dimserk.androidlab.ui.PinCodeScreen
import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            AndroidLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = NavRoutes.MainScreen
                    ) {
                        composable(route = NavRoutes.MainScreen) {
                            MainScreen()
                        }

                        composable(route = NavRoutes.PinCodeScreen) {
                            PinCodeScreen()
                        }
                    }
                }
            }
        }
    }
}