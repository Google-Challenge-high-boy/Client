package com.highboy.gomantle

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import com.highboy.gomantle.data.User
import com.highboy.gomantle.ui.theme.GomantleTheme

class UserProfileActivity : ComponentActivity() {

    private val user: User by lazy {
        when(Build.VERSION.SDK_INT) {
            33 -> intent?.getSerializableExtra(USER_ID, User::class.java) as User
            else -> intent?.getSerializableExtra(USER_ID)as User
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GomantleTheme() {
                Text(text = "Hello ${user.userName}")
            }
        }
    }

    companion object {
        private const val USER_ID = "User_Id"
        fun newIntent(context: Context, user: User) =
            Intent(context, UserProfileActivity::class.java).apply {
                putExtra(USER_ID, user)
            }
    }
}