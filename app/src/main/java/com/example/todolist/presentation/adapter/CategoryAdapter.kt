package com.example.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemCategoriesBinding
import com.example.todolist.data.model.NoOfTaskForEachCategory

class CategoryAdapter :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<NoOfTaskForEachCategory>(){
        override fun areItemsTheSame(
            oldItem: NoOfTaskForEachCategory,
            newItem: NoOfTaskForEachCategory
        ): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(
            oldItem: NoOfTaskForEachCategory,
            newItem: NoOfTaskForEachCategory
        ): Boolean {
            return oldItem==newItem
        }

    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_categories, parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class ViewHolder(private val itemCategoriesBinding: ItemCategoriesBinding) : RecyclerView.ViewHolder(itemCategoriesBinding.root){
        fun bind(noOfTaskForEachCategory: NoOfTaskForEachCategory){
            itemCategoriesBinding.noOfTaskOfCategory = noOfTaskForEachCategory
            itemCategoriesBinding.executePendingBindings()
            itemCategoriesBinding.root.setOnClickListener {
                onItemClickListener?.let {
                    it(noOfTaskForEachCategory.category)
                }
            }
        }
    }

    private var onItemClickListener :((String)->Unit)?=null
    fun setOnItemClickListener(listener : (String)->Unit){
        onItemClickListener = listener
    }
}