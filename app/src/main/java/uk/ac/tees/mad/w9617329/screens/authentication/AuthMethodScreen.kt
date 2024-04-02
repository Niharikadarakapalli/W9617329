package uk.ac.tees.mad.w9617329.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.w9617329.R
import uk.ac.tees.mad.w9617329.navigations.NavigationDestination
import uk.ac.tees.mad.w9617329.ui.theme.PrimaryRed

object AuthDestination: NavigationDestination{
    override val routeName: String
        get() = "auth_method"
}

@Composable
fun AuthMethodScreen(
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(24.dp)) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blood_donation),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Blood can't be produced,",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
                Text(
                    text = "Blood can only be donated.",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }

        }
        Column(
            Modifier
                .padding(36.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onLogin,
                colors = ButtonDefaults.buttonColors(PrimaryRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Text(text = "Login", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Create an account",
                color = PrimaryRed,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    onSignUp()
                }
            )
        }

    }
}