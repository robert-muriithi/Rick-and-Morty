package dev.robert.rickandmorty.core.data.dto


import com.google.gson.annotations.SerializedName

data class LocationResponseDto(
    @SerializedName("info")
    val info: InfoDto,
    @SerializedName("results")
    val locationResultDtos: List<LocationResultDto>
)