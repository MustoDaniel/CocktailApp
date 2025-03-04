package com.example.cocktailapp.viewModels

import Cocktail
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cocktailapp.items.CocktailList

class ApplicationViewModel : ViewModel() {

    //Schermata HOME
    val options by mutableStateOf(listOf("nome", "id", "random"))
    var selectedOption = options[0]
    var imageUrl by mutableStateOf("")
    var cocktailName by mutableStateOf("")
    var ingredients by mutableStateOf(listOf("","","",""))
    var instructions by mutableStateOf("")
    var text by mutableStateOf("")
    var currentCocktail : Cocktail? = Cocktail()
}