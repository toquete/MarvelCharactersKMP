package com.guilherme.marvelcharacters.feature.home

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.marvelcharacters.core.model.Character
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    nightViewModel: NightModeViewModel = koinViewModel(),
    onCharacterClick: (character: Character) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeContent(
        state = state,
        onCharacterClick = onCharacterClick,
        onSearchButtonClick = viewModel::onSearchCharacter,
        onNightModeButtonClick = nightViewModel::toggleDarkMode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeContent(
    state: HomeState,
    onCharacterClick: (character: Character) -> Unit = {},
    onSearchButtonClick: (query: String) -> Unit = {},
    onNightModeButtonClick: () -> Unit = {}
) {
    var characterText by rememberSaveable { mutableStateOf("") }
    val isSearchButtonEnabled by remember {
        derivedStateOf {
            characterText.isNotEmpty()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
            actions = {
                IconButton(onClick = onNightModeButtonClick) {
                    Icon(
                        imageVector = Icons.Default.Brightness4,
                        contentDescription = null
                    )
                }
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(0.7f),
                value = characterText,
                onValueChange = { characterText = it },
                label = { Text("Character") },
                trailingIcon = {
                    if (characterText.isNotEmpty()) {
                        IconButton(onClick = { characterText = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true,
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions {
                    onSearchButtonClick.invoke(characterText)
                }
            )
            Button(
                modifier = Modifier.weight(0.3f),
                onClick = { onSearchButtonClick.invoke(characterText) },
                enabled = isSearchButtonEnabled
            ) {
                Text(stringResource(R.string.search))
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.errorMessageId != null -> Text(text = stringResource(state.errorMessageId))
                else -> {
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
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        state = HomeState(
            characters = listOf(
                Character(
                    id = 1,
                    name = "Spider-Man",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    thumbnail = ""
                ),
                Character(
                    id = 2,
                    name = "Hulk",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    thumbnail = ""
                )
            )
        )
    )
}