package com.guilherme.marvelcharacters.core.ui

import com.guilherme.marvelcharacters.resources.Res
import com.guilherme.marvelcharacters.resources.cancel
import com.guilherme.marvelcharacters.resources.character_added
import com.guilherme.marvelcharacters.resources.character_deleted
import com.guilherme.marvelcharacters.resources.characters_deleted
import com.guilherme.marvelcharacters.resources.delete
import com.guilherme.marvelcharacters.resources.delete_dialog_message
import com.guilherme.marvelcharacters.resources.delete_dialog_title
import com.guilherme.marvelcharacters.resources.error_message
import com.guilherme.marvelcharacters.resources.favorites
import com.guilherme.marvelcharacters.resources.no_description_available
import com.guilherme.marvelcharacters.resources.undo

object Resources {
    object String {
        val NoDescriptionAvailable = Res.string.no_description_available
        val CharacterAdded = Res.string.character_added
        val CharacterDeleted = Res.string.character_deleted
        val CharactersDeleted = Res.string.characters_deleted
        val Undo = Res.string.undo
        val ErrorMessage = Res.string.error_message
        val Favorites = Res.string.favorites
        val DeleteDialogTitle = Res.string.delete_dialog_title
        val DeleteDialogMessage = Res.string.delete_dialog_message
        val Delete = Res.string.delete
        val Cancel = Res.string.cancel
    }
}