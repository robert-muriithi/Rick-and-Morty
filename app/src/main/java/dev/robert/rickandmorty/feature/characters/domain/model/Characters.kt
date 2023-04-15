package dev.robert.rickandmorty.feature.characters.domain.model


import android.os.Parcelable
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class Characters(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val locationDto: LocationDto,
    val name: String,
    val originDto: OriginDto,
    val species: String,
    val status: String,
    val type: String,
    val url: String
) : Parcelable
