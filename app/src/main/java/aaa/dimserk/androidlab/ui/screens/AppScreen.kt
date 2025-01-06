package aaa.dimserk.androidlab.ui.screens

import aaa.dimserk.androidlab.Logger
import aaa.dimserk.androidlab.NavRoute
import aaa.dimserk.androidlab.R
import aaa.dimserk.androidlab.UIViewModel
import aaa.dimserk.androidlab.ui.NavigationBar
import aaa.dimserk.androidlab.ui.ProgressCard
import aaa.dimserk.androidlab.ui.TopBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    viewModel: UIViewModel = viewModel(),
) {
    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    fun showSnackbar(message: String?) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message ?: localContext.getString(R.string.snackbar_massage),
                withDismissAction = true
            )
        }
    }

    fun showSnackbarWithAction(message: String?) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message ?: localContext.getString(R.string.snackbar_massage),
                actionLabel = localContext.getString(R.string.snackbar_action_label),
                withDismissAction = true,
                duration = SnackbarDuration.Long
            )

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    Logger.verbose("Action perfomed")
                }

                SnackbarResult.Dismissed -> {
                    Logger.verbose("Action dismissed")
                }
            }
        }
    }

    var progressCardVisible by remember { mutableStateOf(false) }

    if (progressCardVisible) {
        ProgressCard(
            onDissmiss = { progressCardVisible = false },
            modifier = modifier,
        )
    }

    val routes = listOf(
        NavRoute.MAIN_SCREEN,
        NavRoute.SETTINGS_SCREEN
    )

    val backStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = NavRoute.valueOf(backStackEntry?.destination?.route ?: NavRoute.MAIN_SCREEN.name)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopBar(
                currentRoute = currentRoute,
                canNavigateBack = navHostController.previousBackStackEntry != null,
                navigateBack = { navHostController.navigateUp() }
            )
        },
        bottomBar = {
            NavigationBar(
                routes = routes,
                activeRoute = currentRoute,
                navigateToRoute = {
                    navHostController.navigate(it.name)
                },
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = NavRoute.MAIN_SCREEN.name,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = NavRoute.MAIN_SCREEN.name) {
                MainScreen(
                    islongTaskRunning = viewModel.isLongTaskRunning,
                    onStartLongTask = {
                        scope.launch {
                            progressCardVisible = true
                            viewModel.longTask()
                            progressCardVisible = false
                        }
                    },
                    onShowSnackbar = { showSnackbar(it) },
                    onShowSnackbarWithAction = { showSnackbarWithAction(it) },
                    changebleText = viewModel.changableString
                )
            }

            composable(route = NavRoute.SETTINGS_SCREEN.name) {
                SettingsScreen()
            }
        }
    }
}