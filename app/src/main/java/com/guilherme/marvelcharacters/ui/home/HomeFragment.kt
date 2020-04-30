package com.guilherme.marvelcharacters.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.guilherme.marvelcharacters.EventObserver
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentHomeBinding
import com.guilherme.marvelcharacters.infrastructure.extensions.sharedGraphViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _homeBinding: FragmentHomeBinding? = null

    private val homeBinding get() = _homeBinding!!

    private val homeViewModel: HomeViewModel by sharedGraphViewModel(R.id.nav_graph)

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
            homeViewModel.onActionItemClick()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setupAdapter() {
        homeAdapter = HomeAdapter { character -> homeViewModel.onItemClick(character) }
        homeBinding.recyclerviewCharacters.adapter = homeAdapter
        homeBinding.recyclerviewCharacters.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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
        }
    }

    private fun setupObservers() {
        homeViewModel.states.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when (state) {
                    is HomeViewModel.CharacterListState.Characters -> showCharacters(state.characters)
                    is HomeViewModel.CharacterListState.EmptyState -> showEmptyState()
                    is HomeViewModel.CharacterListState.ErrorState -> showError(state.error)
                    is HomeViewModel.CharacterListState.Loading -> handleLoading(mustShowLoading = true)
                }
            }
        })

        homeViewModel.navigateToDetail.observe(viewLifecycleOwner, EventObserver { character ->
            navigateToDetail(character)
        })

        homeViewModel.nightMode.observe(viewLifecycleOwner, Observer { mode ->
            AppCompatDelegate.setDefaultNightMode(mode)
        })
    }

    private fun closeKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(homeBinding.button.windowToken, 0)
        }
    }

    private fun showError(error: Exception) {
        handleLoading(mustShowLoading = false)
        homeBinding.run {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = error.message
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun handleLoading(mustShowLoading: Boolean) {
        homeBinding.run {
            if (mustShowLoading) {
                recyclerviewCharacters.visibility = View.GONE
                textviewMessage.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showEmptyState() {
        handleLoading(mustShowLoading = false)
        homeBinding.run {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = resources.getString(R.string.empty_state_message)
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun showCharacters(list: List<Character>) {
        handleLoading(mustShowLoading = false)
        homeAdapter.characters = list
        homeAdapter.notifyDataSetChanged()

        homeBinding.textviewMessage.visibility = View.GONE
        homeBinding.recyclerviewCharacters.visibility = View.VISIBLE
    }

    private fun navigateToDetail(character: Character) {
        HomeFragmentDirections.actionHomeToDetail(character).run {
            findNavController().navigate(this)
        }
    }

}