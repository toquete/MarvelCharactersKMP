package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.extension.observe
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                val state by favoritesViewModel.state.collectAsState()
                MdcTheme {
                    FavoritesScreen(
                        state = state,
                        onItemClick = ::navigateToDetail,
                        onErrorMessageShown = favoritesViewModel::onErrorMessageShown,
                        onActionButtonClick = { }
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.favorite_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.delete) {
            buildConfirmationDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupObservers() {
        favoritesViewModel.state.observe(viewLifecycleOwner) { state ->
            setHasOptionsMenu(state.list.isNotEmpty())
        }
    }

    private fun navigateToDetail(character: CharacterVO) {
        FavoritesFragmentDirections.actionFavoritesToDetail(character).apply {
            findNavController().navigate(this)
        }
    }

    private fun buildConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_dialog_title)
            .setMessage(R.string.delete_dialog_message)
            .setPositiveButton(R.string.delete) { _, _ -> favoritesViewModel.onDeleteAllClick() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
