package dev.robert.rickandmorty.core.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.robert.rickandmorty.core.data.conveters.Converters
import dev.robert.rickandmorty.core.data.datasources.local.dao.RemoteKeysDao
import dev.robert.rickandmorty.core.data.datasources.local.entity.RemoteKey
import dev.robert.rickandmorty.feature.characters.data.localdatasource.dao.CharactersDao
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity

@TypeConverters(Converters::class)
@Database(entities = [CharactersEntity::class, RemoteKey::class], version = 1, exportSchema = false)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}