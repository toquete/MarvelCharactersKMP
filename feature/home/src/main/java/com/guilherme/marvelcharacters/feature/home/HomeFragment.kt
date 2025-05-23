package com.guilherme.marvelcharacters.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.guilherme.marvelcharacters.core.common.observe
import com.guilherme.marvelcharacters.core.model.Character
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val nightModeViewModel: NightModeViewModel by activityViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    HomeRoute(onCharacterClick = ::navigateToDetail)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nightModeViewModel.nightMode.observe(viewLifecycleOwner) { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.actionToggleTheme) {
            nightModeViewModel.toggleDarkMode()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToDetail(character: Character) {
        val deeplink = "marvelcharacters://character/${character.id}".toUri()
        findNavController().navigate(deeplink)
    }
}