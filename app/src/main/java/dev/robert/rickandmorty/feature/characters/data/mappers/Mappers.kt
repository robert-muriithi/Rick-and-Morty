package dev.robert.rickandmorty.feature.characters.data.mappers

import dev.robert.rickandmorty.core.data.dto.CharacterResultDto
import dev.robert.rickandmorty.feature.characters.data.localdatasource.entity.CharactersEntity
import dev.robert.rickandmorty.feature.characters.domain.model.Characters

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

