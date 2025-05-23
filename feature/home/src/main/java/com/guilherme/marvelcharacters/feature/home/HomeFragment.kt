package com.guilherme.marvelcharacters.feature.home

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.guilherme.marvelcharacters.core.common.observe
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.feature.home.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!

    private val homeViewModel: HomeViewModel by activityViewModel()
    private val nightModeViewModel: NightModeViewModel by activityViewModel()

    private lateinit var homeAdapter: HomeAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeViewModel.query?.let { homeBinding.filledTextField.editText?.setText(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _homeBinding = FragmentHomeBinding.bind(view)

        setHasOptionsMenu(true)
        setupObservers()
        setupScreenBindings()
        setupAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.query = homeBinding.filledTextField.editText?.text.toString()
        _homeBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.actionToggleTheme) {
            nightModeViewModel.toggleDarkMode()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupAdapter() {
        homeAdapter = HomeAdapter { character -> navigateToDetail(character) }
        homeBinding.recyclerviewCharacters.adapter = homeAdapter
        homeBinding.recyclerviewCharacters.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupScreenBindings() {
        homeBinding.run {
            button.setOnClickListener {
                closeKeyboard()
                homeViewModel.onSearchCharacter(filledTextField.editText?.text.toString())
            }

            filledTextField.editText?.doOnTextChanged { text, _, _, _ ->
                button.isEnabled = !text.isNullOrEmpty()
            }

            searchEditText.setOnEditorActionListener { textView, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_SEARCH -> {
                        closeKeyboard()
                        homeViewModel.onSearchCharacter(textView.text.toString())
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupObservers() {
        homeViewModel.state.observe(viewLifecycleOwner) { state ->
            with(homeBinding) {
                progressBar.isVisible = state.isLoading
                recyclerviewCharacters.isVisible = !state.isLoading && state.characters.isNotEmpty()
                textviewMessage.isVisible = !state.isLoading && state.errorMessageId != null
                state.errorMessageId?.let { textviewMessage.setText(it) }
                homeAdapter.submitList(state.characters)
            }
        }
        nightModeViewModel.nightMode.observe(viewLifecycleOwner) { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    private fun closeKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(homeBinding.button.windowToken, 0)
        }
    }

    private fun navigateToDetail(character: Character) {
        val deeplink = "marvelcharacters://character/${character.id}".toUri()
        findNavController().navigate(deeplink)
    }
}