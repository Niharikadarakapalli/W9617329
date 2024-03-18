package uk.ac.tees.mad.w9617329.screens.explorescreen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import uk.ac.tees.mad.w9617329.navigations.NavigationDestination

@Composable
fun ExploreScreen(navController: NavHostController) {
    Text(text = "Explore screen")
}

object ExploreDestination : NavigationDestination {
    override val routeName = "explore"
    }