package com.elesely.reciperealm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.elesely.reciperealm.pojo.Meal
import com.elesely.reciperealm.pojo.MealList
import com.elesely.reciperealm.pojo.MealsByCategory
import com.elesely.reciperealm.pojo.MealsByCategoryList
import com.elesely.reciperealm.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {

    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body().let { mealsList ->
                    mealsLiveData.postValue(mealsList!!.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun observerMealsByCategory(): MutableLiveData<List<MealsByCategory>> {
        return mealsLiveData
    }
}