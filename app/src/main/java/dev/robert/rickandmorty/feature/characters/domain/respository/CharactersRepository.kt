package dev.robert.rickandmorty.feature.characters.domain.respository

import androidx.paging.PagingData
import dev.robert.rickandmorty.core.utils.Resource
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import kotlinx.coroutines.flow.Flow

interface CharactersRepository {
    //suspend fun getCharacters(): Flow<PagingData<Characters>>
     fun getCharacters(): Flow<PagingData<Characters>>
}