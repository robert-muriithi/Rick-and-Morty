package dev.robert.rickandmorty.core.data.datasources.remote

import dev.robert.rickandmorty.core.data.dto.CharacterDetailsResponseDto
import dev.robert.rickandmorty.core.data.dto.CharacterResponseDto
import dev.robert.rickandmorty.core.data.dto.LocationResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //Get all characters
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int? = null,
    ): CharacterResponseDto

    //Get all locations
    @GET("location")
    suspend fun getAllLocations(
        @Query("page") page: Int? = null,
    ): LocationResponseDto

    //Get Single Character
    @GET("character/{character_id}")
    suspend fun getCharacterDetails(
        @Path("character_id") characterId: Int
    ) : CharacterDetailsResponseDto
}