package uk.ac.tees.mad.w9617329

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme
import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.remember

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import androidx.activity.compose.setContent
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProfileEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    var donor by remember { mutableStateOf(DonarModel(
                        name = "John Doe",
                        email = "john.doe@example.com",
                        phone = "+1 123-456-7890",
                        image_url = "https://firebasestorage.googleapis.com/v0/b/blood-connect-6b4d0.appspot.com/o/profile.jpg?alt=media&token=9ff85cf7-078e-45f4-80d3-fd04fdd9fb5b",
                        bloodType = "A+"
                    ))}


                    LaunchedEffect(uid) {
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("donors").document(uid).get()
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

                    EditProfileScreen(user = donor, onNavigateBack = {
                        val intent = Intent(this@ProfileEditorActivity, HomeActivity2::class.java)
                        startActivity(intent)
                        finish()
                    }, onSaveProfile = {})
                }
            }
        }
    }

    @Composable
    fun EditProfileScreen(
        user: DonarModel,
        onNavigateBack: () -> Unit,
        onSaveProfile: (DonarModel) -> Unit
    ) {
        var editedUser by remember { mutableStateOf(user.copy()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.h5,
                        color = Color.Red
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Red
                        )
                    }
                },
                backgroundColor = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            ProfilePicture(user.image_url)
            Spacer(modifier = Modifier.height(16.dp))
            UserInfo(user = editedUser,
                onNameChange = { editedUser = editedUser.copy(name = it) },
                onEmailChange = { editedUser = editedUser.copy(email = it) },
                onPhoneChange = { editedUser = editedUser.copy(phone = it) })
            Spacer(modifier = Modifier.height(16.dp))
            SaveButton { onSaveProfile(editedUser) }
        }
    }

    @Composable
    fun UserInfo(
        user: DonarModel,
        onNameChange: (String) -> Unit,
        onEmailChange: (String) -> Unit,
        onPhoneChange: (String) -> Unit
    ) {
        Column {
            EditTextField(label = "Name", text = user.name, onValueChange = onNameChange)
            EditTextField(label = "Email", text = user.email, onValueChange = onEmailChange)
            EditTextField(label = "Phone", text = user.phone, onValueChange = onPhoneChange)
        }
    }

    @Composable
    fun EditTextField(
        label: String,
        text: String,
        onValueChange: (String) -> Unit
    ) {
        val redLabelTextFieldStyle = TextStyle(color = Color.Red)
        OutlinedTextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(text, style = redLabelTextFieldStyle) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                backgroundColor = Color.White,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.LightGray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)

        )
    }

    @Composable
    fun SaveButton(onSaveClick: () -> Unit) {
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Save", color = Color.White)
        }
    }

    @Composable
    fun ProfilePicture(imageUrl: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(imageUrl),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Red, CircleShape)
            )
        }
    }

}

