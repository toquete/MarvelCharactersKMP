package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.ActivityDetailBinding
import com.guilherme.marvelcharacters.extension.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
    }

    private fun setupObservers() {
        detailViewModel.uiState.observe(this) { state ->
            when (state) {
                is DetailUiState.ShowSnackbar -> showSnackbar(state.messageId, state.showAction)
                DetailUiState.Loading -> {}
                is DetailUiState.Success -> setupViewBindings(state)
            }
        }
    }

    private fun showSnackbar(stringId: Int?, showAction: Boolean) {
        stringId?.let {
            Snackbar.make(binding.fab, stringId, Snackbar.LENGTH_LONG).apply {
                if (showAction) {
                    setAction(R.string.undo) { detailViewModel.onUndoClick() }
                }
            }.show()
            detailViewModel.onSnackbarShown()
        }
    }

    private fun setupViewBindings(state: DetailUiState.Success) {
        binding.fab.isActivated = state.character.isFavorite
        binding.collapsingToolbarLayout.title = state.character.character.name
        binding.description.text = state.character.character.description.ifEmpty {
            getString(R.string.no_description_available)
        }
        Glide.with(this)
            .load(state.character.character.thumbnail)
            .centerCrop()
            .into(binding.imageView)

        binding.fab.setOnClickListener { detailViewModel.onFabClick(state.character.isFavorite) }
    }
}