package dev.robert.rickandmorty.core.data.datasources.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.local.entity.RemoteKey
import dev.robert.rickandmorty.core.data.datasources.remote.ApiService
import dev.robert.rickandmorty.feature.characters.data.localdatasource.dao.CharactersDao
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity
import dev.robert.rickandmorty.feature.characters.data.mappers.toEntityResults
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val apiService: ApiService,
    private val appDb: RickAndMortyDatabase,
) : RemoteMediator<Int, CharactersEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharactersEntity>
    ): MediatorResult {
        return try {
            val page = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->{
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if(lastItem == null){
                        1
                    }else{
                        (lastItem.id / state.config.pageSize) + 1
                    }
                }
            }
            val apiResponse = apiService.getAllCharacters(
                page = page
            )
            val characters = apiResponse.resultDtos

            appDb.withTransaction {
                if(loadType == LoadType.REFRESH){
                    appDb.charactersDao().deleteCharacters()
                }
                val charactersEntity = characters.map { it.toEntityResults() }
                appDb.charactersDao().insertCharacters(charactersEntity)
            }
            MediatorResult.Success(endOfPaginationReached = characters.isEmpty())
        }catch (e: HttpException){
            MediatorResult.Error(e)
        }
        catch (e: IOException){
            MediatorResult.Error(e)
        }
    }

    /*override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }*/

    /*override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharactersEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextPageKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                val prevKey = remoteKeys.prevPageKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                val nextKey = remoteKeys.nextPageKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                nextKey
            }
        }

        try {
            val apiResponse = apiService.getAllCharacters(page)
            val characters = apiResponse.resultDtos
            val endOfPaginationReached = characters.isEmpty()
            appDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDb.remoteKeysDao().clearRemoteKeys()
                    appDb.charactersDao().deleteCharacters()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = characters.map {
                    RemoteKey(it.id, nextKey, prevKey)
                }
                appDb.remoteKeysDao().insertAll(keys)
                appDb.charactersDao().insertCharacters(
                    characters.map {
                        it.toEntityResults()
                    }
                )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, CharactersEntity>
    ): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                appDb.remoteKeysDao().remoteKeysCharacterId(repoId)
            }
        }
    }

    private suspend  fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharactersEntity>
    ): RemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                appDb.remoteKeysDao().remoteKeysCharacterId(repo.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharactersEntity>
    ): RemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                appDb.remoteKeysDao().remoteKeysCharacterId(repo.id)
            }
    }*/

}