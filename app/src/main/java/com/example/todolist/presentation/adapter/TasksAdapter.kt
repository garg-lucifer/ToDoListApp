package com.example.todolist.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.data.model.TaskCategoryInfo
import com.example.todolist.data.model.TaskInfo

class TasksAdapter:
    RecyclerView.Adapter<TasksAdapter.MyViewHolder>() {

    private val callback = object : DiffUtil.ItemCallback<TaskCategoryInfo>(){
        override fun areItemsTheSame(
            oldItem: TaskCategoryInfo,
            newItem: TaskCategoryInfo
        ): Boolean {
            return oldItem.taskInfo.id == newItem.taskInfo.id
        }

        override fun areContentsTheSame(
            oldItem: TaskCategoryInfo,
            newItem: TaskCategoryInfo
        ): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.item_task, parent, false
        ))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MyViewHolder(private val itemTaskBinding: ItemTaskBinding) : RecyclerView.ViewHolder(itemTaskBinding.root){
        fun bind(taskCategoryInfo: TaskCategoryInfo){
            itemTaskBinding.taskCategoryInfo = taskCategoryInfo
            itemTaskBinding.executePendingBindings()

            itemTaskBinding.isCompleted.setOnCheckedChangeListener{_,it->
                taskCategoryInfo.taskInfo.status = it
                onTaskStatusChangedListener?.let {
                    it(taskCategoryInfo.taskInfo)
                }
            }

            itemTaskBinding.root.setOnClickListener{
                onItemClickListener?.let {
                    it(taskCategoryInfo)
                }
            }

        }
    }

    private var onItemClickListener :((TaskCategoryInfo)->Unit)?=null
    fun setOnItemClickListener(listener : (TaskCategoryInfo)->Unit){
        onItemClickListener = listener
    }

    private var onTaskStatusChangedListener :((TaskInfo)->Unit)?=null
    fun setOnTaskStatusChangedListener(listener : (TaskInfo)->Unit){
        onTaskStatusChangedListener = listener
    }

}