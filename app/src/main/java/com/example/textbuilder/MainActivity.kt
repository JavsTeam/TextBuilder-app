package com.example.textbuilder

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.textbuilder.ui.readysource.ReadySourceFragment
import com.example.textbuilder.ui.readysource.recyclerview.Card

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
/*
        val readySourceFragment: ReadySourceFragment = findViewById(R.id.fragment_ready_source)

        val favoriteButton: ImageButton = findViewById(R.id.toolbar_button_favorite)
        favoriteButton.setOnClickListener {
            readySourceFragment.displayFavorite()
        }*/
    }
}