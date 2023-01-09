package com.guilherme.marvelcharacters.feature.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.google.accompanist.themeadapter.material.MdcTheme
import com.guilherme.marvelcharacters.feature.detail.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _detailBinding: FragmentDetailBinding? = null
    private val detailBinding get() = _detailBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _detailBinding = FragmentDetailBinding.inflate(inflater, container, false)

        detailBinding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MdcTheme {
                    DetailRoute()
                }
            }
        }
        return detailBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _detailBinding = null
    }
}