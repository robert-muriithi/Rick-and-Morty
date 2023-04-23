package dev.robert.rickandmorty.feature.characters.data.mappers

import dev.robert.rickandmorty.core.data.dto.CharacterDetailsResponseDto
import dev.robert.rickandmorty.core.data.dto.CharacterResultDto
import dev.robert.rickandmorty.core.data.dto.LocationDto
import dev.robert.rickandmorty.core.data.dto.OriginDto
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity
import dev.robert.rickandmorty.feature.characters.domain.model.CharacterDetails
import dev.robert.rickandmorty.feature.characters.domain.model.Characters
import dev.robert.rickandmorty.feature.characters.domain.model.Location
import dev.robert.rickandmorty.feature.characters.domain.model.Origin

fun CharacterResultDto.toEntityResults() : CharactersEntity {
    return CharactersEntity(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        locationDto = locationDto,
        name = name,
        originDto = originDto,
        species = species,
        status = status,
        type = type,
        url = url
    )
}

fun CharactersEntity.toDomain() : Characters {
    return Characters(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        locationDto = locationDto,
        name = name,
        originDto = originDto,
        species = species,
        status = status,
        type = type,
        url = url
    )
}

fun CharacterDetailsResponseDto.toDomain() : CharacterDetails {
    return CharacterDetails(
        created = created,
        episode = episode,
        gender = gender,
        id = id,
        image = image,
        location = location.toDomain(),
        name = name,
        origin = origin.toDomain(),
        species,
        status,
        type,
        url
    )
}

fun LocationDto.toDomain() : Location {
    return Location(
        name = name,
        url = url
    )
}

fun OriginDto.toDomain() : Origin {
    return Origin(
        name = name,
        url = url
    )
}

