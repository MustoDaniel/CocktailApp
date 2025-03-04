package com.example.cocktailapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.cocktailapp.database.entities.Cocktail
import retrofit2.http.Query

@Dao
interface CocktailDao {
    @Insert
    suspend fun insert(cocktail: Cocktail)

    //TODO : Delete, Update o Query
}