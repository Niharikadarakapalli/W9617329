package uk.ac.tees.mad.w9617329

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9617329.screens.SplashScreen
import uk.ac.tees.mad.w9617329.screens.SplashScreenDestination
import uk.ac.tees.mad.w9617329.screens.explorescreen.ExploreDestination
import uk.ac.tees.mad.w9617329.screens.explorescreen.ExploreScreen

@Composable
fun BloodConnectApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable(SplashScreenDestination.routeName) {
            SplashScreen(navController = (navController))
        }
        composable(ExploreDestination.routeName) {
            ExploreScreen(navController = navController)
        }
    }
}