package com.guilherme.marvelcharacters.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.FragmentHomeBinding
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val nightModeViewModel: NightModeViewModel by activityViewModels()

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
        homeAdapter = HomeAdapter { character -> homeViewModel.onItemClick(character) }
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.states.collect { setupState(it) }
                }
                launch {
                    homeViewModel.events.collect { setupEvent(it) }
                }
                launch {
                    nightModeViewModel.nightMode.collect { AppCompatDelegate.setDefaultNightMode(it) }
                }
            }
        }
    }

    private fun closeKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(homeBinding.button.windowToken, 0)
        }
    }

    private fun setupState(state: HomeViewModel.State) = with(homeBinding) {
        progressBar.isVisible = state.isLoading
        recyclerviewCharacters.isVisible = !state.isLoading && state.errorMessageId == null
        textviewMessage.isVisible = !state.isLoading && state.errorMessageId != null

        homeAdapter.submitList(state.characters)

        state.errorMessageId?.let {
            textviewMessage.setText(it)
        }
    }

    private fun setupEvent(event: HomeViewModel.Event) {
        when (event) {
            is HomeViewModel.Event.NavigateToDetails -> navigateToDetail(event.character)
        }
    }

    private fun navigateToDetail(character: CharacterVO) {
        HomeFragmentDirections.actionHomeToDetail(character).run {
            findNavController().navigate(this)
        }
    }
}