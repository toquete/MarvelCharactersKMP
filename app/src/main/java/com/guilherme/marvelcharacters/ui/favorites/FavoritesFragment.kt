package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var favoritesBinding: FragmentFavoritesBinding? = null

    private val favoritesViewModel: FavoritesViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoritesBinding.bind(view)
        favoritesBinding = binding

        favoritesViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            binding.recyclerViewFavorites.adapter = FavoritesAdapter(list) { character ->
                FavoritesFragmentDirections.actionFavoritesToDetail(character).apply {
                    findNavController().navigate(this)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesBinding = null
    }
}
