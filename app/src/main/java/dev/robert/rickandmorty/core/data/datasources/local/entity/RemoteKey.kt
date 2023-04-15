package dev.robert.rickandmorty.core.data.datasources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKey(
    @PrimaryKey val id: Int,
    val nextPageKey: Int?,
    val prevPageKey: Int?
)
