package com.guilherme.marvelcharacters.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.states.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is MainViewModel.CharacterListState.Characters -> showCharacters(state.characters)
                    is MainViewModel.CharacterListState.EmptyState -> showEmptyState()
                    is MainViewModel.CharacterListState.ErrorState -> showError(state.error)
                }
            }
        })

        mainViewModel.showLoading.observe(this, Observer { mustShowLoading ->
            mustShowLoading?.let { handleLoading(it) }
        })

        binding.button.setOnClickListener { mainViewModel.onSearchCharacter(binding.editText.text.toString()) }
    }

    private fun showError(error: Exception) {
        with(binding) {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = error.message
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun handleLoading(mustShowLoading: Boolean) {
        binding.progressBar.visibility = if (mustShowLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState() {
        with(binding) {
            recyclerviewCharacters.visibility = View.GONE
            textviewMessage.text = applicationContext.getString(R.string.empty_state_message)
            textviewMessage.visibility = View.VISIBLE
        }
    }

    private fun showCharacters(list: List<Character>) {
        binding.textviewMessage.visibility = View.GONE
        with(binding.recyclerviewCharacters) {
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(list)
            visibility = View.VISIBLE
        }
    }
}
