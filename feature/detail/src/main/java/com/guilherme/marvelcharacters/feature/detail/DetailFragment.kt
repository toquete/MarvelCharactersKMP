package com.guilherme.marvelcharacters.feature.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.guilherme.marvelcharacters.core.common.observe
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import com.guilherme.marvelcharacters.feature.detail.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment(R.layout.fragment_detail) {

    private var _detailBinding: FragmentDetailBinding? = null
    private val detailBinding get() = _detailBinding!!

    private val detailViewModel: DetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _detailBinding = FragmentDetailBinding.bind(view)

        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailBinding = null
    }

    private fun setupObservers() {
        detailViewModel.state.observe(this) { state ->
            if (state.character != null) {
                setupViewBindings(state.character)
            }
            if (state.messageId != null) {
                showSnackbar(state.messageId, state.showAction)
            }
        }
    }

    private fun showSnackbar(stringId: Int?, showAction: Boolean) {
        stringId?.let {
            Snackbar.make(detailBinding.fab, stringId, Snackbar.LENGTH_LONG).apply {
                if (showAction) {
                    setAction(R.string.undo) { detailViewModel.onUndoClick() }
                }
            }.show()
            detailViewModel.onSnackbarShown()
        }
    }

    private fun setupViewBindings(character: FavoriteCharacter) {
        detailBinding.fab.isActivated = character.isFavorite
        detailBinding.collapsingToolbarLayout.title = character.character.name
        detailBinding.description.text = character.character.description.ifEmpty {
            getString(R.string.no_description_available)
        }
        Glide.with(this)
            .load(character.character.thumbnail)
            .centerCrop()
            .into(detailBinding.imageView)

        detailBinding.fab.setOnClickListener { detailViewModel.onFabClick(character.isFavorite) }
    }
}