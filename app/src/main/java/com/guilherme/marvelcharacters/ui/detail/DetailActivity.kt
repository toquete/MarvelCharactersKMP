package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.ActivityDetailBinding
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    @Inject
    lateinit var mapper: CharacterMapper

    @Inject
    lateinit var viewModelFactory: DetailViewModelFactory

    private lateinit var binding: ActivityDetailBinding

    private val args: DetailActivityArgs by navArgs()

    private val detailViewModel: DetailViewModel by viewModels {
        provideFactory(viewModelFactory, mapper.mapFrom(args.character))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewBindings()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    detailViewModel.isCharacterFavorite.collect { isFavorite ->
                        binding.fab.isActivated = isFavorite
                    }
                }
                launch {
                    detailViewModel.events.collect { event ->
                        when (event) {
                            is DetailViewModel.Event.ShowSnackbarMessage -> showSnackbar(event.message.first, event.message.second)
                        }
                    }
                }
            }
        }
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