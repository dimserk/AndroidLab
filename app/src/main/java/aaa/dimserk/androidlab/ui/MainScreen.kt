package aaa.dimserk.androidlab.ui

import aaa.dimserk.androidlab.Logger
import aaa.dimserk.androidlab.NavRoutes
import aaa.dimserk.androidlab.R
import aaa.dimserk.androidlab.UIViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: UIViewModel = viewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val localContext = LocalContext.current

    val navHost = rememberNavController()
    val currentRoute by viewModel.currentNavRoute.observeAsState(NavRoutes.ABOUT_SCREEN)

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var longTaskEnabled by remember { mutableStateOf(true) }
    var progressCardVisible by remember { mutableStateOf(false) }

    val permissionText by remember {
        val text = if (ContextCompat.checkSelfPermission(localContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            localContext.getString(R.string.permission_granted)
        } else {
            localContext.getString(R.string.permission_not_granted)
        }

        mutableStateOf(text)
    }

    var requaredPermissions by remember { mutableStateOf<List<String>>(listOf()) }

    fun foo() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        requaredPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(localContext, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    val permissionRequestLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Logger.verbose("Permission was granted")
            foo()
        } else {
            Logger.verbose("Permission wasn`t granted")
        }
    }

    // Safely update the current lambdas when a new one is provided
    val currentOnStart by rememberUpdatedState {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )

        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.P) {
            permissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        requaredPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(localContext, it) != PackageManager.PERMISSION_GRANTED
        }
    }

    // If `lifecycleOwner` changes, dispose and reset the effect
    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        // for sending analytics events
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                currentOnStart()
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // When the effect leaves the Composition, remove the observer
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (progressCardVisible) {
        ProgressCard(
            onDissmiss = { progressCardVisible = false },
            modifier = modifier,
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = modifier,
    ) { innerPadding ->
        NavHost(navController = navHost, startDestination = currentRoute) {
            composable(route = NavRoutes.ABOUT_SCREEN) {
                Column(
                    modifier = modifier.padding(innerPadding),
                ) {
                    Row(
                        modifier = modifier,
                    ) {
                        Text(
                            text = viewModel.getHelloMessage(),
                            modifier = modifier,
                        )
                    }

                    Button(
                        enabled = longTaskEnabled,
                        modifier = modifier,
                        onClick = {
                            scope.launch {
                                Logger.verbose("Long task started")
                                longTaskEnabled = false
                                progressCardVisible = true
                                viewModel.longTask()
                                progressCardVisible = false
                                Logger.verbose("Long task done")
                                longTaskEnabled = true
                            }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.start_long_task_button),
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.snackbars_label),
                        modifier = modifier,
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = modifier.fillMaxWidth(),
                    ) {
                        Button(
                            modifier = modifier,
                            onClick = {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = localContext.getString(R.string.snackbar_massage),
                                        withDismissAction = true
                                    )
                                }
                            }) {
                            Text(
                                text = stringResource(id = R.string.show_snackbar),
                                modifier = modifier,
                            )
                        }

                        Button(
                            modifier = modifier,
                            onClick = {
                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = localContext.getString(R.string.snackbar_massage),
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
                        ) {
                            Text(
                                text = stringResource(id = R.string.show_action_snackbar),
                                modifier = modifier,
                            )
                        }
                    }

                    // Permissions
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = permissionText,
                        modifier = modifier
                    )

                    Card(
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        LazyColumn {
                            items(requaredPermissions) { permission ->
                                Button(
                                    modifier = modifier,
                                    onClick = {
                                        permissionRequestLauncher.launch(permission)
                                    }
                                ) {
                                    Text(
                                        text = permission,
                                        modifier = modifier
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = {
                            viewModel.changeCurrentNavRoute(NavRoutes.SETTINGS_SCREEN)
                        }
                    ) {
                        Text(text = "To settings")
                    }
                }
            }

            composable(route = NavRoutes.SETTINGS_SCREEN) {
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Button(
                        onClick = {
                            viewModel.changeCurrentNavRoute(NavRoutes.ABOUT_SCREEN)
                        }
                    ) {
                        Text(text = "To about")
                    }
                }
            }
        }
    }
}