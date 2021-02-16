package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.EventObserver
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var _favoritesBinding: FragmentFavoritesBinding? = null

    private val favoritesBinding get() = _favoritesBinding!!

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _favoritesBinding = FragmentFavoritesBinding.bind(view)

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

    private fun setupObservers() {
        favoritesViewModel.list.observe(viewLifecycleOwner) { list ->
            favoritesBinding.recyclerViewFavorites.adapter = FavoritesAdapter(list) { character ->
                favoritesViewModel.onFavoriteItemClick(character)
            }
            favoritesBinding.recyclerViewFavorites.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            setHasOptionsMenu(list.isNotEmpty())
        }

        favoritesViewModel.snackbarMessage.observe(viewLifecycleOwner, EventObserver { id -> showSnackbar(id) })

        favoritesViewModel.navigateToDetail.observe(viewLifecycleOwner, EventObserver { character -> navigateToDetail(character) })
    }

    private fun showSnackbar(stringId: Int) {
        Snackbar.make(favoritesBinding.recyclerViewFavorites, stringId, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.bottomNavigation)
            .show()
    }

    private fun navigateToDetail(character: Character) {
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
