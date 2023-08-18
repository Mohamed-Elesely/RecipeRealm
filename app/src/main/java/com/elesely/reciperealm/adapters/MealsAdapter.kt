package com.elesely.reciperealm.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.elesely.reciperealm.databinding.FavItemBinding
import com.elesely.reciperealm.pojo.Meal


class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoritesMealViewHolder>() {
    inner class FavoritesMealViewHolder(val binding: FavItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    lateinit var onItemClick: ((Meal) -> Unit)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>() {
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesMealViewHolder {
        return FavoritesMealViewHolder(
            FavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )


    }

    override fun onBindViewHolder(holder: FavoritesMealViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        Log.d("FavMealsAdapter", "Position: $position, List size: ${differ.currentList.size}")
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}