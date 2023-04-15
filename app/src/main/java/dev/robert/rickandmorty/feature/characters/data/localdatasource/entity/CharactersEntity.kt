package dev.robert.rickandmorty.feature.characters.data.localdatasource.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import dev.robert.rickandmorty.core.data.dto.CharacterResultDto
import dev.robert.rickandmorty.core.data.dto.InfoDto
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto
import dev.robert.rickandmorty.core.utils.Constants.CHARACTER_TABLE_NAME

@Entity(tableName = CHARACTER_TABLE_NAME)
data class CharactersEntity(
    val created: String,
    val episode: List<String>,
    val gender: String,
    @PrimaryKey val id: Int,
    val image: String,
    val locationDto: LocationDto,
    val name: String,
    val originDto: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)
