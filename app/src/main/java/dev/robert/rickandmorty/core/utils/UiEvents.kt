package dev.robert.rickandmorty.core.utils

sealed class UiEvents {
    data class ErrorEvent(val message: String): UiEvents()
}