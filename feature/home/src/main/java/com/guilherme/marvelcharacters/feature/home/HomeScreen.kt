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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
internal fun HomeRoute(
    viewModel: HomeViewModel = koinViewModel(),
    onCharacterClick: (character: Character) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreen(
        state = state,
        onCharacterClick = onCharacterClick,
        onSearchButtonClick = viewModel::onSearchCharacter
    )
}

@Composable
internal fun HomeScreen(
    state: HomeState,
    onCharacterClick: (character: Character) -> Unit = {},
    onSearchButtonClick: (query: String) -> Unit = {},
) {
    val textFieldState = rememberTextFieldState("")
    var isSearchButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(textFieldState) {
        isSearchButtonEnabled = textFieldState.text.isNotEmpty()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                state = textFieldState,
                label = { Text("Character") },
                trailingIcon = {
                    if (textFieldState.text.isNotEmpty()) {
                        IconButton(onClick = { textFieldState.clearText() }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                },
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true,
                    imeAction = ImeAction.Search,
                ),
                onKeyboardAction = {
                    onSearchButtonClick.invoke(textFieldState.text.toString())
                }
            )
            Button(
                modifier = Modifier.weight(0.2f),
                onClick = { onSearchButtonClick.invoke(textFieldState.text.toString()) },
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
fun HomeScreenPreview() {
    HomeScreen(
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