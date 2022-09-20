package com.example.todolist.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.presentation.MainActivity
import com.example.todolist.presentation.MainActivityViewModel
import com.example.todolist.R
import com.example.todolist.presentation.adapter.TasksAdapter
import com.example.todolist.databinding.FragmentCompletedTasksBinding
import com.example.todolist.data.model.TaskCategoryInfo
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class CompletedTasksFragment : ParentFragment() {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding : FragmentCompletedTasksBinding
    @Inject
    @Named("completed_task_fragment")
    lateinit var adapter : TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_completed_tasks, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        adapter.setOnTaskStatusChangedListener {
            updateTaskStatus(viewModel, it)
        }
        adapter.setOnItemClickListener {
            editTaskInformation(it)
        }
        initRecyclerView()
        viewModel.getCompletedTask().observe(viewLifecycleOwner, Observer {
            if(it.isEmpty()) binding.taskAnimationView.visibility = View.VISIBLE
            else binding.taskAnimationView.visibility = View.GONE
            adapter.differ.submitList(it)
        })
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun editTaskInformation(taskCategoryInfo: TaskCategoryInfo) {
        val action = CompletedTasksFragmentDirections.actionCompletedTasksFragmentToNewTaskFragment(taskCategoryInfo)
        findNavController().navigate(action)
    }

    private val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
        ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            val position = viewHolder.adapterPosition
            val taskInfo = adapter.differ.currentList[position]?.taskInfo
            val categoryInfo = adapter.differ.currentList[position]?.categoryInfo?.get(0)
            if (taskInfo != null && categoryInfo!= null) {
                deleteTask(viewModel, taskInfo, categoryInfo)
                Snackbar.make(binding.root,"Deleted Successfully",Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
                        }
                        show()
                    }
            }
        }
    }

}