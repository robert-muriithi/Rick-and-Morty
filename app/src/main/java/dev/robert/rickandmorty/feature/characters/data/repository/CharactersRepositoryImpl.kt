package dev.robert.rickandmorty.feature.characters.data.repository

import androidx.paging.*
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.remote.ApiService
import dev.robert.rickandmorty.core.utils.Resource
import dev.robert.rickandmorty.feature.characters.data.mappers.toDomain
import dev.robert.rickandmorty.feature.characters.data.mediator.CharactersRemoteMediator
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

        val pager = Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            remoteMediator = CharactersRemoteMediator(
                api,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }

        return pager
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 20
    }

   /* @ExperimentalPagingApi
    override suspend fun getCharacters(): Flow<Resource<PagingData<Characters>>> {
        val pagingSourceFactory = { db.charactersDao().getCharacters() }
        return flow {
            emit(Resource.Loading())
            val pager = Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                    enablePlaceholders = false
                ),
                remoteMediator = CharactersRemoteMediator(
                    api,
                    db
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }
            emit(Resource.Success(pager))
        }
    }*/

    /*@ExperimentalPagingApi
    override  fun getCharacters(): Flow<Resource<PagingData<Characters>>> {
        val pagingSourceFactory = { db.charactersDao().getCharacters() }
        return flow {
            emit(Resource.Loading())
            val pager = Pager(
                config = PagingConfig(
                    pageSize = NETWORK_PAGE_SIZE,
                    maxSize = NETWORK_PAGE_SIZE + (NETWORK_PAGE_SIZE * 2),
                    enablePlaceholders = false
                ),
                remoteMediator = CharactersRemoteMediator(
                    api,
                    db
                ),
                pagingSourceFactory = pagingSourceFactory
            ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }

            *//*pager.flowOn(Dispatchers.IO).collectLatest {
                Timber.d("getCharacters: $it")
                emit(Resource.Success(it))
            }*//*
        }
    }*/

}

