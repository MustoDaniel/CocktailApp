package com.example.cocktailapp.screens

import Cocktail
import android.app.AlertDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocktailapp.api.RetrofitInstance
import com.example.cocktailapp.database.CocktailDB
import com.example.cocktailapp.database.CocktailDBService
import com.example.cocktailapp.database.entities.SavedCocktail
import com.example.cocktailapp.items.CocktailCard
import com.example.cocktailapp.items.ListString
import com.example.cocktailapp.items.OptionList
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Home(viewModel: com.example.cocktailapp.viewModels.ApplicationViewModel = viewModel()){

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val serviceDB = CocktailDBService(LocalContext.current).repository

    Column{
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .padding(bottom = 20.dp),
                text = "Cocktail APP",
                textAlign = TextAlign.Center,
                fontSize = 50.sp,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text("Cerca cocktail per : ")
                viewModel.selectedOption = OptionList(viewModel.options)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp)
                .padding(bottom = 150.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically)
        ) {

            CocktailCard(
                imageUrl = viewModel.imageUrl,
                cocktailName = viewModel.cocktailName,
                imageSize = 200
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text("Ingredienti: ")
                ListString( viewModel.ingredients )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text("Istruzioni: ")
                Text(viewModel.instructions)
            }

            if(!viewModel.selectedOption.contentEquals("random")) {
                TextField(
                    value = viewModel.text,
                    onValueChange = { viewModel.text = it },
                    label = { Text("Campo di ricerca: ") },
                    placeholder = { Text("Type something...") },
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
            ){
                Button(
                    onClick = {
                        val apiService = RetrofitInstance.api

                        viewModel.text = viewModel.text.trim()
                        if(viewModel.text == "" && viewModel.selectedOption != "random")
                            return@Button

                        when (viewModel.selectedOption){
                            "nome", -> {
                                coroutineScope.launch {
                                    viewModel.currentCocktail = apiService.getCocktailByName(viewModel.text).drinks?.firstOrNull()
                                    changeValues(viewModel, viewModel.currentCocktail)
                                }
                            }
                            "id", -> {
                                coroutineScope.launch {
                                    if (viewModel.text.toIntOrNull() == null)
                                        viewModel.currentCocktail = null
                                    else
                                        viewModel.currentCocktail = apiService.getCocktailById(viewModel.text).drinks?.firstOrNull()
                                    changeValues(viewModel, viewModel.currentCocktail)
                                }
                            }
                            "random", -> {
                                coroutineScope.launch {
                                    viewModel.currentCocktail = apiService.getRandomCocktail().drinks?.firstOrNull()
                                    changeValues(viewModel, viewModel.currentCocktail)
                                }
                            }
                        }
                    }
                ) {
                    Text( text = "Cerca" )
                }

                val context = LocalContext.current
                var isSaved by remember { mutableStateOf(false) }
                if(viewModel.cocktailName != "not found" && viewModel.cocktailName != ""){
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (!serviceDB.getSavedCocktails().contains(viewModel.currentCocktail!!.idDrink)){
                                    serviceDB.insertSavedCocktail(SavedCocktail(viewModel.currentCocktail!!.idDrink))
                                    Toast.makeText(context, "Cocktail aggiunto ai preferiti", Toast.LENGTH_SHORT).show()
                                    isSaved = true
                                }
                                else{
                                    serviceDB.deleteSavedCocktail(SavedCocktail(viewModel.currentCocktail!!.idDrink))
                                    Toast.makeText(context, "Cocktail rimosso dai preferiti", Toast.LENGTH_SHORT).show()
                                    isSaved = false
                                }
                            }

                        }
                    ) {
                        LaunchedEffect(Unit) {
                            coroutineScope.launch {
                                if(serviceDB.getSavedCocktails().contains(viewModel.currentCocktail!!.idDrink))
                                    isSaved = true
                                else
                                    isSaved = false
                            }
                        }
                        if(!isSaved)
                            Text( text = "Salva" )
                        else
                            Text( text = "Rimuovi")
                    }
                }
            }
        }
    }
}

fun changeValues(viewModel: com.example.cocktailapp.viewModels.ApplicationViewModel, cocktail: Cocktail?){
    viewModel.imageUrl = cocktail?.strDrinkThumb ?: "not found"
    viewModel.cocktailName = cocktail?.strDrink ?: "not found"
    viewModel.ingredients = cocktail?.getIngredientsList() ?: listOf("", "", "", "")
    viewModel.instructions = cocktail?.strInstructionsIT ?: "not found"
    viewModel.text = ""
}