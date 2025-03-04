package com.example.cocktailapp.data

import com.example.cocktailapp.database.entities.Cocktail
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.example.cocktailapp.api.RetrofitInstance
import com.example.cocktailapp.database.CocktailDBService
import kotlinx.coroutines.launch

class Datasource() {

    //Funzione per ottenere i cocktail dalla a alla z usando l'api (che però per qualche motivo non li fornisce tutti)
    suspend fun loadCocktails() : List<Cocktail>{
        val apiService = RetrofitInstance.api
        val cocktails = mutableListOf<Cocktail>()

        for(letter in 'a'..'z')
            cocktails.addAll(apiService.getCocktailsByFirstLetter( letter ).drinks ?: emptyList())

        return cocktails
    }
}