package uk.ac.tees.mad.w9617329

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth


class AuthenticationScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BloodConnectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AuthenticationScreen()
                }
            }
        }
    }

    @Composable
    fun AuthenticationScreen() {
        val redLabelTextFieldStyle = TextStyle(color = Color(android.graphics.Color.parseColor("#bd7e88")))

        val scaffoldState = rememberScaffoldState()
        // State for email and password fields
        val emailState = remember { mutableStateOf(TextFieldValue()) }
        val passwordState = remember { mutableStateOf(TextFieldValue()) }

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
                    painter = painterResource(id = R.drawable.blooddonation), // Ensure you have a blood drop icon
                    contentDescription = "Blood Donation",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(40.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Log In",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red, // Emphasis color
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "Welcome back! Please log in to continue.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp)
                )

                // Email TextInput
                OutlinedTextField(

                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text("Email", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.email), // Custom email icon
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Password TextInput
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password", style = redLabelTextFieldStyle) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.lock), // Custom lock icon
                            contentDescription = "Password",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.White,
                        focusedBorderColor = Color.Red,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                // Log In Button
                Button(
                    onClick = {
                        if (emailState.value.text.isNotEmpty() && passwordState.value.text.isNotEmpty()) {
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailState.value.text, passwordState.value.text)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val intent = Intent(this@AuthenticationScreenActivity, HomeActivity2::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        // Login failed, get the error message
                                        val errorMessage = task.exception?.message
                                        if (errorMessage != null) {
                                            Toast.makeText(this@AuthenticationScreenActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }.addOnFailureListener { exception ->
                                    Toast.makeText(this@AuthenticationScreenActivity, exception.toString(), Toast.LENGTH_SHORT)
                                        .show()
                                }
                        } else {
                            Toast.makeText(this@AuthenticationScreenActivity, "Username or password is incorrect", Toast.LENGTH_SHORT)
                                .show()
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Log In",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }

                // Forgot Password Text
                Text(
                    text = "Forgot your password?",
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 16.dp)
                        .clickable(onClick = {
                            Toast.makeText(this@AuthenticationScreenActivity,"This Functionality is Coming Soon",Toast.LENGTH_SHORT).show()
                        })
                )

                // Sign Up Prompt
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clickable{
                            val intent = Intent(this@AuthenticationScreenActivity, CreateAccountActivity::class.java)
                            startActivity(intent)
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Don’t have an account? ",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Sign Up",
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}
