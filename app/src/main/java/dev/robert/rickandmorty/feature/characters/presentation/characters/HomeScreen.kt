package dev.robert.rickandmorty.feature.characters.presentation.characters

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.annotation.Destination
import dev.robert.rickandmorty.R
import dev.robert.rickandmorty.core.presentation.designsystem.theme.PrimaryColor
import dev.robert.rickandmorty.core.presentation.designsystem.theme.PrimaryLightColor
import dev.robert.rickandmorty.core.presentation.designsystem.theme.SecondaryColor
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@Destination(start = true)
@Composable
fun CharactersMainScreen(
    viewModel: CharactersViewModel = hiltViewModel()
) {

    //val state = viewModel.charactersState.value
    val scaffoldState = rememberScaffoldState()
    //val characters = state.characters?.collectAsLazyPagingItems()
    val context = LocalContext.current
    val character = viewModel.characters.collectAsLazyPagingItems()

    LaunchedEffect(key1 = character.loadState, block = {
        if (character.loadState.refresh is LoadState.Error) {
            Toast.makeText(
                context,
                "Error" + (character.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_SHORT
            ).show()
        }

    })
    CharactersScreenContent(character, scaffoldState = scaffoldState)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CharactersScreenContent(
    characters: LazyPagingItems<Characters>,
    scaffoldState: ScaffoldState
) {
    val scroll: ScrollState = rememberScrollState(0)
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

    var isFabVisible by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                isFabVisible = it > 0
            }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        lazyListState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .alpha(if (isFabVisible) 1f else 0f)
            ) {
                Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Scroll to top")
            }

        }
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            if (characters.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else{
                Header(
                    scroll = scroll,
                    headerHeightPx = headerHeightPx,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                )
                Body(
                    scroll = scroll,
                    modifier = Modifier.fillMaxSize(),
                    characters = characters,
                    lazyListState = lazyListState
                )
                Toolbar(
                    scroll = scroll,
                    headerHeightPx = headerHeightPx,
                    toolbarHeightPx = toolbarHeightPx
                )
                Title(scroll = scroll)
            }
        }
    }
}


@Composable
fun Toolbar(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float,
    modifier: Modifier = Modifier
) {
    val toolbarBottom by remember {
        mutableStateOf(headerHeightPx - toolbarHeightPx)
    }

    val showToolbar by remember {
        derivedStateOf {
            scroll.value >= toolbarBottom
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(PrimaryColor, PrimaryLightColor)
                )
            ),
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            title = {},
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        )
    }

}
@Composable
fun Body(
    scroll: ScrollState,
    modifier: Modifier = Modifier,
    characters: LazyPagingItems<Characters>,
    lazyListState: LazyListState

) {
    Column(modifier = modifier.verticalScroll(scroll).height(50.dp)) {
        CharactersList(characters = characters, lazyListState = lazyListState)
    }

}

@Composable
fun CharactersList(
    characters: LazyPagingItems<Characters>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = headerHeight),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.Start,
        state = lazyListState
    ) {
        items(characters) { character ->
            character?.let {
                CharacterItem(
                    character = it /*onCharacterClick = onCharacterClick*/,
                    modifier = Modifier.height(100.dp).padding(2.dp)
                )
            }
        }
        when {
            characters.loadState.append is LoadState.Loading -> {
                item {
                    CircularProgressIndicator()
                }
            }
            characters.loadState.append is LoadState.Error -> {
                val e = characters.loadState.append as LoadState.Error
                item {
                    Text(text = e.error.localizedMessage ?: "Unknown error")
                }
            }
        }
    }
}

@Composable
fun Header(
    scroll: ScrollState,
    headerHeightPx: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = -scroll.value.toFloat() / 2f // Parallax effect
                alpha = (-1f / headerHeightPx) * scroll.value + 1
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.rick_and_morty),
            contentDescription = "Rick and Morty",
            contentScale = ContentScale.FillBounds
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, SecondaryColor),
                        startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                    )
                )
        )
    }
}

@Composable
private fun Title(
    scroll: ScrollState,
    modifier: Modifier = Modifier
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }

    Text(
        text = stringResource(id = R.string.characters),
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .graphicsLayer {
                val collapseRange: Float = (headerHeight.toPx() - toolbarHeight.toPx())
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










private val headerHeight = 250.dp
private val toolbarHeight = 56.dp

private val paddingMedium = 16.dp

private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f