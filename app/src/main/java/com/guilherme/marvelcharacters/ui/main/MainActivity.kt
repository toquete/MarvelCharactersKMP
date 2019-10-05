package com.guilherme.marvelcharacters.ui.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.source.remote.RetrofitFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val api = RetrofitFactory.makeRetrofitService()
        val characterRepository = CharacterRepositoryImpl(api)

        viewModel = ViewModelProviders.of(
            this, MainViewModelFactory(characterRepository, Dispatchers.Main)
        ).get(MainViewModel::class.java)

        viewModel.states.observe(this, Observer { state ->
            state?.let {
                when (state) {
                    is MainViewModel.CharacterListState.Characters -> showCharacters(state.characters)
                    is MainViewModel.CharacterListState.EmptyState -> showEmptyState()
                    is MainViewModel.CharacterListState.ErrorState -> showError(state.error)
                }
            }
        })

        viewModel.showLoading.observe(this, Observer { mustShowLoading ->
            mustShowLoading?.let { handleLoading(it) }
        })

        button.setOnClickListener { viewModel.onSearchCharacter(editText.text.toString()) }
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
