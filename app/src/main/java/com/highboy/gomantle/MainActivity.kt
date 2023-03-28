package com.highboy.gomantle

import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Global
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.highboy.gomantle.ui.GomantleApp
import com.highboy.gomantle.ui.state.GomantleViewModel
import com.highboy.gomantle.ui.theme.GomantleTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update

class MainActivity : ComponentActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest

    private val viewModel = GomantleViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("activity", "GomantleMainActivity")

        createSharedPreferences()

        setContent {
            GomantleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    viewModel.loadSharedPreferences()
                    GomantleApp(
                        globalStateFlow = viewModel.globalStateFlow,
                        gameScreenStateFlow = viewModel.gameScreenStateFlow,
                        friendScreenStateFlow = viewModel.friendScreenStateFlow,
                        rankScreenStateFlow = viewModel.rankScreenStateFlow,
                        myPageScreenStateFlow = viewModel.myPageScreenStateFlow,
                        startSignIn = {
                            startSignIn()
                        },
                        updateIsSignInChecked = {
                            viewModel.updateIsSignInChecked(it)
                        },
                        updateCurrentView = {
                            viewModel.updateCurrentView(it)
                        }
                    )
                }
            }
        }
    }

    private fun startSignIn() {
        // Configure the One Tap sign-in client
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
            .setAutoSelectEnabled(true)
            .build()

        // Configure the One Tap client
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(getString(R.string.web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
            .build()

        // Display the One Tap sign-in UI
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    startForResult.launch(intentSenderRequest)
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow, or
                // do nothing and continue presenting the signed-out UI.

//                oneTapClient.beginSignIn(signUpRequest)
//                    .addOnSuccessListener(this) { result ->
//                        try {
//                            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                            startForResult.launch(intentSenderRequest)
//
//                        } catch (e: IntentSender.SendIntentException) {
//                            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                        }
//                    }
//                    .addOnFailureListener(this) { e ->
//                        // No Google Accounts found. Just continue presenting the signed-out UI.
//                        Log.d(TAG, e.localizedMessage)
//                    }

                Log.e(TAG, e.localizedMessage)
            }
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
        result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK) {
            Log.e("startForResult", "Result_Ok")
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            val username = credential.id
            val password = credential.password
            Log.e("startForResult", idToken.toString())
            Log.e("startForResult", username.toString())
            Log.e("startForResult", password.toString())

            PrefRepository.putString(GlobalConstants.USER_EMAIL, username)
            viewModel.updateIsSignedIn(true)
        } else {
            Log.e("startForResult", "Result_NoOk")
        }
    }

    private fun createSharedPreferences() {
        if(PrefRepository.checkPrefExist()) return

        PrefRepository.putString(GlobalConstants.USER_EMAIL, "")
        PrefRepository.putString(GlobalConstants.SERVER_TIME, "")
        PrefRepository.putString(GlobalConstants.LAST_PREDICTION, "")
        PrefRepository.putString(GlobalConstants.WORD_HISTORY, "")
        PrefRepository.putInt(GlobalConstants.TRY_COUNT, 0)
        PrefRepository.putBoolean(GlobalConstants.IS_FINISHED, false)
    }
}