package dev.robert.rickandmorty.feature.characters.presentation.characters

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ramcosta.composedestinations.annotation.Destination
import dev.robert.rickandmorty.core.presentation.common.CollapsingToolbar
import dev.robert.rickandmorty.core.presentation.designsystem.theme.gilroyRegularFont
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

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
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun CharacterScreenContent1(
    characters: LazyPagingItems<Characters>,
    scaffoldState: ScaffoldState
) {

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    var isFabVisible by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(true) }


    Scaffold(
        scaffoldState = scaffoldState,
        /*topBar = {
            CollapsingToolbar(height = 64.dp) {
                Text(text = "Characters", fontFamily = gilroyRegularFont)
            }
        },*/
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

        Box(modifier = Modifier.fillMaxSize()) {
            if (characters.loadState.refresh is LoadState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.Start,
                    state = lazyListState,
                ) {
                    item {

                    }
                    items(characters) { character ->
                        character?.let {
                            CharacterItem(
                                character = it /*onCharacterClick = onCharacterClick*/,
                                modifier = Modifier.height(100.dp)
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

        }
    }

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect {
                isFabVisible = it > 0
                isExpanded = it == 0
            }
    }

}

