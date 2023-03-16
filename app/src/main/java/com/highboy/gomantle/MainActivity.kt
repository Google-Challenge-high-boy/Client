package com.highboy.gomantle

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.credentials.CredentialsClient
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.highboy.gomantle.ui.GomantleApp
import com.highboy.gomantle.ui.theme.GomantleTheme

class MainActivity : ComponentActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("activity", "GomantleMainActivity")
        setContent {
            GomantleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GomantleApp()
                }
            }
        }

//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
//                .setSupported(true)
//                .build())
//            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                .setServerClientId(getString(R.string.web_client_id))
//                .setFilterByAuthorizedAccounts(false)
//                .build())
//            .setAutoSelectEnabled(true)
//            .build()
//
//        signUpRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                .setServerClientId(getString(R.string.web_client_id))
//                .setFilterByAuthorizedAccounts(false)
//                .build())
//            .build()
//
//        Log.e(TAG, "hello")
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    activityLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
//                    Log.e(TAG, "oneTapLogin")
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                oneTapClient.beginSignIn(signUpRequest)
//                    .addOnSuccessListener(this) { result ->
//                        try {
//                            activityLauncher.launch(IntentSenderRequest.Builder(result.pendingIntent.intentSender).build())
//                        } catch (e: IntentSender.SendIntentException) {
//                            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                        }
//                    }
//                    .addOnFailureListener(this) { e ->
//                        // No Google Accounts found. Just continue presenting the signed-out UI.
//                        Log.d(TAG, e.localizedMessage)
//                    }
//                Log.d(TAG, e.localizedMessage + "bbbbbb")
//            }
//    }
//
//    private val activityLauncher = registerForActivityResult(
//        ActivityResultContracts.StartIntentSenderForResult()) {
//            result ->
//            try {
//                if(result.resultCode == Activity.RESULT_OK) {
//                    val oneTapClient = Identity.getSignInClient(this)
//                    val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
//                    val tokenId = credentials.googleIdToken
//                    Log.d("aaaa", tokenId.toString())
//                }
//            } catch(e: ApiException) {
//                Log.d("aaaa", "ApiException")
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GomantleTheme {
        GomantleApp()
    }
}