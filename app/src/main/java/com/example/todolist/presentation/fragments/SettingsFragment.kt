package com.example.todolist.presentation.fragments
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.databinding.DataBindingUtil
import com.example.todolist.R
import com.example.todolist.databinding.FragmentSettingsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private lateinit var binding : FragmentSettingsBinding
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()
        binding.apply {
            lowTasks.isChecked = sharedPreferences.getBoolean("0", true)
            midTasks.isChecked = sharedPreferences.getBoolean("1", true)
            highTasks.isChecked = sharedPreferences.getBoolean("2", true)
            darkTheme.isChecked = sharedPreferences.getBoolean("dark_theme", false)
        }

        binding.rateApp.setOnClickListener {
            val manager = ReviewManagerFactory.create(requireContext())
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener {
                if(it.isSuccessful){
                    val reviewInfo = it.result
                    val flow = manager.launchReviewFlow(activity!!, reviewInfo!!)
                    flow.addOnCompleteListener {  }
                }else {
                    Snackbar.make(binding.root, "Some error occurred!", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
                }
            }
        }

        binding.darkTheme.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
            editor.putBoolean("dark_theme", isChecked)
            editor.apply()
        }

        binding.lowTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("0", isChecked)
            editor.apply()
        }

        binding.midTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("1", isChecked)
            editor.apply()
        }

        binding.highTasks.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("2", isChecked)
            editor.apply()
        }
    }
}