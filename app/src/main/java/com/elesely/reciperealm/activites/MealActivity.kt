package com.elesely.reciperealm.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.elesely.reciperealm.R
import com.elesely.reciperealm.databinding.ActivityMealBinding
import com.elesely.reciperealm.db.MealDatabase
import com.elesely.reciperealm.fragments.FavoritesFragment
import com.elesely.reciperealm.fragments.HomeFragment
import com.elesely.reciperealm.fragments.SearchFragment
import com.elesely.reciperealm.pojo.Meal
import com.elesely.reciperealm.viewmodel.MealViewModel
import com.elesely.reciperealm.viewmodel.MealViewModelProviderFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var binding: ActivityMealBinding
    private lateinit var youtubeLink: String
    private lateinit var mealMVVM: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val mealViewModelProvider = MealViewModelProviderFactory(mealDatabase)
        mealMVVM = ViewModelProvider(this, mealViewModelProvider)[MealViewModel::class.java]


        if (intent.hasExtra(FavoritesFragment.MEAL_ID)) {
            getMealInfoFromFav()
        } else if (intent.hasExtra(CategoryMealsActivity.MEAL_ID)) {
            getMealInfoFromCat()
        }
        else if (intent.hasExtra(SearchFragment.MEAL_ID)) {
            getMealInfoFromCat()
        }
        else {
            getMealInfo()
        }
        setInfo()
        loadingCase()
        mealMVVM.getMealDetails(mealId)
        observerMealDetailsLiveData()

        onYoutubeImageClicked()
        onFavBtnClicked()
    }

    private fun onFavBtnClicked() {
        binding.buttonAddToFav.setOnClickListener {
            mealToBeSaved?.let { it1 -> mealMVVM.insertMeal(it1) }
            Toast.makeText(this, "Meal added to favourites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onYoutubeImageClicked() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToBeSaved: Meal? = null
    private fun observerMealDetailsLiveData() {
        mealMVVM.observerMealDetailsLiveData().observe(this, object : Observer<Meal> {
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal = t
                mealToBeSaved = meal
                binding.tvCategory.text = "Category: ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal.strArea}"

                youtubeLink = meal.strYoutube.toString()

                val instructions = meal.strInstructions?.split("\n")?.filter { it.isNotBlank() }

                val formattedInstructions = instructions?.mapIndexed { index, step ->
                    "${index + 1}. $step"
                }?.joinToString("\n\n")

                binding.tvInstructionsSteps.text = formattedInstructions
            }
        })
    }


    private fun setInfo() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInfo() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB) ?: ""
    }

    private fun getMealInfoFromCat() {
        val intent = intent
        mealId = intent.getStringExtra(CategoryMealsActivity.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(CategoryMealsActivity.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(CategoryMealsActivity.MEAL_THUMB) ?: ""
    }

    private fun getMealInfoFromFav() {
        val intent = intent
        mealId = intent.getStringExtra(FavoritesFragment.MEAL_ID) ?: ""
        mealName = intent.getStringExtra(FavoritesFragment.MEAL_NAME) ?: ""
        mealThumb = intent.getStringExtra(FavoritesFragment.MEAL_THUMB) ?: ""
    }

    private fun loadingCase() {
        binding.buttonAddToFav.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onResponseCase() {
        binding.buttonAddToFav.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.progressBar.visibility = View.INVISIBLE
    }

}