package dev.robert.rickandmorty.feature.characters.presentation.charactersdetails

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.robert.rickandmorty.core.utils.Resource
import dev.robert.rickandmorty.feature.characters.domain.model.CharacterDetails
import dev.robert.rickandmorty.feature.characters.domain.respository.CharactersRepository
import dev.robert.rickandmorty.feature.characters.presentation.characters.CharactersState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor (
    private val repository : CharactersRepository
) : ViewModel() {
    private val _characterDetailsState = mutableStateOf(CharacterDetailsState())
    val characterDetailsState = _characterDetailsState as State<CharacterDetailsState>

    private val handler = CoroutineExceptionHandler { _, throwable ->
        _characterDetailsState.value = characterDetailsState.value.copy(
            isLoading = false,
            errorMessage = throwable.message ?: "Unknown error"
        )
    }

    fun getCharacterDetails(id: Int) {
        viewModelScope.launch(handler){
            _characterDetailsState.value = characterDetailsState.value.copy(isLoading = true)
            repository.getCharacterById(id).collectLatest {
                when(it){
                    is Resource.Error -> {
                        _characterDetailsState.value = characterDetailsState.value.copy(
                            isLoading = false,
                            errorMessage = it.message
                        )
                    }
                    is Resource.Success -> {
                        _characterDetailsState.value = characterDetailsState.value.copy(
                            isLoading = false,
                            data = it.data
                        )
                    }
                    else -> {
                        characterDetailsState
                    }
                }
            }
        }
    }
}

data class CharacterDetailsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data : CharacterDetails? = null
)