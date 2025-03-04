package com.example.cocktailapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cocktailapp.api.RetrofitInstance
import com.example.cocktailapp.data.Datasource
import com.example.cocktailapp.database.dao.CocktailDao
import com.example.cocktailapp.database.dao.Cocktail_IngredientDao
import com.example.cocktailapp.database.dao.IngredientDao
import com.example.cocktailapp.database.dao.SavedCocktailDao
import com.example.cocktailapp.database.entities.Cocktail
import com.example.cocktailapp.database.entities.Cocktail_Ingredient
import com.example.cocktailapp.database.entities.Ingredient
import com.example.cocktailapp.database.entities.SavedCocktail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Cocktail::class,
        Ingredient::class,
        SavedCocktail::class,
        Cocktail_Ingredient::class
    ],
    version = 1
)
abstract class CocktailDB : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun savedCocktailDao(): SavedCocktailDao
    abstract fun cocktail_IngredientDao(): Cocktail_IngredientDao

    companion object {
        @Volatile
        private var Instance: CocktailDB? = null

        fun getDatabase(context: Context): CocktailDB {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, CocktailDB::class.java, "cocktailDB")
                    .addCallback(roomCallback)
                    .build()
                    .also { Instance = it }
            }
        }


        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.cocktailDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(cocktailDao: CocktailDao) {
            val cocktails = Datasource().loadCocktails()

            cocktails.forEach { cocktail ->
                cocktailDao.insert(Cocktail(cocktail.idDrink, cocktail.strDrink))
            }
        }
    }
}