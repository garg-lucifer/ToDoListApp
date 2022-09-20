package com.example.todolist.presentation.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todolist.presentation.MainActivity
import com.example.todolist.presentation.MainActivityViewModel
import com.example.todolist.R
import com.example.todolist.presentation.br.AlarmReceiver
import com.example.todolist.databinding.FragmentNewTaskBinding
import com.example.todolist.data.model.CategoryInfo
import com.example.todolist.data.model.TaskCategoryInfo
import com.example.todolist.data.model.TaskInfo
import com.example.todolist.data.util.Constants
import com.example.todolist.data.util.DateToString
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*

class NewTaskFragment : Fragment() {
    private lateinit var binding : FragmentNewTaskBinding
    private lateinit var navController: NavController
    private val args : NewTaskFragmentArgs by navArgs()
    private var taskInfo = TaskInfo(0,"", Date(Constants.MAX_TIMESTAMP), 0, false,"" )
    private var categoryInfo = CategoryInfo("","#000000")
    private lateinit var viewModel: MainActivityViewModel
    private var colorString = "#000000"
    private lateinit var prevTaskCategory : TaskCategoryInfo
    private var isCategorySelected = false
    private lateinit var colorView : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_task, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        navController = findNavController()
        createNotification()
        if(args.newTaskArg != null) initUpdate()
        setInitialValues()
        loadAllCategories()
    }

    private fun initUpdate(){
        taskInfo = args.newTaskArg!!.taskInfo
        categoryInfo = args.newTaskArg!!.categoryInfo[0]
        binding.fab.text = "Update"
        colorString = categoryInfo.color
        prevTaskCategory = TaskCategoryInfo(
            TaskInfo(taskInfo.id, taskInfo.description, taskInfo.date, taskInfo.priority, taskInfo.status, taskInfo.category),
            listOf(CategoryInfo(categoryInfo.categoryInformation, categoryInfo.color))
        )
        isCategorySelected = true
    }

    private fun setInitialValues() {
        var str = DateToString.convertDateToString(taskInfo.date)
        if(str=="N/A")str="Due Date"

        binding.apply {
            editText.setText(taskInfo.description)
            dateAndTimePicker.text = str
            isCompleted.isChecked = taskInfo.status

            when (taskInfo.priority) {
                0 -> low.isChecked = true
                1 -> mid.isChecked = true
                else -> high.isChecked = true
            }

            //ClickListeners
            dateAndTimePicker.setOnClickListener { showDateTimePicker()}
            isCompleted.setOnCheckedChangeListener{_,it-> taskInfo.status = it }
            fab.setOnClickListener{ addTask()}
            priorityChipGroup.setOnCheckedStateChangeListener{chipGroup, i->
                changePriority(chipGroup, i)
            }
            categoryChipGroup.setOnCheckedStateChangeListener{chipGroup, i->
                listenToCategoryClick(chipGroup, i)
            }

        }
    }

    private fun loadAllCategories() {
        viewModel.getCategories().observe(viewLifecycleOwner) {
            for (category in it) {
                val chip = Chip(context)
                chip.text = category.categoryInformation
                val drawable = ChipDrawable.createFromAttributes(
                    requireContext(),
                    null,
                    0,
                    com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice
                )
                chip.setChipDrawable(drawable)
                chip.tag = category.color
                chip.isChecked = chip.text == taskInfo.category
                binding.categoryChipGroup.addView(chip)
            }
        }
    }

    private fun changePriority(chipGroup: ChipGroup, i: List<Int>) {
        val id = i[0]
        val chip = chipGroup.findViewById(id) as Chip

        when (chip.text) {
            "Low" -> taskInfo.priority = 0
            "Medium" -> taskInfo.priority = 1
            else -> taskInfo.priority = 2
        }
    }

    private fun listenToCategoryClick(chipGroup: ChipGroup, i: List<Int>) {
        val id = i[0]
        val chip = chipGroup.findViewById(id) as Chip
        if(chip.text.toString() == "+ Add New Category"){
            displayCategoryChooseDialog()
            isCategorySelected = false
        }else {
            taskInfo.category = chip.text.toString()
            categoryInfo.categoryInformation = chip.text.toString()
            categoryInfo.color = chip.tag.toString()
            colorString = categoryInfo.color
            isCategorySelected = true
        }
    }

    private fun displayCategoryChooseDialog() {
        colorString = generateRandomColor()
        Log.d("DATA", colorString)
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.category_dialog)
        val editText = dialog.findViewById<TextInputEditText>(R.id.editText)
        val addCategory = dialog.findViewById<MaterialButton>(R.id.addCategory)
        val addColor = dialog.findViewById<MaterialButton>(R.id.addColor)
        colorView = dialog.findViewById<View>(R.id.viewColor)
        colorView.setBackgroundColor(Color.parseColor(colorString))
        addColor.setOnClickListener { displayColorPickerDialog() }
        addCategory.setOnClickListener {
            if(editText.text.isNullOrBlank())
                Snackbar.make(binding.root, "Please add category", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
            else {
                addNewCategoryChip(editText.text.toString())
            }
            dialog.dismiss()
        }
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private fun generateRandomColor() : String{
        val random = Random()
        val color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
        return "#" + Integer.toHexString(color)
    }

    private fun displayColorPickerDialog() {
        ColorPickerDialogBuilder
            .with(context)
            .setTitle("Choose color")
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(12)
            .setOnColorSelectedListener { selectedColor ->
                colorString = "#" + Integer.toHexString(selectedColor)
            }
            .setPositiveButton("Ok") { _, _, _ ->
                colorView.setBackgroundColor(Color.parseColor(colorString))
            }
            .setNegativeButton("Cancel") { _,_ ->
                colorString = "#000000"
            }
            .build()
            .show()
    }

    private fun addNewCategoryChip(category: String) {
        val chip = Chip(context)
        val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, com.google.android.material.R.style.Widget_MaterialComponents_Chip_Choice)
        chip.apply {
            setChipDrawable(drawable)
            text = category
            isCheckable = true
            isChecked = true
            tag = colorString
        }
        taskInfo.category = chip.text.toString()
        categoryInfo.categoryInformation = chip.text.toString()
        categoryInfo.color = colorString
        binding.categoryChipGroup.addView(chip)
        isCategorySelected = true
    }

    private fun addTask() {
        val date = Date()
        Log.d("DATA", taskInfo.date.seconds.toString())
        taskInfo.description = binding.editText.text.toString()
        if(taskInfo.description.isNullOrBlank())Snackbar.make(binding.root, "Please add description", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        else if(taskInfo.category.isNullOrBlank() || categoryInfo.categoryInformation.isNullOrBlank() || !isCategorySelected)Snackbar.make(binding.root, "Please select a category", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        else {
            if(binding.fab.text.equals("Update")) {
                updateTask()
            }else {
                val diff = (Date().time/1000) - Constants.sDate
                taskInfo.id = diff.toInt()
                viewModel.insertTaskAndCategory(taskInfo, categoryInfo)
                if(!taskInfo.status && taskInfo.date>date && taskInfo.date.seconds == 5)
                setAlarm(taskInfo)
            }
            navController.popBackStack()
        }
    }

    private fun updateTask(){
        val date = Date()
        if (taskInfo.category == prevTaskCategory.taskInfo.category)
            viewModel.updateTaskAndAddCategory(taskInfo, categoryInfo)
        else {
            CoroutineScope(Main).launch {
                if (viewModel.getCountOfCategory(prevTaskCategory.taskInfo.category) == 1) {
                    viewModel.updateTaskAndAddDeleteCategory(
                        taskInfo,
                        categoryInfo,
                        prevTaskCategory.categoryInfo[0]
                    )
                } else {
                    viewModel.updateTaskAndAddCategory(taskInfo, categoryInfo)
                }
            }
        }

        if(!taskInfo.status && taskInfo.date>date && taskInfo.date.seconds == 5)
            setAlarm(taskInfo)
        else removeAlarm(taskInfo)
    }

    private fun removeAlarm(taskInfo: TaskInfo){
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("task_info", taskInfo)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.cancel(pendingIntent)
    }

    private fun showDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            taskInfo.date = calendar.time
            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener{
            val cal = Calendar.getInstance()
            cal.time = taskInfo.date
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            taskInfo.date = cal.time
            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
        }
        datePicker.show(childFragmentManager,"TAG")
    }

    private fun setAlarm(taskInfo: TaskInfo) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.putExtra("task_info", taskInfo)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
        val mainActivityIntent = Intent(requireContext(), MainActivity::class.java)
        val basicPendingIntent = PendingIntent.getActivity(requireContext(), taskInfo.id, mainActivityIntent, PendingIntent.FLAG_IMMUTABLE)
        val clockInfo = AlarmManager.AlarmClockInfo(taskInfo.date.time, basicPendingIntent)
        alarmManager.setAlarmClock(clockInfo, pendingIntent)
    }

    private fun createNotification() {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("to_do_list", "Tasks Notification Channel", importance).apply {
                description = "Notification for Tasks"
            }
            val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
    }

}