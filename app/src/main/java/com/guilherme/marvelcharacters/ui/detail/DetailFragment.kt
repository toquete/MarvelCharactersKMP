package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var detailBinding: FragmentDetailBinding? = null

    private val args: DetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)
        detailBinding = binding

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
        detailBinding = null
    }
}
