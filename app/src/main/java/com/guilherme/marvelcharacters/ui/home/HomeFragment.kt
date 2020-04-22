package com.guilherme.marvelcharacters.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.FragmentHomeBinding
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)
        homeBinding = binding

        homeViewModel.states.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                when (state) {
                    is HomeViewModel.CharacterListState.Characters -> showCharacters(binding, state.characters)
                    is HomeViewModel.CharacterListState.EmptyState -> showEmptyState(binding)
                    is HomeViewModel.CharacterListState.ErrorState -> showError(binding, state.error)
                }
            }
        })

        homeViewModel.showLoading.observe(viewLifecycleOwner, Observer { mustShowLoading ->
            mustShowLoading?.let { handleLoading(binding, it) }
        })

        binding.button.setOnClickListener { homeViewModel.onSearchCharacter(binding.editText.text.toString()) }

        setupToolbar(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    private fun setupToolbar(binding: FragmentHomeBinding) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
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
            textviewMessage.text = context?.getString(R.string.empty_state_message)
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun showCharacters(binding: FragmentHomeBinding, list: List<Character>) {
        binding.textviewMessage.visibility = View.GONE
        binding.recyclerviewCharacters.run {
            layoutManager = LinearLayoutManager(context)
            adapter = HomeAdapter(list) { character ->
                HomeFragmentDirections.actionToDetail(character).apply {
                    findNavController().navigate(this)
                }
            }
            visibility = View.VISIBLE
        }
    }
}