package com.guilherme.marvelcharacters.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.guilherme.marvelcharacters.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val args: DetailActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.collapsingToolbarLayout.title = args.character.name
        binding.description.text = if (args.character.description.isEmpty()) {
            "No description available"
        } else {
            args.character.description
        }
        Glide.with(this)
            .load("${args.character.thumbnail.path}.${args.character.thumbnail.extension}")
            .centerCrop()
            .into(binding.imageView)
    }
}