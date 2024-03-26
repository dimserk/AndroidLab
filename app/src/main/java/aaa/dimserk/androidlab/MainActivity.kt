package aaa.dimserk.androidlab

import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidLabTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    viewModel: UIViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var longTaskEnabled by remember { mutableStateOf(true) }
    var progressCardVisible by remember { mutableStateOf(false) }

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
                        Logger.verbose ("Long task started")
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
                            message = context.getString(R.string.snackbar_massage),
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
                                message = context.getString(R.string.snackbar_massage),
                                actionLabel = context.getString(R.string.snackbar_action_label),
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
        }
    }
}