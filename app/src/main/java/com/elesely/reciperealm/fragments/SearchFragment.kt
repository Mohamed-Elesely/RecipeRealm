package com.elesely.reciperealm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.elesely.reciperealm.R
import com.elesely.reciperealm.activites.MainActivity
import com.elesely.reciperealm.activites.MealActivity
import com.elesely.reciperealm.adapters.MealsAdapter
import com.elesely.reciperealm.databinding.FragmentSearchBinding
import com.elesely.reciperealm.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel:HomeViewModel
    private lateinit var searchAdapter:MealsAdapter

    companion object {
        const val MEAL_ID = "package com.elesely.reciperealm.fragments.idMeal"
        const val MEAL_NAME = "package com.elesely.reciperealm.fragments.nameMeal"
        const val MEAL_THUMB = "package com.elesely.reciperealm.fragments.thumbMeal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRV()
        observeSearchedMealsLiveData()

        binding.imgArrow.setOnClickListener{searchMeals()}

        var searchJob: Job? = null
        binding.edSearchBox.addTextChangedListener { searchQuery ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(searchQuery.toString())
            }
        }

        itemClicked()
    }

    private fun observeSearchedMealsLiveData() {
        viewModel.observeSearchMealListLiveData().observe(viewLifecycleOwner,Observer{ mealList ->
            searchAdapter.differ.submitList(mealList)
        })
    }

    private fun searchMeals() {
        val searchQuery = binding.edSearchBox.text.toString()
        if(searchQuery.isNotEmpty()){
            viewModel.searchMeals(searchQuery)
        }
    }

    private fun prepareRV() {
        searchAdapter = MealsAdapter()
        binding.rvSearch.apply {
            layoutManager = GridLayoutManager(context,1,GridLayoutManager.VERTICAL,false)
            adapter = searchAdapter
        }
    }

    private fun itemClicked() {
        searchAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

}