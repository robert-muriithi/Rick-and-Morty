package dev.robert.rickandmorty.feature.characters.data.localdatasource.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.robert.rickandmorty.core.utils.Constants.CHARACTER_TABLE_NAME
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity

@Dao
interface CharactersDao {
    //Insert all characters
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharactersEntity>)

    //Insert single character
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharactersEntity)

    //Get all characters
    @Query("SELECT * FROM $CHARACTER_TABLE_NAME")
    fun getCharacters(): PagingSource<Int, CharactersEntity>

    //Delete all characters
    @Query("DELETE FROM $CHARACTER_TABLE_NAME")
    suspend fun deleteCharacters()
}