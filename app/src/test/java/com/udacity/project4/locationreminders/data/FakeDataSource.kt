package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var reminders: MutableList<ReminderDTO>? = mutableListOf()) :
    ReminderDataSource {

    private var shouldReturnError = false
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) {
            return Result.Error("Error getting reminders")
        }
        reminders?.let { return Result.Success(it) }
        return Result.Error("Reminders not found")
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        val reminder = reminders?.find { reminderDTO ->
            reminderDTO.id == id
        }
        return when {
            reminder != null -> {
                Result.Success(reminder)
            }
            shouldReturnError -> {
                Result.Error(" Not found reminder!")
            }
            else -> {
                Result.Error("Not found reminder!")
            }
        }
    }

    override suspend fun deleteAllReminders() {
        reminders?.clear()
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }
}