package com.elesely.reciperealm.retrofit

import com.elesely.reciperealm.pojo.Categories
import com.elesely.reciperealm.pojo.MealsByCategoryList
import com.elesely.reciperealm.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularMeals(@Query("c")categoryName: String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories (): Call<Categories>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c")categoryName: String) : Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeal(@Query("s")searchQuery:String) : Call<MealList>
}