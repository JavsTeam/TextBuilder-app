package com.example.textbuilder.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.textbuilder.R
import com.example.textbuilder.ui.interaction.InteractionFragment
import com.example.textbuilder.ui.readysource.ReadySourceFragment

class MainActivity : AppCompatActivity(), UpdateListener {
    var readySourceFragment: ReadySourceFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        var isDisplayingAll = true
        readySourceFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_ready_source) as ReadySourceFragment
        val favoriteButton: ImageButton = findViewById(R.id.toolbar_button_favorite)
        favoriteButton.setOnClickListener {
            if (isDisplayingAll) readySourceFragment?.displayFavorite()
            else readySourceFragment?.displayAll()
            isDisplayingAll = !isDisplayingAll
        }
        readySourceFragment?.displayAll() // crutch for first launch


        val interactionFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_interaction) as InteractionFragment
        interactionFragment.setListener(this)


    }

    override fun onUpdate() {
        Log.d("Debug", "onUpdate()")
        Thread.sleep(20) // waiting for db
        readySourceFragment?.displayAll()
    }
}