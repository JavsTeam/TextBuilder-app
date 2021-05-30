package com.example.textbuilder.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.textbuilder.R
import com.example.textbuilder.service.Logger
import com.example.textbuilder.service.PreferencesHandler
import com.example.textbuilder.ui.display.DisplayFragment
import com.example.textbuilder.ui.interaction.InteractionFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(), UpdateListener {
    companion object {
        const val SOURCE_TAGS_LIST_TAG = "sourceTags"
    }

    private var displayFragment: DisplayFragment? = null
    var sourceList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        val sharedPreferences = getPreferences(MODE_PRIVATE)
        val preferencesHandler = PreferencesHandler(this)
        preferencesHandler.logState()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.main_bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)


        var isDisplayingAll = true
        displayFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_display) as DisplayFragment

        val interactionFragment =
            navHostFragment.childFragmentManager.fragments[0] as InteractionFragment
        interactionFragment.setListener(this)
        //interactionFragment.setSpinnerAdapter()

        val buttonScaleAnimation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.button_scale)
        val favoriteButton: ImageButton = findViewById(R.id.toolbar_button_favorite)
        val hostFragmentLayout: View = findViewById(R.id.nav_host_fragment)
        favoriteButton.setOnClickListener {
            it.startAnimation(buttonScaleAnimation)
            if (isDisplayingAll) {
                displayFragment?.displayFavorite()
                hostFragmentLayout.visibility = View.GONE
            } else {
                displayFragment?.displayAll()
                hostFragmentLayout.visibility = View.VISIBLE
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