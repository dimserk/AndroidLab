package aaa.dimserk.androidlab

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
    val scope = rememberCoroutineScope()

    var visible by remember { mutableStateOf(false) }

    if (visible) {
        ProgressCard(
            onDissmiss = { visible = false },
            onCancel = { }
        )
    }

    Column {
        Row {
            Text(
                text = viewModel.getHelloMessage(),
                modifier = modifier
            )
        }

        Button(onClick = {
            scope.launch {
                Log.d("TAG", "started")
                visible = true
                viewModel.longTask()
                visible = false
                Log.d("TAG", "done")
            }
        }) {
            Text(text = "Push me")
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidLabTheme {
        Greeting()
    }
}*/