package com.elesely.reciperealm.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.elesely.reciperealm.R
import com.elesely.reciperealm.activites.CategoryMealsActivity
import com.elesely.reciperealm.activites.MainActivity
import com.elesely.reciperealm.adapters.CategoriesAdapter
import com.elesely.reciperealm.adapters.ItemSpacingDecoration
import com.elesely.reciperealm.databinding.FragmentCategoriesBinding
import com.elesely.reciperealm.viewmodel.HomeViewModel

class CategoriesFragment : Fragment() {


    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel:HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRv()
        observerCateg()
        onCategoryClicked()
    }

    private fun observerCateg() {
        viewModel.observeCategories().observe(viewLifecycleOwner,Observer{ categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun prepareRv() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCateg.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter

            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
            val itemSpacingDecoration = ItemSpacingDecoration(spacingInPixels)
            addItemDecoration(itemSpacingDecoration)
        }
    }

    private fun onCategoryClicked() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity ::class.java )
            intent.putExtra(HomeFragment.CATEGORY_NAME,category.strCategory)
            startActivity(intent)
        }
    }

}