package com.guilherme.marvelcharacters.ui.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.model.Image

@Composable
fun DetailRoute(
    viewModel: DetailComposeViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    DetailScreen(
        state,
        onNavigationButtonClick = navController::popBackStack
    )
}

@Composable
fun DetailScreen(
    state: DetailComposeState,
    onNavigationButtonClick: () -> Unit = {},
    onActionButtonClick: (Int) -> Unit = {}
) {
    if (state.character != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(state.character.name) },
                navigationIcon = {
                    IconButton(onClick = onNavigationButtonClick) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { onActionButtonClick(state.character.id) }) {
                        Icon(
                            imageVector = if (state.character.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null
                        )
                    }
                }
            )
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                text = state.character.description.ifEmpty {
                    stringResource(R.string.no_description_available)
                }
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MdcTheme {
        DetailScreen(
            state = DetailComposeState(
                character = Character(
                    id = 0,
                    name = "Spider-Man",
                    description = "The Amazing Spider-Man",
                    thumbnail = Image(path = "", extension = "")
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenLoadingPreview() {
    MdcTheme {
        DetailScreen(
            state = DetailComposeState(character = null)
        )
    }
}