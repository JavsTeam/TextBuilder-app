package com.example.textbuilder.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        val interactionFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_interaction) as InteractionFragment
        interactionFragment.setListener(this)

        val buttonScaleAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.button_scale)
        val favoriteButton: ImageButton = findViewById(R.id.toolbar_button_favorite)
        val interactionFragmentLayout: View = findViewById(R.id.fragment_interaction)
        favoriteButton.setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            if (isDisplayingAll) {
                displayFragment?.displayFavorite()
                interactionFragmentLayout.visibility = View.GONE
            }
            else {
                displayFragment?.displayAll()
                interactionFragmentLayout.visibility = View.VISIBLE
            }
            isDisplayingAll = !isDisplayingAll
        }
        displayFragment?.displayAll() // crutch for first launch
    }

    override fun onUpdate() {
        Thread.sleep(20) // waiting for db
        displayFragment?.displayAll()
    }
}