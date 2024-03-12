package aaa.dimserk.androidlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
import androidx.compose.foundation.layout.Row
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //@Inject
    //lateinit var repo: SomeRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidLabTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("repo.foo()")
                }
            }
        }
    }
}

@Composable
fun Greeting(
    fooText: String,
    modifier: Modifier = Modifier,
    viewModel: UIViewModel = viewModel()
) {
    Row {
        Text(
            text = viewModel.getHelloMessage(),
            modifier = modifier
        )

        Text(
            text = fooText
        )
    }
}

/*@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidLabTheme {
        Greeting()
    }
}*/