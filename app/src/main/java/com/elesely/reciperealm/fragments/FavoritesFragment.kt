package com.elesely.reciperealm.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.elesely.reciperealm.R
import com.elesely.reciperealm.activites.MainActivity
import com.elesely.reciperealm.activites.MealActivity
import com.elesely.reciperealm.adapters.MealsAdapter
import com.elesely.reciperealm.adapters.ItemSpacingDecoration
import com.elesely.reciperealm.databinding.FragmentFavoritesBinding
import com.elesely.reciperealm.viewmodel.HomeViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoritesAdapter: MealsAdapter

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
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRV()
        observeFav()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])
                Toast.makeText(context, "Meal deleted", Toast.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFav)
        itemClicked()
    }

    private fun prepareRV() {
        favoritesAdapter = MealsAdapter()
        binding.rvFav.apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            adapter = favoritesAdapter

            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
            val itemSpacingDecoration = ItemSpacingDecoration(spacingInPixels)
            addItemDecoration(itemSpacingDecoration)
        }
    }

    private fun observeFav() {
        viewModel.observeFavoritesLiveData().observe(requireActivity(), Observer { meals ->
            Log.d("FavoritesFragment", "Meals list size: ${meals.size}")
            favoritesAdapter.differ.submitList(meals)
        })
    }

    private fun itemClicked() {
        favoritesAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

}