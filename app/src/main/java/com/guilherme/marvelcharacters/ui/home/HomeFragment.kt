package com.guilherme.marvelcharacters.ui.home

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.guilherme.marvelcharacters.EventObserver
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by stateViewModel()

    private lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        homeBinding = binding

        setupObservers(binding)
        setupScreenBindings(binding)
        setupAdapter(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    private fun setupAdapter(binding: FragmentHomeBinding) {
        homeAdapter = HomeAdapter { character -> homeViewModel.onItemClick(character) }
        binding.recyclerviewCharacters.adapter = homeAdapter
    }

    private fun setupScreenBindings(binding: FragmentHomeBinding) {
        binding.button.setOnClickListener {
            closeKeyboard(binding)
            homeViewModel.onSearchCharacter(binding.editText.text.toString())
        }
    }

    private fun setupObservers(binding: FragmentHomeBinding) {
        homeViewModel.states.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when (state) {
                    is HomeViewModel.CharacterListState.Characters -> showCharacters(binding, state.characters)
                    is HomeViewModel.CharacterListState.EmptyState -> showEmptyState(binding)
                    is HomeViewModel.CharacterListState.ErrorState -> showError(binding, state.error)
                }
            }
        })

        homeViewModel.list.observe(viewLifecycleOwner, Observer { list -> showCharacters(binding, list) })

        homeViewModel.showLoading.observe(viewLifecycleOwner, Observer { mustShowLoading ->
            mustShowLoading?.let { handleLoading(binding, it) }
        })

        homeViewModel.navigateToDetail.observe(viewLifecycleOwner, EventObserver { character ->
            navigateToDetail(character)
        })
    }

    private fun closeKeyboard(binding: FragmentHomeBinding) {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(binding.button.windowToken, 0)
        }
    }

    private fun showError(binding: FragmentHomeBinding, error: Exception) {
        binding.run {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = error.message
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun handleLoading(binding: FragmentHomeBinding, mustShowLoading: Boolean) {
        binding.progressBar.visibility = if (mustShowLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState(binding: FragmentHomeBinding) {
        binding.run {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = resources.getString(R.string.empty_state_message)
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun showCharacters(binding: FragmentHomeBinding, list: List<Character>) {
        homeAdapter.characters = list
        homeAdapter.notifyDataSetChanged()

        binding.textviewMessage.visibility = View.GONE
        binding.recyclerviewCharacters.visibility = View.VISIBLE
    }

    private fun navigateToDetail(character: Character) {
        HomeFragmentDirections.actionHomeToDetail(character).run {
            findNavController().navigate(this)
        }
    }

}