package uk.ac.tees.mad.w9617329

import android.Manifest
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
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import java.io.BufferedReader
import java.io.ByteArrayOutputStream
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
                    var loading by remember { mutableStateOf(true) }

                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    var donor by remember { mutableStateOf(DonarModel())}


                    LaunchedEffect(uid) {
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("donars").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val donorData = document.toObject(DonarModel::class.java)
                                        if (donorData != null) {
                                            donor = donorData
                                            loading = false
                                        }
                                    }
                                }
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (loading) {
                            // Display a loading indicator while data is being fetched
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        } else {
                            // Data has been fetched, display the EditProfileScreen1
                            EditProfileScreen(user = donor, onNavigateBack = {
                                val intent = Intent(this@ProfileEditorActivity, HomeActivity2::class.java)
                                startActivity(intent)
                                finish()
                            }, onSaveProfile = {})
                        }
                    }

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
            SaveButton {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                val userData = DonarModel(editedUser.name,editedUser.phone,editedUser.image_url,editedUser.bloodType,editedUser.email,editedUser.lat,editedUser.long,editedUser.isdonar)
                if (uid != null) {
                    FirebaseFirestore.getInstance().collection("donars").document(uid).set(userData).addOnCompleteListener {
                        val intent = Intent(this@ProfileEditorActivity, HomeActivity2::class.java)
                        startActivity(intent)
                        finish()
                    }.addOnFailureListener {
                            exception ->
                        Toast.makeText(this@ProfileEditorActivity, exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
                onSaveProfile(editedUser)
            }
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
            label = { Text(label, style = redLabelTextFieldStyle) },
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
                .background(color = Color(android.graphics.Color.parseColor("#FFEBEE")), shape = RoundedCornerShape(16.dp))
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
                    .clickable{triggerCamera()}
            )
        }
    }


    private val REQUEST_IMAGE_CAPTURE = 1

    private fun triggerCamera() {
        if (checkPermissionsCamera()) {
            if (isCameraPermissionEnabled()) {
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (e: ActivityNotFoundException) {
                    // display error state to the user
                }}
        }
        else{
            Log.e("man log" , "requesting for camera permission")
            requestCameraPermission()
        }
    }

    private fun isCameraPermissionEnabled(): Boolean {
        val permission = Manifest.permission.CAMERA
        val result = ContextCompat.checkSelfPermission(this, permission)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 123


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                triggerCamera()
            } else {
                // Permission denied, handle accordingly (show a message, etc.)
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }


    private fun checkPermissionsCamera(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val storage = FirebaseStorage.getInstance()
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //binding.image.setImageBitmap(imageBitmap)
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val storageRef = storage.reference.child("profile_pictures").child(fileName)
            // Convert the Bitmap to a byte array
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // Upload the image to Firebase Storage
            val uploadTask = storageRef.putBytes(data)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()

                        val uid = FirebaseAuth.getInstance().getCurrentUser()?.getUid()
                        if (uid != null) {
                            FirebaseFirestore.getInstance().collection("donars").document(uid).get()
                                .addOnSuccessListener { document ->
                                    if (document.exists()) {
                                        val userData = document.toObject(DonarModel::class.java)
                                        if (userData != null) {
                                            // Update the user data
                                            val user = DonarModel(
                                                userData.name,
                                                userData.phone,
                                                downloadUrl,
                                                userData.bloodType,
                                                userData.email,
                                                userData.lat,
                                                userData.long,
                                                userData.isdonar
                                            )
                                            FirebaseFirestore.getInstance().collection("donars").document(uid).set(user)
                                        }
                                    } else {
                                        // The document does not exist
                                    }
                                }

                        }
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    } } else {
                    val exception = task.exception
                }
            }
        }
    }


}

