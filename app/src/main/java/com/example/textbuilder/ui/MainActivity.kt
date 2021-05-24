package com.example.textbuilder.ui

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.textbuilder.R
import com.example.textbuilder.ui.display.DisplayFragment
import com.example.textbuilder.ui.interaction.InteractionFragment

class MainActivity : AppCompatActivity(), UpdateListener {
    private var displayFragment: DisplayFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        var isDisplayingAll = true
        displayFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_display) as DisplayFragment
        val favoriteButton: ImageButton = findViewById(R.id.toolbar_button_favorite)
        favoriteButton.setOnClickListener {
            if (isDisplayingAll) displayFragment?.displayFavorite()
            else displayFragment?.displayAll()
            isDisplayingAll = !isDisplayingAll
        }
        displayFragment?.displayAll() // crutch for first launch

        val interactionFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_interaction) as InteractionFragment
        interactionFragment.setListener(this)
    }

    override fun onUpdate() {
        Thread.sleep(20) // waiting for db
        displayFragment?.displayAll()
    }
}