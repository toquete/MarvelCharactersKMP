package com.guilherme.marvelcharacters.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character
import com.guilherme.marvelcharacters.databinding.ItemListBinding

class FavoritesAdapter(
    private val characters: List<Character>,
    private val onClickListener: (Character) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.BindingHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesAdapter.BindingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return BindingHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: FavoritesAdapter.BindingHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    inner class BindingHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding: ItemListBinding = ItemListBinding.bind(item)

        fun bind(character: Character) {
            binding.textviewCharacter.text = character.name
            itemView.setOnClickListener { onClickListener(character) }
        }
    }
}