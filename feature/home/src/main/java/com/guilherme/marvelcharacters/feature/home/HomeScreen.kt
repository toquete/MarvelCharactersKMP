package com.guilherme.marvelcharacters.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.CharacterListItem

@Composable
fun HomeRoute(onCharacterClick: (Character) -> Unit) {
    val viewModel: HomeViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    HomeScreen(
        state = state,
        onSearchButtonClick = viewModel::onSearchCharacter,
        onCharacterClick = onCharacterClick
    )
}

@Composable
internal fun HomeScreen(
    state: HomeUiState,
    onSearchButtonClick: (String) -> Unit = {},
    onCharacterClick: (Character) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var query by rememberSaveable { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(3f),
                value = query,
                onValueChange = { query = it },
                label = { Text(text = stringResource(R.string.character)) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(imageVector = Icons.Filled.Cancel, contentDescription = null)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                    onSearchButtonClick(query)
                },
                maxLines = 1
            )
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    focusManager.clearFocus()
                    onSearchButtonClick(query)
                },
                enabled = query.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.search))
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when (state) {
                HomeUiState.Empty -> {
                    HomeText(
                        modifier = Modifier.align(Alignment.Center),
                        textId = R.string.start_message
                    )
                }
                is HomeUiState.Error -> {
                    HomeText(
                        modifier = Modifier.align(Alignment.Center),
                        textId = state.errorMessageId
                    )
                }
                HomeUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is HomeUiState.Success -> {
                    LazyColumn {
                        items(state.characters) { character ->
                            CharacterListItem(
                                name = character.name,
                                onCharacterClick = { onCharacterClick(character) }
                            )
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeText(
    modifier: Modifier = Modifier,
    @StringRes textId: Int
) {
    Text(
        modifier = modifier.padding(horizontal = 16.dp),
        text = stringResource(textId),
        style = MaterialTheme.typography.body1
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        state = HomeUiState.Success(
            characters = listOf(
                Character(
                    id = 0,
                    name = "Spider-Man",
                    description = "",
                    thumbnail = ""
                )
            )
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenEmptyPreview() {
    HomeScreen(state = HomeUiState.Empty)
}