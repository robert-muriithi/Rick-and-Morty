package dev.robert.rickandmorty.core.data.conveters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto


@ProvidedTypeConverter
class Converters(
    private val gson: Gson
) {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val objects = gson.fromJson(value, Array<String>::class.java) as Array<String>
        return objects.toList()
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromLocationDto(value: LocationDto): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLocationDto(value: String): LocationDto {
        return gson.fromJson(value, LocationDto::class.java)
    }

    @TypeConverter
    fun fromOriginDto(value: OriginDto): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toOriginDto(value: String): OriginDto {
        return gson.fromJson(value, OriginDto::class.java)
    }
}