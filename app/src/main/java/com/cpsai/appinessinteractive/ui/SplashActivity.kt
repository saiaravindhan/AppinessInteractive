package com.cpsai.appinessinteractive.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import com.cpsai.appinessinteractive.R
import com.cpsai.appinessinteractive.utility.Utility
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    val activityScope = CoroutineScope(Dispatchers.Main)
    val utility= Utility()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            supportActionBar?.hide()
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
            supportActionBar?.hide()
        }

        activityScope.launch {
            delay(3000)

            if(utility.isOnline(this@SplashActivity)) {
                var intent = Intent(this@SplashActivity, Home::class.java)
                startActivity(intent)
                finish()
            }else{
                var intent = Intent(this@SplashActivity, NoInternetActiivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onPause() {
        activityScope.cancel()
        super.onPause()
    }
}