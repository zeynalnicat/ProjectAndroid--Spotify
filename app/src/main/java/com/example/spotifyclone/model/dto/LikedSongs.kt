package com.example.spotifyclone.model.dto

data class LikedSongs(
    val name: String,
    val artist: String,
    val imgUri: String,
    val uri: String,
    var isPlayed: Boolean = false
)
