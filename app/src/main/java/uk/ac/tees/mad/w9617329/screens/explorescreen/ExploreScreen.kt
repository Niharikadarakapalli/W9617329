package uk.ac.tees.mad.w9617329.screens.explorescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.tees.mad.w9617329.navigations.NavigationDestination

@Composable
fun ExploreScreen(navController: NavHostController, signOut: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text(text = "Explore screen")
        Button(onClick = signOut) {
            Text(text = "Sign out")
        }
    }
}

object ExploreDestination : NavigationDestination {
    override val routeName = "explore"
}