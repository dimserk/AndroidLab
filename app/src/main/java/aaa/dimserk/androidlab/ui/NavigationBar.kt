package aaa.dimserk.androidlab.ui

import aaa.dimserk.androidlab.NavRoute
import aaa.dimserk.androidlab.ui.theme.AndroidLabTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NavigationBar(
    routes: List<NavRoute>,
    activeRoute: NavRoute,
    navigateToRoute: (NavRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            routes.forEach {
                val buttonColors = if (it == activeRoute) {
                    IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    IconButtonDefaults.iconButtonColors()
                }

                IconButton(
                    onClick = { navigateToRoute(it) },
                    colors = buttonColors,
                ) {
                    Icon(
                        painter = painterResource(it.screenIconId),
                        contentDescription = null,
                        modifier = modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

@Preview(widthDp = 300)
@Composable
private fun NavigationBarPreview() {
    AndroidLabTheme {
        NavigationBar(
            routes = listOf(
                NavRoute.MAIN_SCREEN,
                NavRoute.SETTINGS_SCREEN
            ),
            activeRoute = NavRoute.SETTINGS_SCREEN,
            navigateToRoute = { },
        )
    }
}