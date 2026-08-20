package com.amos_tech_code.kmp_memocore.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amos_tech_code.kmp_memocore.data.cache.DataStoreManager
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpScreen(
    navController: NavController,
    dataStoreManager: DataStoreManager
) {

    val viewModel = viewModel { SignUpViewModel(dataStoreManager) }
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.navigationFlow.collectLatest {
            when(it) {
                is AuthNavigation.NavigateToHome -> {
                    navController.previousBackStackEntry?.savedStateHandle?.set("email", email)
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
        ) {
            when (uiState) {
                is AuthState.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Sign Up", fontSize = 22.sp)
                        Spacer(modifier = Modifier.size(16.dp))
                        OutlinedTextField(
                            email,
                            onValueChange = {
                                viewModel.updateEmail(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Email")
                            },
                            label = {
                                Text("Email")
                            })

                        Spacer(modifier = Modifier.size(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                viewModel.updatePassword(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Password")
                            },
                            label = {
                                Text("Password")
                            })

                        Spacer(modifier = Modifier.size(16.dp))

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = {
                                viewModel.updateConfirmPassword(it)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("Confirm Password")
                            },
                            label = {
                                Text("Confirm Password")
                            })

                        Spacer(modifier = Modifier.size(16.dp))

                        TextButton(
                            onClick = { navController.navigate("signin") }
                        ) {
                            Text("Already have an account? SignIn")
                        }

                        Spacer(modifier = Modifier.size(16.dp))

                        Button(
                            onClick = { viewModel.signUp() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Submit")
                        }

                    }
                }

                is AuthState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text("Loading...")
                    }
                }

                is AuthState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val email = (uiState as AuthState.Success).response.email
                        Text(("Success: $email"))
                        Button(
                            onClick = { viewModel.onSuccessClick(email) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ok")
                        }
                    }

                }

                is AuthState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(("Error: ${(uiState as AuthState.Error).message}"))

                        Button(
                            onClick = { viewModel.onErrorClick() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ok")
                        }
                    }
                }
            }
        }
    }
}
