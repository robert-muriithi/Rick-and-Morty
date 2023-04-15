package dev.robert.rickandmorty.feature.characters.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.rickandmorty.core.utils.Resource
import dev.robert.rickandmorty.core.utils.UiEvents
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import dev.robert.rickandmorty.feature.characters.domain.respository.CharactersRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {

    private val _charactersState = mutableStateOf(CharactersState())
     val charactersState = _charactersState as State<CharactersState>

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val events = _eventsFlow

    private val handler = CoroutineExceptionHandler { _, throwable ->
        _charactersState.value = charactersState.value.copy(
            isLoading = false,
            message = throwable.message ?: "Unknown error"
        )
    }

     private fun getCharacters(){
      viewModelScope.launch(handler) {
            _charactersState.value = charactersState.value.copy(isLoading = true)
            val response = charactersRepository.getCharacters()
           _charactersState.value = CharactersState(
                isLoading = false,
                characters = response
           )
        }
    }

    init {
        getCharacters()
    }

}

data class CharactersState(
    val isLoading: Boolean = false,
    val characters: Flow<PagingData<Characters>>? = null,
    val message: String = ""
)