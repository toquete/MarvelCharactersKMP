package com.guilherme.marvelcharacters.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.data.model.Character

class MainAdapter(private var characters: List<Character>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)

        return BindingHolder(view)
    }

    override fun getItemCount() = characters.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as BindingHolder).bind(characters[position].name)
    }

    inner class BindingHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val characterItem = item.findViewById<TextView>(R.id.textview_character)

        fun bind(character: String) {
            characterItem.text = character
        }
    }
}