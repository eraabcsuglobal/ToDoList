package com.example.todolist

import android.content.Context
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.util.UUID

data class TaskItem(
    var name: String,
    var desc: String,
    var completedDate: LocalDate? = null,
    var id: UUID = UUID.randomUUID()
) {
    fun isCompleted() = completedDate != null
    fun imageResource(): Int = if (isCompleted()) R.drawable.unchecked_art else R.drawable.unchecked_art
    fun imageColor(context: Context): Int = if (isCompleted()) purple(context) else black(context)

    private fun purple(context: Context) = ContextCompat.getColor(context, R.color.purple_500)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)


    fun getName() = name
    fun getDesc() = desc
    fun getId() = id
    fun getCompletedDate() = completedDate
}
