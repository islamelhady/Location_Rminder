package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase

    @Before
    fun initDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun saveReminder_getReminderById() = runBlockingTest {
        val reminder = ReminderDTO("title", "description", "location", 30.033333, 31.233334)

        database.reminderDao().saveReminder(reminder)
        val result = database.reminderDao().getReminderById(reminder.id)

        assertThat(result as ReminderDTO, notNullValue())
        assertThat(result.id, `is`(reminder.id))
        assertThat(result.title, `is`(reminder.title))
        assertThat(result.description, `is`(reminder.description))
        assertThat(result.location, `is`(reminder.location))
        assertThat(result.latitude, `is`(reminder.latitude))
        assertThat(result.longitude, `is`(reminder.longitude))
    }

    @Test
    fun getAllRemindersFromDatabase() = runBlockingTest {
        val reminder1 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder2 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder3 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder4 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder5 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)
        database.reminderDao().saveReminder(reminder4)
        database.reminderDao().saveReminder(reminder5)

        val remindersList = database.reminderDao().getReminders()
        assertThat(remindersList, `is`(notNullValue()))
    }

    @Test
    fun insertReminders_deleteAllReminders() = runBlockingTest {
        val reminder1 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder2 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder3 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder4 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)
        val reminder5 = ReminderDTO("title", "description", "location", 30.033333, 31.233334)

        database.reminderDao().saveReminder(reminder1)
        database.reminderDao().saveReminder(reminder2)
        database.reminderDao().saveReminder(reminder3)
        database.reminderDao().saveReminder(reminder4)
        database.reminderDao().saveReminder(reminder5)

        database.reminderDao().deleteAllReminders()
        val remindersList = database.reminderDao().getReminders()
        assertThat(remindersList, `is`(emptyList()))
    }
}