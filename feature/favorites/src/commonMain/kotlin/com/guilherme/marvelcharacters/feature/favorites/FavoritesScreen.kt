package com.guilherme.marvelcharacters.feature.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.Resources
import com.guilherme.marvelcharacters.core.ui.SnackbarMessageMP
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    onCharacterClick: (character: Character) -> Unit = {},
    onShowSnackbar: suspend (SnackbarMessageMP) -> Boolean = { _ -> false }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoritesContent(
        state = state,
        onCharacterClick = onCharacterClick,
        onDeleteAllClick = viewModel::onDeleteAllClick,
        onShowSnackbar = onShowSnackbar,
        onSnackbarShown = viewModel::onSnackbarShown
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesContent(
    state: FavoritesState,
    onCharacterClick: (character: Character) -> Unit = {},
    onDeleteAllClick: () -> Unit = {},
    onShowSnackbar: suspend (SnackbarMessageMP) -> Boolean = { _ -> false },
    onSnackbarShown: () -> Unit = {}
) {
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let {
            onShowSnackbar.invoke(it)
            onSnackbarShown.invoke()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(Resources.String.Favorites)) },
            actions = {
                if (state.characters.isEmpty()) return@TopAppBar
                IconButton(onClick = { isDropDownExpanded = !isDropDownExpanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = { isDropDownExpanded = !isDropDownExpanded }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Resources.String.DeleteDialogTitle)) },
                        onClick = {
                            isDialogOpen = !isDialogOpen
                            isDropDownExpanded = !isDropDownExpanded
                        }
                    )
                }
            }
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.characters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCharacterClick.invoke(it) }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = it.name,
                        fontSize = 16.sp
                    )
                    HorizontalDivider()
                }
            }
        }
        if (isDialogOpen) {
            DeleteAllCharactersDialog(
                onDismissRequest = { isDialogOpen = false },
                onDeleteAllClick = {
                    onDeleteAllClick.invoke()
                    isDialogOpen = false
                }
            )
        }
    }
}

@Composable
internal fun DeleteAllCharactersDialog(
    onDismissRequest: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDeleteAllClick) {
                Text(stringResource(Resources.String.Delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(Resources.String.Cancel))
            }
        },
        title = { Text(stringResource(Resources.String.DeleteDialogTitle)) },
        text = { Text(stringResource(Resources.String.DeleteDialogMessage)) }
    )
}
