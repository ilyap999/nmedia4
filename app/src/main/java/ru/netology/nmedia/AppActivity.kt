package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text.isNullOrBlank()) {
                findNavController(R.id.nav_main)
                    .navigate(R.id.action_feedFragment_to_newPostFragment, Bundle().apply {
                        putString("TEXT_KEY", text)
                    })
                return@let
            }
        }
    }
}
