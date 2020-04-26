package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.EventObserver
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var favoritesBinding: FragmentFavoritesBinding? = null

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoritesBinding.bind(view)
        favoritesBinding = binding

        setupObservers(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesBinding = null
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

    private fun setupObservers(binding: FragmentFavoritesBinding) {
        favoritesViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            binding.recyclerViewFavorites.adapter = FavoritesAdapter(list) { character ->
                favoritesViewModel.onFavoriteItemClick(character)
            }
            setHasOptionsMenu(list.isNotEmpty())
        })

        favoritesViewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is FavoritesViewModel.FavoritesState.CharactersDeleted -> {
                    Snackbar.make(binding.recyclerViewFavorites, R.string.character_deleted, Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.bottomNavigation)
                        .show()
                }
                is FavoritesViewModel.FavoritesState.Error -> {
                    Snackbar.make(binding.recyclerViewFavorites, state.error.message.toString(), Snackbar.LENGTH_LONG).show()
                }
            }
        })

        favoritesViewModel.navigateToDetail.observe(viewLifecycleOwner, EventObserver { character ->
            navigateToDetail(character)
        })
    }

    private fun navigateToDetail(character: Character) {
        FavoritesFragmentDirections.actionFavoritesToDetail(character).apply {
            findNavController().navigate(this)
        }
    }

    private fun buildConfirmationDialog() {
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.delete_dialog_title)
            .setMessage(R.string.delete_dialog_message)
            .setPositiveButton(R.string.delete) { _, _ -> favoritesViewModel.onDeleteAllClick() }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
