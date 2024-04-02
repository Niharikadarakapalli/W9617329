package uk.ac.tees.mad.w9617329

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.w9617329.screens.authentication.LoginScreen
import uk.ac.tees.mad.w9617329.screens.authentication.SignUpScreen
import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BloodConnectApp()
                }
            }
        }
    }
}