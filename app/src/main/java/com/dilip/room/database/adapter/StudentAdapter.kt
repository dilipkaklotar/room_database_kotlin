package com.dilip.room.database.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dilip.room.database.R
import com.dilip.room.database.databinding.ListItemBinding
import com.dilip.room.database.db.Student

class StudentAdapter(private val clickListener: (Student) -> Unit) :
    RecyclerView.Adapter<MyViewHolder>() {
    private val studentList = ArrayList<Student>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(studentList[position], clickListener)
    }

    fun setList(students: List<Student>) {
        studentList.clear()
        studentList.addAll(students)
    }

}

class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(student: Student, clickListener: (Student) -> Unit) {
        binding.nameTextView.text = student.name
        binding.emailTextView.text = student.email
        binding.rollnoTextView.text = student.roll_no
        binding.mediumTextView.text = student.medium

        binding.listItemLayout.setOnClickListener {
            clickListener(student)
        }
    }
}