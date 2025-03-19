package com.example.androidpracticumcustomview

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.androidpracticumcustomview.ui.theme.CustomContainer


class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this).apply {
            movementAnimationDuration = 4000
            transparentAnimationDuration = 2000
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
        customContainer.addView(secondView)
    }
}