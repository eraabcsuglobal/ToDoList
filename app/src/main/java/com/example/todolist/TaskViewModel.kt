package com.example.todolist.com.example.todolist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todolist.TaskItem
import java.time.LocalDate
import java.util.UUID

class TaskViewModel: ViewModel() {
    val taskItems: MutableLiveData<MutableList<TaskItem>> = MutableLiveData(mutableListOf())

    fun addTaskItem(newTask: TaskItem) {
        val list = taskItems.value ?: mutableListOf()
        list.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTaskItem(id: UUID, name: String, desc: String) {
        val list = taskItems.value ?: mutableListOf()
        val task = list.find { it.id == id }
        task?.let {
            it.name = name
            it.desc = desc
            taskItems.postValue(list)
        }
    }

    fun setCompleted(taskItem: TaskItem) {
        val list = taskItems.value ?: mutableListOf()
        val task = list.find { it.id == taskItem.id }
        task?.let {
            if (it.completedDate == null) {
                it.completedDate = LocalDate.now()
            }
            taskItems.postValue(list)
        }
    }
}