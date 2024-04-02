package uk.ac.tees.mad.w9617329

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9617329.screens.SplashScreen
import uk.ac.tees.mad.w9617329.screens.SplashScreenDestination
import uk.ac.tees.mad.w9617329.screens.authentication.AuthDestination
import uk.ac.tees.mad.w9617329.screens.authentication.AuthMethodScreen
import uk.ac.tees.mad.w9617329.screens.authentication.LoginDestination
import uk.ac.tees.mad.w9617329.screens.authentication.LoginScreen
import uk.ac.tees.mad.w9617329.screens.authentication.SignUpDestination
import uk.ac.tees.mad.w9617329.screens.authentication.SignUpScreen
import uk.ac.tees.mad.w9617329.screens.explorescreen.ExploreDestination
import uk.ac.tees.mad.w9617329.screens.explorescreen.ExploreScreen

@Composable
fun BloodConnectApp() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val firebase = FirebaseAuth.getInstance()
    val initialDestination =
        if (firebase.currentUser != null) {
            ExploreDestination.routeName
        } else {
            AuthDestination.routeName
        }

    NavHost(navController = navController, startDestination = SplashScreenDestination.routeName) {
        composable(SplashScreenDestination.routeName) {
            SplashScreen(
                onFinish = {
                    scope.launch(Dispatchers.Main) {
                        navController.popBackStack()
                        navController.navigate(initialDestination)
                    }
                }
            )
        }

        composable(ExploreDestination.routeName) {
            ExploreScreen(navController = navController, signOut = {
                scope.launch {
                    firebase.signOut()
                    Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
                    navController.navigate(LoginDestination.routeName)
                }
            })
        }

        composable(AuthDestination.routeName) {
            AuthMethodScreen(
                onLogin = { navController.navigate(LoginDestination.routeName) },
                onSignUp = { navController.navigate(SignUpDestination.routeName) })
        }

        composable(LoginDestination.routeName) {
            LoginScreen(
                onLoginSuccess = {
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(ExploreDestination.routeName)
                },
                onSignUp = { navController.navigate(SignUpDestination.routeName) },
                onNavigateUp = { navController.navigateUp() })
        }
        composable(SignUpDestination.routeName) {
            SignUpScreen(
                onLogin = { navController.navigate(LoginDestination.routeName) },
                onSignUpSuccess = {
                    Toast.makeText(context, "Sign up Successful", Toast.LENGTH_SHORT).show()
                    navController.navigate(ExploreDestination.routeName)
                },
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }
    }
}