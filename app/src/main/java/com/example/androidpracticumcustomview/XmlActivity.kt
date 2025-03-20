package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer

private const val DEFAULT_MOVEMENT_ANIMATION_DURATION = 5000L
private const val DEFAULT_TRANSPARENT_ANIMATION_DURATION = 2000L

class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this).apply {
            movementAnimationDuration = DEFAULT_MOVEMENT_ANIMATION_DURATION
            transparentAnimationDuration = DEFAULT_TRANSPARENT_ANIMATION_DURATION
        }
        setContentView(customContainer)
        customContainer.setOnClickListener {
            finish()
        }

        val firstView = TextView(this).apply {
            text = getString(R.string.first_item_text)
        }

        val secondView = TextView(this).apply {
            text = getString(R.string.second_item_text)
        }

        customContainer.addView(firstView)

        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }
}