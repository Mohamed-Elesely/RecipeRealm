package com.elesely.reciperealm.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.elesely.reciperealm.R
import com.elesely.reciperealm.adapters.CategoryMealsAdapter
import com.elesely.reciperealm.adapters.ItemSpacingDecoration
import com.elesely.reciperealm.databinding.ActivityCategoryMealsBinding
import com.elesely.reciperealm.fragments.HomeFragment
import com.elesely.reciperealm.pojo.MealsByCategory
import com.elesely.reciperealm.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoriesViewModel: CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    companion object {
        const val MEAL_ID = "package com.elesely.reciperealm.activites.idMeal"
        const val MEAL_NAME = "package com.elesely.reciperealm.activites.nameMeal"
        const val MEAL_THUMB = "package com.elesely.reciperealm.activites.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareMealRecycler()

        categoriesViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoriesViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        observerPopularMeal()
        itemClicked()

    }


    private fun prepareMealRecycler() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter


        }
    }


    private fun itemClicked() {
        categoryMealsAdapter.onItemClick = { meal ->
            val intent = Intent(applicationContext, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerPopularMeal() {
        categoriesViewModel.observerMealsByCategory().observe(this, Observer { mealList ->
            categoryMealsAdapter.setMealList(mealList = mealList as ArrayList<MealsByCategory>)

            binding.tvCategoryCount.text = intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!
        })
    }


}