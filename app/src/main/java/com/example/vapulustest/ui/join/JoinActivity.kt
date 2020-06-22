package com.example.vapulustest.ui.join

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.vapulustest.R

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTransparentStatusBar()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
    }

    /**
     * This method is works to allow the full screen mode with a transparent status bar.
     * */
    private fun setTransparentStatusBar() {
        window.apply {
            statusBarColor =
                Color.TRANSPARENT
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            attributes.flags =
                attributes.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        }
    }
}