package uk.ac.tees.mad.w9617329

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HomeActivity2 : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Navigation()
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    requestLocation()
                } else {
                    // Permission denied, handle accordingly
                }
            }

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    private fun requestLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if(uid != null){
                        FirebaseFirestore.getInstance().collection("donars").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val userData = document.toObject(DonarModel::class.java)
                                    if (userData != null) {
                                        // Update the user data
                                        val user = DonarModel(
                                            userData.name,
                                            userData.phone,
                                            userData.image_url,
                                            userData.bloodType,
                                            userData.email,
                                            latitude,
                                            longitude,
                                            userData.isdonar
                                        )
                                        FirebaseFirestore.getInstance().collection("donars").document(uid).set(user)
                                    }
                                } else {
                                    // The document does not exist
                                }
                            }


                    }

                }
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    @Composable
    fun Navigation() {

        val navController = rememberNavController()

        val items = listOf(
            NavController.Home,
            NavController.Notifications,
            NavController.Account
        )
//    val profilePicture: Painter = painterResource(id = R.drawable.profile_picture)
//    val name = "Hi, " +"John Doe"


        Scaffold(
            bottomBar = {
                BottomNavigation(backgroundColor = MaterialTheme.colors.background) {

                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route


                    items.forEach {
                        BottomNavigationItem(selected = currentRoute == it.route,
                            label = {
                                Text(
                                    text = it.label,
                                    color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = it.icons, contentDescription = null,
                                    tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                                )

                            },

                            onClick = {
                                if(currentRoute!=it.route){

                                    navController.graph?.startDestinationRoute?.let {
                                        navController.popBackStack(it,true)
                                    }

                                    navController.navigate(it.route){
                                        launchSingleTop = true
                                    }

                                }

                            })

                    }

                }


            }) {

            NavigationController(navController = navController)

        }

    }


    @Composable
    fun NavigationController(navController: NavHostController) {
        NavHost(navController = navController, startDestination = NavController.Home.route) {

            composable(NavController.Home.route) {

            }

            composable(NavController.Notifications.route) {

            }

            composable(NavController.Account.route) {
                profileInitializer(this@HomeActivity2)
            }

        }
    }
    @Composable
    fun profileInitializer(context: Context){
        var donor by remember { mutableStateOf(DonarModel(
            name = "John Doe",
            email = "john.doe@example.com",
            phone = "+1 123-456-7890",
            image_url = "https://firebasestorage.googleapis.com/v0/b/blood-connect-6b4d0.appspot.com/o/profile.jpg?alt=media&token=9ff85cf7-078e-45f4-80d3-fd04fdd9fb5b",
            bloodType = "A+",
            lat = 0.0,
            long = 0.0,
            isdonar = false
        ))}

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        LaunchedEffect(uid) {
            if (uid != null) {
                FirebaseFirestore.getInstance().collection("donars").document(uid).get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            val donorData = document.toObject(DonarModel::class.java)
                            if (donorData != null) {
                                donor = donorData
                            }
                        }
                    }
            }
        }

        ProfileScreen(user = donor, onEditProfileClick = {},context)
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
            var is_donar = user.isdonar
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Implement navigation back action */ }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Red)
                    }
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
                val intent = Intent(context, ProfileEditorActivity::class.java)
                localContext.startActivity(intent)
                (context as? Activity)?.finish()
            },"Manage Profile")
            if(is_donar == false){
                EditProfileButton(onClick = {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    if (uid != null) {
                        FirebaseFirestore.getInstance().collection("donars").document(uid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val userData = document.toObject(DonarModel::class.java)
                                    if (userData != null) {
                                        // Update the user data
                                        val userrr = DonarModel(
                                            userData.name,
                                            userData.phone,
                                            userData.image_url,
                                            userData.bloodType,
                                            userData.email,
                                            userData.lat,
                                            userData.long,
                                            true
                                        )
                                        is_donar = true
                                        FirebaseFirestore.getInstance().collection("donars").document(uid).set(userrr)
                                        Toast.makeText(this@HomeActivity2,"Now, You are a Donar",Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    // The document does not exist
                                }
                            }
                    }

                },"Be a Donar")
            }
            EditProfileButton(onClick = {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, AuthenticationScreenActivity::class.java)
                localContext.startActivity(intent)
                (context as? Activity)?.finish()
            },"Logout")
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

