package dev.robert.rickandmorty.feature.characters.presentation.characters

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.annotation.Destination
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto
import dev.robert.rickandmorty.core.presentation.designsystem.theme.RickAndMortyTheme
import dev.robert.rickandmorty.core.presentation.designsystem.theme.gilroyRegularFont
import dev.robert.rickandmorty.feature.characters.domain.model.Characters

@Destination(start = true)
@Composable
fun CharactersScreen(
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

    CharacterScreenContent1(character, scaffoldState = scaffoldState)

    //CharactersScreenContent(scaffoldState = scaffoldState, state = state, characters = characters/*navigator = navigator*/)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CharacterScreenContent1(
    character: LazyPagingItems<Characters>,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Characters", fontFamily = gilroyRegularFont)
                },
            )
        }) {
        val lazyListState = rememberLazyListState()
        Box(modifier = Modifier.fillMaxSize()) {
            if (character.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start,
                    state = lazyListState
                ) {
                    items(character) { character ->
                        character?.let {
                            CharacterItem(character = it /*onCharacterClick = onCharacterClick*/, modifier = Modifier.height(100.dp))
                        }
                    }
                    when {
                        character.loadState.append is LoadState.Loading -> {
                            item {
                                CircularProgressIndicator()
                            }
                        }
                        character.loadState.append is LoadState.Error -> {
                            val e = character.loadState.append as LoadState.Error
                            item {
                                Text(text = e.error.localizedMessage ?: "Unknown error")
                            }
                        }
                    }
                }
            }

        }
    }

}


/*
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
*/

/*@Composable
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
                    CharacterItem(character = it, *//*onCharacterClick = onCharacterClick*//*)
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
}*/


@Composable
fun CharacterItem(character: Characters, /*onCharacterClick: (Characters) -> Unit*/ modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = 2.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            CoilImage(
                imageModel = {
                    character.image
                },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                ),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                },
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.h6,
                        fontFamily = gilroyRegularFont
                    )
                    Text(
                        text = "Status: ${character.status}",
                        fontStyle = FontStyle.Italic,
                        color = Color.LightGray,
                        fontFamily = gilroyRegularFont,
                        modifier = Modifier.padding(3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Created on ${character.created}",
                    fontStyle = FontStyle.Italic,
                    color = Color.LightGray,
                    fontFamily = gilroyRegularFont,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = character.species,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = gilroyRegularFont
                )
            }
        }
    }
}

@Preview
@Composable
fun CharacterItemPreview() {
    RickAndMortyTheme {
        CharacterItem(
            character = Characters(
                created = "12/2020",
                episode = listOf(
                    "Ep 01",
                    "Ep o2"
                ),
                gender = "Male",
                id = 1,
                image = "vdjhbvjbd",
                locationDto = LocationDto(
                    name = "Space X",
                    url = "www.rick.com"
                ),
                name = "Rick",
                originDto = OriginDto(
                    name = "Space X",
                    url = "www.rick.com"
                ),
                species = "Unknown",
                status = "Alive",
                type = "Human",
                url = "W",
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
