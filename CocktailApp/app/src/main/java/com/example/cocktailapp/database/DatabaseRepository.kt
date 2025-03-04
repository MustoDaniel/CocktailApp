package com.example.cocktailapp.database

import com.example.cocktailapp.database.dao.CocktailDao
import com.example.cocktailapp.database.dao.Cocktail_IngredientDao
import com.example.cocktailapp.database.dao.IngredientDao
import com.example.cocktailapp.database.dao.SavedCocktailDao
import com.example.cocktailapp.database.entities.Cocktail
import com.example.cocktailapp.database.entities.Cocktail_Ingredient
import com.example.cocktailapp.database.entities.Ingredient
import com.example.cocktailapp.database.entities.SavedCocktail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository(
    private val cocktailDao: CocktailDao,
    private val ingredientDao: IngredientDao,
    private val savedCocktailDao: SavedCocktailDao,
    private val cocktail_IngredientDao: Cocktail_IngredientDao
){
    //cocktail
    suspend fun insertCocktail(cocktail: Cocktail) = withContext(Dispatchers.IO){
        cocktailDao.insert(cocktail)
    }

    //ingredient
    suspend fun insertIngredient(ingredient: Ingredient) = withContext(Dispatchers.IO){
        ingredientDao.insert(ingredient)
    }
    suspend fun getIngredient(id: Int): Ingredient? = withContext(Dispatchers.IO){
        ingredientDao.getIngredient(id)
    }

    //saved cocktail
    suspend fun insertSavedCocktail(savedCocktail: SavedCocktail) = withContext(Dispatchers.IO){
        savedCocktailDao.insert(savedCocktail)
    }
    suspend fun updateSavedCocktail(savedCocktail: SavedCocktail) = withContext(Dispatchers.IO){
        savedCocktailDao.update(savedCocktail)
    }
    suspend fun deleteSavedCocktail(savedCocktail: SavedCocktail) = withContext(Dispatchers.IO){
        savedCocktailDao.delete(savedCocktail)
    }
    suspend fun getSavedCocktails(): List<Int> = withContext(Dispatchers.IO){
        savedCocktailDao.getSavedCocktails()
    }

    //cocktail_ingredient
    suspend fun insertCocktail_Ingredient(cocktailIngredient: Cocktail_Ingredient) = withContext(Dispatchers.IO){
        cocktail_IngredientDao.insert(cocktailIngredient)
    }
    suspend fun getIngredientByCocktailName(cocktailName: String): List<String> = withContext(Dispatchers.IO){
        cocktail_IngredientDao.getIngredientsByCocktailName(cocktailName)
    }

}