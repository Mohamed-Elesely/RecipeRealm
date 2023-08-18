package com.elesely.reciperealm.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.elesely.reciperealm.pojo.Meal

@Dao
interface MealDao {

    //This means if you inserted a meal that already existed it will just update or replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInfo")
    fun getAllMeals(): LiveData<List<Meal>>


}