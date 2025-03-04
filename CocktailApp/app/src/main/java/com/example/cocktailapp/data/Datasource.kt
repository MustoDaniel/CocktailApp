package com.example.cocktailapp.data

import Cocktail
import com.example.cocktailapp.api.RetrofitInstance

class Datasource() {
    suspend fun loadCocktails() : List<Cocktail>{
        val apiService = RetrofitInstance.api
        val cocktails = mutableListOf<Cocktail>()

        for(letter in 'a'..'z')
            cocktails.addAll(apiService.getCocktailsByFirstLetter( letter ).drinks ?: emptyList())

        return cocktails
    }
}