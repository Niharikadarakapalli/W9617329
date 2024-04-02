package uk.ac.tees.mad.w9617329.screens.authentication

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9617329.R
import uk.ac.tees.mad.w9617329.navigations.NavigationDestination
import uk.ac.tees.mad.w9617329.repository.GoogleAuthClient
import uk.ac.tees.mad.w9617329.screens.authentication.viewmodel.AuthenticationViewModel
import uk.ac.tees.mad.w9617329.ui.theme.PrimaryRed

object SignUpDestination : NavigationDestination {
    override val routeName: String
        get() = "signup"
}

@Composable
fun SignUpScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onLogin: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val signUpstate = viewModel.signUpState.collectAsState(initial = null)
    val signInState = viewModel.state.collectAsState().value
    val signUpUiState = viewModel.signUpUiState.collectAsState().value
    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val googleAuthUiClient by lazy {
        GoogleAuthClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInWithGoogleResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = signInState.isSignInSuccessful) {
        if (signInState.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sign in successful",
                Toast.LENGTH_LONG
            ).show()
            val user = googleAuthUiClient.getSignedInUser()
            if (user != null) {
                viewModel.saveUserInFirestore(user)
            }
            viewModel.resetState()
            onSignUpSuccess()
        }
    }
    LaunchedEffect(key1 = signUpstate.value?.isSuccess) {
        scope.launch {
            if (signUpstate.value?.isSuccess?.isNotEmpty() == true) {
                onSignUpSuccess()
            }
        }
    }

    LaunchedEffect(key1 = signUpstate.value?.isError) {
        scope.launch {
            if (signUpstate.value?.isError?.isNotEmpty() == true) {
                val error = signUpstate.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
            }
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onNavigateUp()
                    }
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Register",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryRed
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "Create your new account", fontSize = 22.sp)
        }
        Spacer(modifier = Modifier.height(80.dp))
        Column {

            OutlinedTextField(
                value = signUpUiState.name,
                onValueChange = {
                    viewModel.updateSignUpState(signUpUiState.copy(name = it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Full Name")
                },
                maxLines = 1,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                shape = RoundedCornerShape(24.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.PersonOutline, contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = signUpUiState.email,
                onValueChange = {
                    viewModel.updateSignUpState(signUpUiState.copy(email = it))
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Email")
                },
                maxLines = 1,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }),
                shape = RoundedCornerShape(24.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = "")
                }

            )

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value = signUpUiState.password,
                onValueChange = {
                    viewModel.updateSignUpState(signUpUiState.copy(password = it))
                },

                modifier = Modifier.fillMaxWidth(),

                maxLines = 1,
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Outlined.Visibility
                    else Icons.Outlined.VisibilityOff

                    val description =
                        if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            description,
                        )
                    }
                },
                placeholder = {
                    Text(text = "Password")
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus()
                }),
                shape = RoundedCornerShape(24.dp),
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = "")
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    viewModel.registerUser(
                        username = signUpUiState.name,
                        email = signUpUiState.email,
                        password = signUpUiState.password,
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryRed
                )
            ) {
                if (signUpstate.value?.isLoading == true) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
                } else {
                    Text(text = "Sign up", fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(PrimaryRed)
                )
                Text(
                    text = "or Continue with",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    color = Color.Gray
                )
                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(PrimaryRed)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                IconButton(
                    onClick = {
                        scope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest
                                    .Builder(
                                        signInIntentSender ?: return@launch
                                    )
                                    .build()
                            )
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Sign with google",
                        modifier = Modifier.size(32.dp),
                        tint = PrimaryRed
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Already have an account?")
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "Login",
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryRed,
                    modifier = Modifier.clickable {
                        onLogin()
                    }
                )
            }
            Spacer(modifier = Modifier.height(50.dp))

        }
    }
}