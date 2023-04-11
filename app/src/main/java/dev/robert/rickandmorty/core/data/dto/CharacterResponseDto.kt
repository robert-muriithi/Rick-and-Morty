package dev.robert.rickandmorty.core.data.dto


import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("info")
    val info: InfoDto,
    @SerializedName("results")
    val resultDtos: List<CharacterResultDto>
)