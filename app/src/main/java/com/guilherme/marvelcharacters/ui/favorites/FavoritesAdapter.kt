package com.guilherme.marvelcharacters.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.databinding.ItemListBinding

class FavoritesAdapter(
    private val onClickListener: (Character) -> Unit
) : ListAdapter<Character, FavoritesAdapter.BindingHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.BindingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return BindingHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritesAdapter.BindingHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
    }

    inner class BindingHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding: ItemListBinding = ItemListBinding.bind(item)

        fun bind(character: Character) {
            binding.textviewCharacter.text = character.name
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