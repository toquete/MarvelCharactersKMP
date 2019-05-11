package com.guilherme.marvelcharacters.ui.main

import android.app.SearchManager
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.View
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.data.repository.CharacterRepositoryImpl
import com.guilherme.marvelcharacters.data.source.remote.RetrofitFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

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
                    is MainViewModel.CharacterListState.LoadingState -> showLoading()
                    is MainViewModel.CharacterListState.Characters -> showCharacters(state.characters)
                    is MainViewModel.CharacterListState.EmptyState -> showEmptyState()
                    is MainViewModel.CharacterListState.ErrorState -> showError(state.error)
                }
            }
        })

        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                viewModel.onSearchCharacter(query)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu?.findItem(R.id.search_bar)?.actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }

    private fun showError(error: Exception) {
        hideLoading()
        textview_message.text = error.message
        textview_message.visibility = View.VISIBLE
    }

    private fun showLoading() {
        recyclerview_characters.visibility = View.GONE
        textview_message.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        progressbar.visibility = View.GONE
    }

    private fun showEmptyState() {
        hideLoading()
        textview_message.text = applicationContext.getString(R.string.empty_state_message)
        textview_message.visibility = View.VISIBLE
    }

    private fun showCharacters(list: List<Character>) {
        hideLoading()
        with(recyclerview_characters) {
            layoutManager = LinearLayoutManager(context)
            adapter = MainAdapter(list)
            visibility = View.VISIBLE
        }
    }
}
