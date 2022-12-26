package com.guilherme.marvelcharacters.feature.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.accompanist.themeadapter.material.MdcTheme
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.feature.favorites.databinding.FragmentFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _favoritesBinding: FragmentFavoritesBinding? = null
    private val favoritesBinding get() = _favoritesBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _favoritesBinding = FragmentFavoritesBinding.inflate(inflater, container, false)

        favoritesBinding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    FavoritesRoute(onCharacterClick = ::navigateToDetail)
                }
            }
        }

        return favoritesBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _favoritesBinding = null
    }

    private fun navigateToDetail(character: Character) {
        val deeplink = "marvelcharacters://character/${character.id}".toUri()
        findNavController().navigate(deeplink)
    }
}
