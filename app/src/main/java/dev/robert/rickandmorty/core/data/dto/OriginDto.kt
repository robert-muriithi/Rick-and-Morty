package dev.robert.rickandmorty.core.data.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OriginDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
) : Parcelable
