package com.guilherme.marvelcharacters.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String): UiText()
    data class ResourceString(@StringRes val id: Int, val args: List<Any> = emptyList()): UiText()

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is ResourceString -> stringResource(id, *args.toTypedArray())
        }
    }

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is ResourceString -> context.getString(id, *args.toTypedArray())
        }
    }
}