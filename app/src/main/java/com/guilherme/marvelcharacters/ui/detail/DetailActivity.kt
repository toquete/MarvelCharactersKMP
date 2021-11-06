package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.EventObserver
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.ActivityDetailBinding
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.infrastructure.util.Mapper
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val mapper: Mapper<Character, CharacterVO> by inject()

    private val args: DetailActivityArgs by navArgs()

    private val detailViewModel: DetailViewModel by viewModel { parametersOf(mapper.mapFrom(args.character)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewBindings()
        setupObservers()
    }

    private fun setupObservers() {
        detailViewModel.snackbarMessage.observe(this, EventObserver { result ->
            showSnackbar(result.first, result.second)
        })

        detailViewModel.isCharacterFavorite.observe(this, Observer { isFavorite ->
            binding.fab.isActivated = isFavorite
        })
    }

    private fun showSnackbar(stringId: Int, showAction: Boolean) {
        Snackbar.make(binding.fab, stringId, Snackbar.LENGTH_LONG).apply {
            if (showAction) {
                setAction(R.string.undo) { detailViewModel.onUndoClick() }
            }
        }.show()
    }

    private fun setupViewBindings() {
        binding.collapsingToolbarLayout.title = args.character.name
        binding.description.text = if (args.character.description.isEmpty()) {
            getString(R.string.no_description_available)
        } else {
            args.character.description
        }
        Glide.with(this)
            .load("${args.character.thumbnail.path}.${args.character.thumbnail.extension}")
            .centerCrop()
            .into(binding.imageView)

        binding.fab.setOnClickListener { detailViewModel.onFabClick() }
    }
}