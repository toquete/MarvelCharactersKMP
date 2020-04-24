package com.guilherme.marvelcharacters.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.FragmentFavoritesBinding
import org.koin.android.ext.android.inject

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private var favoritesBinding: FragmentFavoritesBinding? = null

    private val favoritesViewModel: FavoritesViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentFavoritesBinding.bind(view)
        favoritesBinding = binding

        favoritesViewModel.list.observe(viewLifecycleOwner, Observer { list ->
            binding.recyclerViewFavorites.adapter = FavoritesAdapter(list)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        favoritesBinding = null
    }
}