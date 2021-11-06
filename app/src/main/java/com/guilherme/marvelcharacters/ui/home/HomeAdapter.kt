package com.guilherme.marvelcharacters.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.databinding.ItemListBinding
import com.guilherme.marvelcharacters.ui.model.CharacterVO

class HomeAdapter(private val onClickListener: (CharacterVO) -> Unit) : RecyclerView.Adapter<HomeAdapter.BindingHolder>() {

    var characters: List<CharacterVO> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return BindingHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun getItemId(position: Int) = position.toLong()

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val character = characters[position]
        holder.bind(character)
    }

    inner class BindingHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val binding: ItemListBinding = ItemListBinding.bind(item)

        fun bind(character: CharacterVO) {
            binding.textviewCharacter.text = character.name
            itemView.setOnClickListener { onClickListener(character) }
        }
    }
}