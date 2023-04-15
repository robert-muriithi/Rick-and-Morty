package dev.robert.rickandmorty.feature.characters.presentation.characters

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.ramcosta.composedestinations.annotation.Destination
import dev.robert.rickandmorty.core.presentation.designsystem.theme.gilroyRegularFont
import dev.robert.rickandmorty.core.utils.UiEvents
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import kotlinx.coroutines.flow.collectLatest

@Destination(start = true)
@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val state = viewModel.charactersState.value
    val scaffoldState = rememberScaffoldState()
    val characters = state.characters?.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true, block = {
        viewModel.events.collectLatest { UiEvents ->
            when (UiEvents) {
                is UiEvents.ErrorEvent -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = UiEvents.message)
                }
            }
        }
    })

    CharactersScreenContent(scaffoldState = scaffoldState, state = state, characters = characters/*navigator = navigator*/)
}



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CharactersScreenContent(
    scaffoldState: ScaffoldState,
    state: CharactersState,
    characters: LazyPagingItems<Characters>?,
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Characters", fontFamily = gilroyRegularFont)
                },
            )
        }
    ) {
        if (characters != null) {
            CharactersList(
                characters = characters,
                isLoading = state.isLoading,
                state = state,
                onCharacterClick = { character ->
                    //navigator.navigate(Routes.CharacterDetailsScreen(character.id))
                }
            )
        }
    }
}

@Composable
fun CharactersList(
    characters: LazyPagingItems<Characters>,
    isLoading: Boolean,
    onCharacterClick: (Characters) -> Unit,
    state: CharactersState
) {
    val lazyListState = rememberLazyListState()
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn (
            state = lazyListState
                ){
            itemsIndexed(characters) { index, character ->
                character?.let {
                    CharacterItem(character = it, onCharacterClick = onCharacterClick)
                }
            }
            when {
                characters.loadState.refresh is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                characters.loadState.append is LoadState.Loading -> {
                    item { CircularProgressIndicator() }
                }
                characters.loadState.refresh is LoadState.Error -> {
                    val e = characters.loadState.refresh as LoadState.Error
                    item {
                        Text(text = e.error.localizedMessage ?: "Unknown error")
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
}


@Composable
fun CharacterItem(character: Characters, onCharacterClick: (Characters) -> Unit) {
    Card(
    ) {
        Column {
            Text(text = character.name, fontFamily = gilroyRegularFont)
        }
    }
}
