package dev.robert.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.robert.rickandmorty.core.data.conveters.Converters
import dev.robert.rickandmorty.core.data.datasources.local.RickAndMortyDatabase
import dev.robert.rickandmorty.core.data.datasources.local.dao.RemoteKeysDao
import dev.robert.rickandmorty.core.utils.Constants
import dev.robert.rickandmorty.feature.characters.data.localdatasource.dao.CharactersDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideConverters(gson: Gson) : Converters {
        return Converters(gson = gson)
    }

    @Provides
    @Singleton
    fun provideRickAndMortyDatabase(@ApplicationContext context: Context, converters: Converters) : RickAndMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickAndMortyDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .addTypeConverter(converters)
            .build()

    }

    @Provides
    @Singleton
    fun provideDao (db : RickAndMortyDatabase) : CharactersDao {
        return db.charactersDao()
    }

    @Provides
    @Singleton
    fun provideRemoteKeysDao(db: RickAndMortyDatabase) : RemoteKeysDao {
        return db.remoteKeysDao()
    }


}