package dev.robert.rickandmorty.feature.characters.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.remote.ApiService
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

}