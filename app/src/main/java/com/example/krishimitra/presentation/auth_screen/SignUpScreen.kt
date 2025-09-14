package com.example.krishimitra.presentation.auth_screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.krishimitra.R
import com.example.krishimitra.data.repo.location_manager.LocationManager
import com.example.krishimitra.domain.farmer_data.UserDataModel
import com.example.krishimitra.presentation.components.CustomOutlinedTextField
import com.example.krishimitra.utils.AskLocationPermission
import com.example.krishimitra.utils.isLocationPermissionGranted


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    moveToSignInScreen: () -> Unit,
    signUp: (UserDataModel) -> Unit,
    authState: AuthState,
    context: Context,
    getLocation: () -> Unit,
    enableLocationPermission: ()-> Unit
) {

    var mobileNo by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var state by rememberSaveable { mutableStateOf("") }
    var district by rememberSaveable { mutableStateOf("") }
    var village by rememberSaveable { mutableStateOf("") }
    var pinCode by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    val gmailRegex = Regex("^[a-zA-Z0-9._%+-]+@gmail\\.com$")

    var requestPermission by remember { mutableStateOf(false) }

    LaunchedEffect(authState.location) {
        authState.location?.let {
            state = it.state
            village = it.village
            pinCode = it.pinCode
            district = it.district
        }
        Log.d("Location",authState.location.toString())
    }
    if (requestPermission) {
        AskLocationPermission {
            if (it) getLocation()
            else {
                requestPermission=false
            }
        }
    }



    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(640.dp)
            .background(color = Color.White.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.signup),
            style = MaterialTheme.typography.titleLarge,
        )

        CustomOutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = stringResource(id = R.string.name),
            supportingText = ""
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomOutlinedTextField(
                value = village,
                onValueChange = { village = it },
                label = stringResource(id = R.string.village),
                supportingText = "",
                modifier = Modifier.weight(2f)
            )
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .background(colorResource(id = R.color.grass_green), shape = RoundedCornerShape(4.dp)),

                onClick = {
                    if (!authState.isLocationLoading) {
                        if (isLocationPermissionGranted(context) && LocationManager.isLocationEnabled(context)) {
                            getLocation()
                        } else if(!LocationManager.isLocationEnabled(context)){
                            enableLocationPermission()
                        }
                            else{

                            requestPermission = true
                        }
                    }
                },
            ) {
                if (authState.isLocationLoading) {
                    CircularProgressIndicator()
                } else {
                    Icon(
                        imageVector = Icons.Default.LocationOn, contentDescription = "Location"
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            CustomOutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = stringResource(id = R.string.district),
                supportingText = "",
                modifier = Modifier.weight(1f)
            )
            CustomOutlinedTextField(
                value = state,
                onValueChange = { state = it },
                label = stringResource(id = R.string.state),
                supportingText = "",
                modifier = Modifier.weight(1f)

            )
        }

        OutlinedTextField(
            value = mobileNo,
            onValueChange = { mobileNo = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = stringResource(id = R.string.mobile_no)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colorResource(id = R.color.grass_green),
                focusedBorderColor = colorResource(id = R.color.grass_green),
                focusedLabelColor = colorResource(R.color.grass_green),
                unfocusedTextColor = colorResource(id = R.color.grass_green),
                cursorColor = colorResource(id = R.color.grass_green),
            ),
            leadingIcon = {
                Text(
                    text = "+91"
                )

            },
            supportingText = {
                if (mobileNo.length != 10 && mobileNo.isNotEmpty()) {
                    Text(
                        text = "Enter valid mobile no.", color = Color.Red
                    )
                }
            })
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = "Email"
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colorResource(id = R.color.grass_green),
                focusedBorderColor = colorResource(id = R.color.grass_green),
                focusedLabelColor = colorResource(R.color.grass_green),
                unfocusedTextColor = colorResource(id = R.color.grass_green),
                cursorColor = colorResource(id = R.color.grass_green),
            ),
            supportingText = {
                if (email.isNotEmpty() && !email.matches(gmailRegex)) {
                    Text(
                        text = "Enter a valid Gmail address", color = Color.Red
                    )
                }
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = pinCode,
                onValueChange = { pinCode = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp),
                label = {
                    Text(
                        text = stringResource(id = R.string.pin_no)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = colorResource(id = R.color.grass_green),
                    focusedBorderColor = colorResource(id = R.color.grass_green),
                    focusedLabelColor = colorResource(R.color.grass_green),
                    unfocusedTextColor = colorResource(id = R.color.grass_green),
                    cursorColor = colorResource(id = R.color.grass_green),
                ),
                supportingText = {
                    if (pinCode.length > 0 && pinCode.length != 6) Text(
                        text = "Invalid"
                    )
                })
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(
                    mask = '*'
                ),
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = "Password"
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = colorResource(id = R.color.grass_green),
                    focusedBorderColor = colorResource(id = R.color.grass_green),
                    focusedLabelColor = colorResource(R.color.grass_green),
                    unfocusedTextColor = colorResource(id = R.color.grass_green),
                    cursorColor = colorResource(id = R.color.grass_green),
                ),
                trailingIcon = {
                    IconButton(
                        onClick = { showPassword = !showPassword }) {
                        Icon(
                            painter = if (showPassword) painterResource(id = R.drawable.outline_visibility_off_24) else painterResource(
                                id = R.drawable.outline_visibility_24
                            ),
                            contentDescription = if (showPassword) "Hide password" else "Show password"
                        )

                    }
                },
                supportingText = {
                    if (password.length > 0 && password.length < 8) {
                        Text(
                            text = "Password must be 8 characters long", color = Color.Red

                        )
                    }
                }

            )

        }
        Button(
            modifier = Modifier.fillMaxWidth(), onClick = {
                if (name.isEmpty() || village.isEmpty() || district.isEmpty() || mobileNo.length != 10 || pinCode.length != 6 || password.length < 8 || !email.matches(
                        gmailRegex
                    )
                ) {
                    Toast.makeText(context, "Enter valid details", Toast.LENGTH_SHORT).show()
                } else if (authState.isSignLoading) {
                    Toast.makeText(context, "Wait...", Toast.LENGTH_SHORT).show()
                } else {
                    signUp(
                        UserDataModel(
                            name = name,
                            email = email,
                            password = password,
                            village = village,
                            district = district,
                            state = state,
                            mobileNo = mobileNo,
                            pinCode = pinCode
                        )
                    )

                }
            }, shape = RoundedCornerShape(4.dp), colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.grass_green)
            )
        ) {
            if (authState.isSignLoading) {
                CircularProgressIndicator(
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(id = R.string.signup),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.already_have_account),

                )
            TextButton(
                onClick = moveToSignInScreen
            ) {
                Text(
                    text = stringResource(id = R.string.login)
                )
            }
        }

    }
}
