package com.textbuilder.ui

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.textbuilder.R
import com.textbuilder.gen.handlers.Reader
import com.textbuilder.service.FileHandler
import com.textbuilder.service.PreferencesHandler
import com.textbuilder.ui.display.DisplayFragment
import com.textbuilder.ui.generate.GenerateFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), UpdateListener {
    companion object {
        const val SOURCE_TAGS_LIST_TAG = "sourceTags"
    }

    private var displayFragment: DisplayFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Checking and restoring preferences storage if necessary
        val preferencesHandler = PreferencesHandler(this)
        preferencesHandler.logState()
        createDefaultSetOfSources()

        // Setting up navigation host for Jetpack's Navigation component framework
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setting up navigation view for Jetpack's Navigation component framework
        val bottomNavigationView: BottomNavigationView =
            findViewById(R.id.main_bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

        // Setting up update listener of ActivityMain to let spinner know, when to rebuild itself
        val interactionFragment =
            navHostFragment.childFragmentManager.fragments[0] as GenerateFragment
        interactionFragment.setListener(this)

        initLikeButton(findViewById(R.id.toolbar_button_favorite))

        // Displaying all cards by default
        displayFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_display) as DisplayFragment
        displayFragment?.displayAll() // for first launch
    }

    private fun createDefaultSetOfSources() {
        val preferencesHandler = PreferencesHandler(this)
        preferencesHandler.createEmptySetIfNecessary()

        createDefaultSource("Цитаты", preferencesHandler, R.raw.quotesmipt)
        createDefaultSource("Бугурты", preferencesHandler, R.raw.bugurts)
        createDefaultSource("Юморески", preferencesHandler, R.raw.jumoreski)
        createDefaultSource("Новости", preferencesHandler, R.raw.news)
    }

    private fun createDefaultSource(
        fileTag: String,
        preferencesHandler: PreferencesHandler,
        resId: Int
    ) {
        if (FileHandler.isFileTagUnique(fileTag, preferencesHandler)) {
            preferencesHandler.saveFileTag(fileTag)
            FileHandler.saveTextToFile(
                this,
                FileHandler.encodeFileTag(fileTag),
                Reader.readTxtFromRes(resId, this)
            )
        }
    }

    // Serve for handling display of general and favorite lists of cards
    private fun initLikeButton(favoriteButton: ImageButton) {
        var isDisplayingAll = true
        val buttonScaleAnimation: Animation =
            AnimationUtils.loadAnimation(this, R.anim.button_scale)
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
    }

    override fun onUpdate() {
        displayFragment?.displayAll()
    }
}