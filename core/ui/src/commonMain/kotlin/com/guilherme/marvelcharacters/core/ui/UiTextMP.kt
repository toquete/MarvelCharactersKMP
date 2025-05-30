package com.guilherme.marvelcharacters.core.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed class UiTextMP {
    data class DynamicString(val value: String): UiTextMP()
    data class ResourceString(val resource: StringResource, val args: List<Any> = emptyList()): UiTextMP()

    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is ResourceString -> stringResource(resource, *args.toTypedArray())
        }
    }

    suspend fun asStringText(): String {
        return when(this) {
            is DynamicString -> value
            is ResourceString -> getString(resource, *args.toTypedArray())
        }
    }
}