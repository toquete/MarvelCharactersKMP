package com.guilherme.marvelcharacters.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.extension.observe
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val nightModeViewModel: NightModeViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by homeViewModel.state.collectAsState()
                MdcTheme {
                    HomeScreen(
                        state = state,
                        onSearchButtonClick = homeViewModel::onSearchCharacter,
                        onItemClick = ::navigateToDetail
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupObservers()
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

    private fun setupObservers() {
        nightModeViewModel.nightMode.observe(viewLifecycleOwner) { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun navigateToDetail(character: CharacterVO) {
        HomeFragmentDirections.actionHomeToDetail(character).run {
            findNavController().navigate(this)
        }
    }
}