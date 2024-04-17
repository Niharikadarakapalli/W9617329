package uk.ac.tees.mad.w9617329

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme


import uk.ac.tees.mad.w9617329.ui.theme.BloodConnectTheme

import android.view.View
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore


class CreateAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CreateAccount()
                }
            }
        }
    }

    @Composable
    fun CreateAccount() {
        val scaffoldState = rememberScaffoldState()
        var fullName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var bloodType by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var confirmPassword by remember { mutableStateOf("") }

        val redLabelTextFieldStyle = TextStyle(color = Color(android.graphics.Color.parseColor("#bd7e88")))

        Scaffold(
            scaffoldState = scaffoldState,
            backgroundColor = Color.White,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.blooddonation), // Ensure this icon is correctly linked
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .padding(40.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Sign Up",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Join us to save lives!",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.user),
                            contentDescription = "Full Name",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.email),
                            contentDescription = "Email",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.phone),
                            contentDescription = "Phone",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = bloodType,
                    onValueChange = { bloodType = it },
                    label = { Text("Blood Type", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.blooddonation),
                            contentDescription = "Blood Type",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock),
                            contentDescription = "Password",
                            tint = Color.Red,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if(email.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty()  && fullName.isNotEmpty() && bloodType.isNotEmpty()){
                            FirebaseAuth
                                .getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        CreateUserDetailsInFirebase(fullName,bloodType,phone)
                                        val intent = Intent(this@CreateAccountActivity, HomeActivity2::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Login failed, get the error message
                                        val errorMessage = task.exception?.message
                                        if (errorMessage != null) {
                                            Toast.makeText(this@CreateAccountActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    Toast.makeText(this@CreateAccountActivity, exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                        }else{
                            Toast.makeText(this@CreateAccountActivity, "Username or password is incorrect", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                Text(
                    text = "Already have an account? Log In",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .clickable(onClick = { /* Navigate to login screen */ })
                )
            }
        }
    }


    private fun CreateUserDetailsInFirebase(name2:String,bloodType:String,phone:String){
        var user = FirebaseAuth.getInstance().getCurrentUser()
        if (user != null) {
            val uid = user.uid
            val name = user.email.toString()
            val atIndex = name.indexOf('@')

            val userDoc = DonarModel(
                name = name2,
                phone = phone,
                image_url = "https://firebasestorage.googleapis.com/v0/b/chefhub-77f55.appspot.com/o/profile_photo%2Fpexels-andrea-piacquadio-733872.jpg?alt=media&token=6f87efa2-dcf6-4b3c-bf54-ccc4690721d0",
                bloodType = bloodType,
                email = user.email.toString()
            )
            FirebaseFirestore.getInstance().collection("donars").document(uid).set(userDoc).addOnCompleteListener {
                task ->
                if (task.isSuccessful) {

                }else{
                    val errorMessage = task.exception?.message
                    if (errorMessage != null) {
                        Toast.makeText(this@CreateAccountActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}

