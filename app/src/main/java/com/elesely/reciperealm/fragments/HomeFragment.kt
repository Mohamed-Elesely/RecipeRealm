    package com.elesely.reciperealm.fragments

    import android.content.Intent
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.lifecycle.Observer
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.GridLayoutManager
    import androidx.recyclerview.widget.LinearLayoutManager
    import com.bumptech.glide.Glide
    import com.elesely.reciperealm.R
    import com.elesely.reciperealm.activites.CategoryMealsActivity
    import com.elesely.reciperealm.activites.MainActivity
    import com.elesely.reciperealm.activites.MealActivity
    import com.elesely.reciperealm.adapters.CategoriesAdapter
    import com.elesely.reciperealm.adapters.ItemSpacingDecoration
    import com.elesely.reciperealm.adapters.MostPopularAdapter

    import com.elesely.reciperealm.databinding.FragmentHomeBinding
    import com.elesely.reciperealm.pojo.MealsByCategory
    import com.elesely.reciperealm.pojo.Meal

    import com.elesely.reciperealm.viewmodel.HomeViewModel


    class HomeFragment : Fragment() {
        private lateinit var viewModel: HomeViewModel
        private lateinit var binding: FragmentHomeBinding
        private lateinit var randomMeal: Meal
        private lateinit var popularItemAdapter: MostPopularAdapter
        private lateinit var categoriesItemAdapter: CategoriesAdapter

        companion object {
            const val MEAL_ID = "package com.elesely.reciperealm.fragments.idMeal"
            const val MEAL_NAME = "package com.elesely.reciperealm.fragments.nameMeal"
            const val MEAL_THUMB = "package com.elesely.reciperealm.fragments.thumbMeal"
            const val CATEGORY_NAME = "package com.elesely.reciperealm.fragments.categoryName"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            viewModel = (activity as MainActivity).viewModel
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            preparePopularRecycler()
            viewModel.getRandomMeal()
            observerRandomMeal()
            onRandomMealClicked()

            viewModel.getPopularItem()
            observerPopularMeal()
            onPopularItemClicked()

            viewModel.getCategories()
            observerCategories()
            prepareCategoriesRecycler()

            onCategoryClicked()

            onSearchIconClicked()
            onSeeAllClicked()

        }

        private fun onSeeAllClicked() {
            binding.seeAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_categoriesFragment)
            }
        }

        private fun onSearchIconClicked() {
            binding.ingSearch.setOnClickListener{
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }
        }

        private fun onCategoryClicked() {
            categoriesItemAdapter.onItemClick = { category ->
                val intent = Intent(activity,CategoryMealsActivity ::class.java )
                intent.putExtra(CATEGORY_NAME,category.strCategory)
                startActivity(intent)
            }
        }

        private fun prepareCategoriesRecycler() {
            categoriesItemAdapter = CategoriesAdapter()
            binding.recViewCategories.apply {
                layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
                adapter = categoriesItemAdapter

                // Add item spacing decoration
                val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
                val itemSpacingDecoration = ItemSpacingDecoration(spacingInPixels)
                addItemDecoration(itemSpacingDecoration)
            }
        }


        private fun observerCategories() {
            viewModel.observeCategories().observe(viewLifecycleOwner,Observer{ caegories->
                categoriesItemAdapter.setCategoryList(caegories)
            })
        }

        private fun onPopularItemClicked() {
            popularItemAdapter.onItemClick = { meal ->
                val intent = Intent(activity,MealActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_NAME, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        }

        private fun preparePopularRecycler() {
            popularItemAdapter = MostPopularAdapter()
            binding.recViewMealsPopular.apply {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = popularItemAdapter
            }
        }

        private fun observerPopularMeal() {
            viewModel.observePopularItemsLiveData().observe(
                viewLifecycleOwner
            ) { mealList ->
                popularItemAdapter.setMeals(mealList = mealList as ArrayList<MealsByCategory>)
            }
        }

        private fun onRandomMealClicked() {
            binding.randomMealCard.setOnClickListener {
                val intent = Intent(activity, MealActivity::class.java)
                intent.putExtra(MEAL_ID, randomMeal.idMeal)
                intent.putExtra(MEAL_NAME, randomMeal.strMeal)
                intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
                startActivity(intent)
            }
        }

        private fun observerRandomMeal() {
            viewModel.observeRandomMealLiveData().observe(
                viewLifecycleOwner
            ) { meal ->
                binding.progrees.visibility = View.GONE
                Glide.with(this@HomeFragment)
                    .load(meal!!.strMealThumb)
                    .into(binding.imgRandomMeal)

                this.randomMeal = meal
            }
        }
    }