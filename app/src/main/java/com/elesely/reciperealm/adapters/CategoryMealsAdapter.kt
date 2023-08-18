package com.elesely.reciperealm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elesely.reciperealm.databinding.ActivityCategoryMealsBinding
import com.elesely.reciperealm.databinding.CategoryItemBinding
import com.elesely.reciperealm.databinding.MealItemBinding
import com.elesely.reciperealm.pojo.Category
import com.elesely.reciperealm.pojo.Meal
import com.elesely.reciperealm.pojo.MealList
import com.elesely.reciperealm.pojo.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>(){


    private var mealList = ArrayList<MealsByCategory>()
    lateinit var onItemClick:((MealsByCategory)-> Unit)

    fun setMealList(mealList: List<MealsByCategory>){
        this.mealList = mealList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding: MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return  mealList.size
    }


}