package com.guilherme.marvelcharacters.feature.favorites

import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.themeadapter.material.MdcTheme
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.CharacterListItem

class FavoritesAdapter(
    private val onClickListener: (Character) -> Unit
) : ListAdapter<Character, FavoritesAdapter.BindingHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.BindingHolder {
        return BindingHolder(ComposeView(parent.context))
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.BindingHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    inner class BindingHolder(private val item: ComposeView) : RecyclerView.ViewHolder(item) {
        fun bind(character: Character) {
            item.setContent {
                MdcTheme {
                    CharacterListItem(name = character.name)
                }
            }
            itemView.setOnClickListener { onClickListener(character) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }
}