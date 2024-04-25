package uk.ac.tees.mad.w9617329

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme

class DonarDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val imageUrl = intent.getStringExtra("image_url") ?: ""
        val name = intent.getStringExtra("name") ?: "N/A"
        val email = intent.getStringExtra("email") ?: "N/A"
        val bloodType = intent.getStringExtra("bloodType") ?: "N/A"
        val phone = intent.getStringExtra("phone") ?: "N/A"
        val latitude = intent.getDoubleExtra("lat", 0.0)
        val longitude = intent.getDoubleExtra("long", 0.0)

        val donor = DonarModel(
            name,
            phone,
            imageUrl,
             bloodType,
            email,
            latitude,
             longitude,
            true
        )

        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ProfileScreen(donor, onEditProfileClick = {

                    },this@DonarDetailsScreen)
                }
            }
        }
    }

    @Composable
    fun ProfileScreen(user: DonarModel, onEditProfileClick: () -> Unit,context: Context) {
        val localContext = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Donor Profile",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            ProfilePicture(user = user)
            Spacer(modifier = Modifier.height(16.dp))
            UserInfo(user = user)
            Spacer(modifier = Modifier.height(8.dp))
            EditProfileButton(onClick = {
                val intent = Intent(context, LocateDonarActivity::class.java)
                intent.putExtra("name", user.name)
                intent.putExtra("lat", user.lat)
                intent.putExtra("long", user.long)
                startActivity(intent)
            },"Locate Donar")

        }
    }

    @Composable
    fun ProfilePicture(user: DonarModel) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color(android.graphics.Color.parseColor("#FFEBEE")), // A lighter red
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(user.image_url),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Red, CircleShape),
                contentScale = ContentScale.Crop

            )
        }
    }

    @Composable
    fun UserInfo(user: DonarModel) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            ProfileInfoItem("Name", user.name)
            ProfileInfoItem("Email", user.email)
            ProfileInfoItem("Phone", user.phone)
            ProfileInfoItem("Blood Type", user.bloodType)
        }
    }

    @Composable
    fun EditProfileButton(onClick: () -> Unit,text:String) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text, color = Color.White)
        }
    }

    @Composable
    fun ProfileInfoItem(label: String, value: String) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.subtitle2,
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = value,
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )
        }
    }

}
