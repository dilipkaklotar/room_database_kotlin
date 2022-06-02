package com.dilip.room.database

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dilip.room.database.adapter.StudentAdapter
import com.dilip.room.database.databinding.ActivityMainBinding
import com.dilip.room.database.db.Student
import com.dilip.room.database.db.StudentDatabase
import com.dilip.room.database.db.StudentRepository
import com.dilip.room.database.model.StudentViewModel
import com.dilip.room.database.model.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var studentViewModel: StudentViewModel
    private lateinit var adapter: StudentAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        val dao = StudentDatabase.getInstance(application).studentDAO
        val repository = StudentRepository(dao)
        val factory = StudentViewModelFactory(repository)
        studentViewModel = ViewModelProvider(this,factory).get(StudentViewModel::class.java)
        binding.myViewModel = studentViewModel
        binding.lifecycleOwner = this


        studentViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        initRecyclerView()
    }

    private fun listItemClicked(student: Student) {
        studentViewModel.initUpdateAndDelete(student)
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter({selectedItem: Student ->listItemClicked(selectedItem)})
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList(){
        studentViewModel.getSavedStudent().observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

}