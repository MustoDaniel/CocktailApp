package com.example.cocktailapp.database

import android.content.Context

class CocktailDBService(context: Context) {
    val database: CocktailDB by lazy {
        CocktailDB.getDatabase(context)
    }
    val repository: DatabaseRepository by lazy {
        DatabaseRepository(
            database.cocktailDao(),
            database.ingredientDao(),
            database.savedCocktailDao(),
            database.cocktail_IngredientDao()
        )
    }
}