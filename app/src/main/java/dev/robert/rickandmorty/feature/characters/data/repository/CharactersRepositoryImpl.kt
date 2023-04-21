package dev.robert.rickandmorty.feature.characters.data.repository

import androidx.paging.*
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.remote.ApiService
import dev.robert.rickandmorty.feature.characters.data.mappers.toDomain
import dev.robert.rickandmorty.core.data.datasources.remote.mediator.CharactersRemoteMediator
import dev.robert.rickandmorty.core.utils.Resource
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import dev.robert.rickandmorty.feature.characters.domain.respository.CharactersRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class CharactersRepositoryImpl
@Inject constructor(
    private val api: ApiService,
    private val db: RickAndMortyDatabase
): CharactersRepository {

    @ExperimentalPagingApi
    override  fun getCharacters():  Flow<PagingData<Characters>>   {

        val pagingSourceFactory = { db.charactersDao().getCharacters() }
        val charactersRemoteMediator = CharactersRemoteMediator(api, db)

        val pager = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = charactersRemoteMediator,
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

        return pager
    }

    override suspend fun getCharacterById(id: Int): Flow<Resource<Characters>> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }
}

