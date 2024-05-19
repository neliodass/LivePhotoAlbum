package com.example.livealbum

import java.time.LocalDate


data class LiveImage(
    val id: Int,
    val live: Boolean = false,
    val resource: Int,
    val description: String,
    val location: String,
    val date: LocalDate
) {

}