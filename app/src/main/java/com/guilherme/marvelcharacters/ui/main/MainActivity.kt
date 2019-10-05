package com.guilherme.marvelcharacters.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        button.setOnClickListener { mainViewModel.onSearchCharacter(editText.text.toString()) }
    }

    private fun showError(error: Exception) {
        recyclerview_characters.visibility = View.GONE
        textview_message.text = error.message
        textview_message.visibility = View.VISIBLE
    }

    private fun handleLoading(mustShowLoading: Boolean) {
        progressbar.visibility = if (mustShowLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyState() {
        recyclerview_characters.visibility = View.GONE
        textview_message.text = applicationContext.getString(R.string.empty_state_message)
        textview_message.visibility = View.VISIBLE
    }

    private fun showCharacters(list: List<Character>) {
        textview_message.visibility = View.GONE
        with(recyclerview_characters) {
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(list)
            visibility = View.VISIBLE
        }
    }
}
