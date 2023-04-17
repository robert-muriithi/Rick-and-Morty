package dev.robert.rickandmorty.feature.characters.presentation.characters

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity
import dev.robert.rickandmorty.feature.characters.data.mappers.toDomain
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import dev.robert.rickandmorty.feature.characters.domain.respository.CharactersRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository,
    /**
     *     Injecting pager here exposes CharacterEntity from data layer.
     *     This violates clean architecture principles
     */
    pager : Pager<Int, CharactersEntity>
) : ViewModel() {

    /**
     * This works well but violates clean architecture guidelines
     */
    val characters = pager
        .flow
        .map { pagingData->
            pagingData.map { charactersEntity ->
                charactersEntity.toDomain()
            }
        }
        .cachedIn(viewModelScope)






    /**
     * This function needs to be troubleshooted. Works fine but upon appending more items on the list,
     * scrolls upwards. It is not in use right now
     */

    private val _charactersState = mutableStateOf(CharactersState())
     val charactersState = _charactersState as State<CharactersState>

    private val handler = CoroutineExceptionHandler { _, throwable ->
        _charactersState.value = charactersState.value.copy(
            isLoading = false,
            message = throwable.message ?: "Unknown error"
        )
    }

    /**
     * This function needs to be troubleshooted. Works fine but upon appending more items on the list,
     * scrolls upwards.  It is not in use right now
     */
     private fun getCharacters(){
      viewModelScope.launch(handler) {
            _charactersState.value = charactersState.value.copy(isLoading = true)
            val response = charactersRepository.getCharacters().cachedIn(viewModelScope)
           _charactersState.value = CharactersState(
              characters = response,
               isLoading = false
          )
        }
    }

    /*init {
        getCharacters()
    }*/

}

data class CharactersState(
    val isLoading: Boolean = false,
    val characters: Flow<PagingData<Characters>>? = null,
    val message: String = ""
)
