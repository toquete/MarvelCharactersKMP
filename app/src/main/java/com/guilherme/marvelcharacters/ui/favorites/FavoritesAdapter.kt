package com.guilherme.marvelcharacters.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.ItemListBinding
import com.guilherme.marvelcharacters.model.CharacterVO

class FavoritesAdapter(
    private val onClickListener: (CharacterVO) -> Unit
) : ListAdapter<CharacterVO, FavoritesAdapter.BindingHolder>(diffCallback) {

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

        fun bind(character: CharacterVO) {
            binding.textviewCharacter.text = character.name
            itemView.setOnClickListener { onClickListener(character) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<CharacterVO>() {
            override fun areItemsTheSame(oldItem: CharacterVO, newItem: CharacterVO): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CharacterVO, newItem: CharacterVO): Boolean {
                return oldItem == newItem
            }
        }
    }
}