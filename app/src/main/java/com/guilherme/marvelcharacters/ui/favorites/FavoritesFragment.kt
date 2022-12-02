package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentFavoritesBinding
import com.guilherme.marvelcharacters.extension.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesBinding: FragmentFavoritesBinding? = null
    private val favoritesBinding get() = _favoritesBinding!!

    private val favoritesViewModel: FavoritesViewModel by activityViewModels()

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _favoritesBinding = FragmentFavoritesBinding.bind(view)

        setupView()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesBinding = null
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

    private fun setupView() = with(favoritesBinding) {
        favoritesAdapter = FavoritesAdapter { character ->
            navigateToDetail(character)
        }
        recyclerViewFavorites.adapter = favoritesAdapter
        recyclerViewFavorites.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupObservers() {
        favoritesViewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesUiState.ShowSnackbar -> showSnackbar(state.messageId)
                is FavoritesUiState.Success -> setupSuccess(state.list)
            }
        }
    }

    private fun setupSuccess(characters: List<Character>) {
        favoritesAdapter.submitList(characters)
        setHasOptionsMenu(characters.isNotEmpty())
    }

    private fun showSnackbar(@StringRes stringId: Int?) {
        stringId?.let {
            Snackbar.make(favoritesBinding.recyclerViewFavorites, it, Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.bottomNavigation)
                .show()
            favoritesViewModel.onSnackbarShown()
        }
    }

    private fun navigateToDetail(character: Character) {
        FavoritesFragmentDirections.actionFavoritesToDetail(character.id).apply {
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
