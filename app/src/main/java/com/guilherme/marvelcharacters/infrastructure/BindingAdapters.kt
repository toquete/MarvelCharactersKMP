package com.guilherme.marvelcharacters.infrastructure

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView

object BindingAdapters {

    @BindingAdapter("items")
    @JvmStatic
    fun setItems(recyclerView: RecyclerView, items: List<Any>) {
        recyclerView.adapter.let {
            if (it is AdapterItemsContract) {
                it.replaceItems(items)
            }
        }
    }
}