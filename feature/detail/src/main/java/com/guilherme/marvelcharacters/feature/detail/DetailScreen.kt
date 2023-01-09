package com.guilherme.marvelcharacters.feature.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import kotlinx.coroutines.launch

private val headerHeight = 256.dp
private val toolbarHeight = 56.dp
private val paddingMedium = 16.dp
private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp
private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f

@Composable
fun DetailRoute(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val viewModel: DetailViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    DetailScreen(
        state = state,
        scaffoldState = scaffoldState,
        onFabClick = viewModel::onFabClick,
        onSnackbarShown = viewModel::onSnackbarShown
    )
}

@Composable
internal fun DetailScreen(
    state: DetailUiState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onFabClick: (Boolean) -> Unit = {},
    onSnackbarShown: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    when (state) {
        DetailUiState.Loading -> {
            Box {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is DetailUiState.ShowSnackbar -> {
            state.messageId?.let { id ->
                val message = stringResource(id)
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(message)
                    onSnackbarShown()
                }
            }
        }
        is DetailUiState.Success -> {
            DetailContent(
                favoriteCharacter = state.character,
                onFabClick = onFabClick
            )
        }
    }
}

@Composable
@OptIn(ExperimentalAnimationApi::class)
private fun DetailContent(
    favoriteCharacter: FavoriteCharacter,
    onFabClick: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val showFab by remember {
        derivedStateOf {
            scrollState.value == 0
        }
    }
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight)
                    .background(MaterialTheme.colors.background)
                    .graphicsLayer {
                        translationY = 0.5f * scrollState.value
                        alpha = 1f - ((scrollState.value.toFloat() / scrollState.maxValue) * 1.5f)
                    }
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(R.drawable.teste),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
                    .padding(paddingMedium),
                text = favoriteCharacter.character.description.ifEmpty { stringResource(R.string.no_description_available) },
                textAlign = TextAlign.Justify,
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.body1
            )
        }
        Toolbar(
            scroll = scrollState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
        Title(
            text = favoriteCharacter.character.name,
            scroll = scrollState,
            headerHeightPx = headerHeightPx,
            toolbarHeightPx = toolbarHeightPx
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomEnd),
            visible = showFab,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(paddingMedium),
                onClick = { onFabClick(favoriteCharacter.isFavorite) }
            ) {
                Icon(
                    imageVector = if (favoriteCharacter.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
private fun Toolbar(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    val toolbarBottom = headerHeightPx - toolbarHeightPx
    val showToolbar by remember {
        derivedStateOf {
            scroll.value >= toolbarBottom
        }
    }

    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        )
    }
}

@Composable
private fun Title(
    text: String,
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }

    Text(
        text = text,
        color = Color.White,
        fontSize = 30.sp,
        modifier = Modifier
            .graphicsLayer {
                val collapseRange: Float = (headerHeightPx - toolbarHeightPx)
                val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                val scaleXY = lerp(
                    titleFontScaleStart.dp,
                    titleFontScaleEnd.dp,
                    collapseFraction
                )

                val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                val titleYFirstInterpolatedPoint = lerp(
                    headerHeight - titleHeightPx.toDp() - paddingMedium,
                    headerHeight / 2,
                    collapseFraction
                )

                val titleXFirstInterpolatedPoint = lerp(
                    titlePaddingStart,
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    collapseFraction
                )

                val titleYSecondInterpolatedPoint = lerp(
                    headerHeight / 2,
                    toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                    collapseFraction
                )

                val titleXSecondInterpolatedPoint = lerp(
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    titlePaddingEnd - titleExtraStartPadding,
                    collapseFraction
                )

                val titleY = lerp(
                    titleYFirstInterpolatedPoint,
                    titleYSecondInterpolatedPoint,
                    collapseFraction
                )

                val titleX = lerp(
                    titleXFirstInterpolatedPoint,
                    titleXSecondInterpolatedPoint,
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                titleHeightPx = it.size.height.toFloat()
                titleWidthPx = it.size.width.toFloat()
            }
    )
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        state = DetailUiState.Success(
            character = FavoriteCharacter(
                character = Character(
                    id = 0,
                    name = "Spider-Man",
                    description = "Description",
                    thumbnail = "xablau.jpg"
                ),
                isFavorite = true
            )
        )
    )
}