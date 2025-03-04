package com.example.cocktailapp.screens

import Cocktail
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cocktailapp.R
import com.example.cocktailapp.data.Datasource
import com.example.cocktailapp.items.CocktailList
import com.example.cocktailapp.items.Gif
import com.example.cocktailapp.viewModels.ApplicationViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun Screen2(navController: NavController, viewModel: ApplicationViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 130.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val coroutineScope = rememberCoroutineScope()
        var cocktails by remember { mutableStateOf<List<Cocktail>>(emptyList()) }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                cocktails = Datasource().loadCocktails()
            }
        }

        if (cocktails.isEmpty()) {
            Gif(R.drawable.loading_image)
        } else{
            CocktailList(
                cocktailList = cocktails,
                modifier = Modifier.fillMaxHeight(),
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}