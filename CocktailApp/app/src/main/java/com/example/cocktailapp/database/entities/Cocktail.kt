package com.example.cocktailapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cocktail(
    @PrimaryKey
    val id: Int,
    val name: String,
)
