package uk.ac.tees.mad.w9617329.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9617329.R
import uk.ac.tees.mad.w9617329.components.LoaderAnimation
import uk.ac.tees.mad.w9617329.navigations.NavigationDestination
import uk.ac.tees.mad.w9617329.screens.explorescreen.ExploreDestination
import uk.ac.tees.mad.w9617329.ui.theme.PrimaryRed


object SplashScreenDestination : NavigationDestination {
    override val routeName = "splash"
}

@Composable
fun SplashScreen(navController: NavHostController) {

    val anim = remember {
        Animatable(0f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(240.dp))
            LoaderAnimation(
                modifier = Modifier
                    .size(200.dp),
                anim = R.raw.blood
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.5f))
            Text(
                text = "Blood Connect",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryRed,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(anim.value)
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Every drop counts, every donation saves a life. Be a hero today - donate blood.",
                fontSize = 18.sp,
                color = PrimaryRed,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(anim.value)
            )
            Spacer(modifier = Modifier.height(25.dp))
        }

    }
    LaunchedEffect(key1 = true) {
        anim.animateTo(1f, animationSpec = tween(1500))
        delay(2000L)
        launch(Dispatchers.Main) {
            navController.popBackStack()
            navController.navigate(ExploreDestination.routeName)
        }
    }
}