package uk.ac.tees.mad.w9617329

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class welcomeScreen: Fragment(R.layout.welcome_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<ComposeView>(R.id.compose_view).setContent {
            WelcomeScreen()
            object : Thread() {
                override fun run() {
                    try {
                        sleep(1000)
                    } catch (e: Exception) {
                    } finally {
                        if (isUserLoggedIn()) {
                            val intent = Intent(requireContext(), HomeActivity2::class.java)
                            startActivity(intent)
                            activity?.finish()
                        } else {
                            val intent = Intent(requireContext(), AuthenticationScreenActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                    }
                }
            }.start()
        }
    }

    fun isUserLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }

    @Composable
    fun WelcomeScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)  // Increased padding for a better layout
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blooddonation), // Replace with your logo resource
                    contentDescription = "Logo",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)  // Increased size for better visibility
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))  // Adjusted spacing for better visual separation

                Text(
                    text = "Welcome to Blood Donate App",
                    style = MaterialTheme.typography.h6,
                    color = Color(android.graphics.Color.parseColor("#670E10"))
                )
            }
        }
    }
}