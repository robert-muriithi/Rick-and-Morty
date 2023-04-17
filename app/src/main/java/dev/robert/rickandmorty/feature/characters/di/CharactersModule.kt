package dev.robert.rickandmorty.feature.characters.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.remote.ApiService
import dev.robert.rickandmorty.core.data.datasources.remote.mediator.CharactersRemoteMediator
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity
import dev.robert.rickandmorty.feature.characters.data.repository.CharactersRepositoryImpl
import dev.robert.rickandmorty.feature.characters.domain.respository.CharactersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CharactersModule {

    @Provides
    @Singleton
    fun providesCharactersRepository(
        api: ApiService,
        db: RickAndMortyDatabase
    ) : CharactersRepository {
        return CharactersRepositoryImpl(api = api, db = db)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideCharactersPager(db: RickAndMortyDatabase, apiService: ApiService) : Pager<Int, CharactersEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = CharactersRemoteMediator(
                apiService = apiService,
                appDb = db
            ),
            pagingSourceFactory = {
                db.charactersDao().getCharacters()
            }
        )
    }

}