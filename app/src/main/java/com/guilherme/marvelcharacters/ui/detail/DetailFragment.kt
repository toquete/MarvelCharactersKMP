package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.MaterialToolbar
import com.guilherme.marvelcharacters.MainActivity
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var detailBinding: FragmentDetailBinding? = null

    private var mainToolbar: MaterialToolbar? = null

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)
        detailBinding = binding

        // workaround para collapsing toolbar
        mainToolbar = (activity as MainActivity).findViewById(R.id.mainToolbar)
        mainToolbar?.visibility = View.GONE

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        binding.collapsingToolbarLayout.title = args.character.name
        binding.description.text = if (args.character.description.isEmpty()) {
            "No description available"
        } else {
            args.character.description
        }
        Glide.with(this)
            .load("${args.character.thumbnail.path}.${args.character.thumbnail.extension}")
            .centerCrop()
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainToolbar?.visibility = View.VISIBLE
        mainToolbar = null
        detailBinding = null
    }
}
