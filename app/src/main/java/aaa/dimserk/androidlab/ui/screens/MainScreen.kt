package aaa.dimserk.androidlab.ui.screens

import aaa.dimserk.androidlab.Logger
import aaa.dimserk.androidlab.R
import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun MainScreen(
    islongTaskRunning: LiveData<Boolean>,
    onStartLongTask: () -> Unit,
    onShowSnackbar: (String?) -> Unit,
    onShowSnackbarWithAction: (String?) -> Unit,
    changebleText: MutableLiveData<String>,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val longTaskRunning by islongTaskRunning.observeAsState(false)
    val changableString by changebleText.observeAsState("")

    val localContext = LocalContext.current

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

    Column {
        Button(
            enabled = !longTaskRunning,
            modifier = modifier,
            onClick = onStartLongTask
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
                onClick = { onShowSnackbar(null) }
            ) {
                Text(
                    text = stringResource(id = R.string.show_snackbar),
                    modifier = modifier,
                )
            }

            Button(
                modifier = modifier,
                onClick = { onShowSnackbarWithAction(null) }
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

        TextField(
            value = changableString,
            onValueChange = { changebleText.value = it },
            trailingIcon = {
                IconButton(
                    onClick = { onShowSnackbar(changableString) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    AndroidLabTheme {
        MainScreen(
            islongTaskRunning = MutableLiveData(false),
            onStartLongTask = { },
            onShowSnackbar = { },
            onShowSnackbarWithAction = { },
            changebleText = MutableLiveData("bar")
        )
    }
}