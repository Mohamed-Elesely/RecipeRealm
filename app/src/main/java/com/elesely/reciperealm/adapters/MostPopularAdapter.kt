package com.elesely.reciperealm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elesely.reciperealm.databinding.PopularItemBinding
import com.elesely.reciperealm.pojo.MealsByCategory

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    private var mealsList = ArrayList<MealsByCategory>()
    lateinit var onItemClick:((MealsByCategory)-> Unit)

    fun setMeals(mealList: ArrayList<MealsByCategory>){
        this.mealsList = mealList
        notifyDataSetChanged()
    }

    public class PopularMealViewHolder(val binding:PopularItemBinding ):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}