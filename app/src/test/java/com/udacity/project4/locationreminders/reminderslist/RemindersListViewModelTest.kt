package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.utils.MainCoroutineRule
import com.udacity.project4.locationreminders.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    //Subject under test
    private lateinit var viewModel: RemindersListViewModel
    private lateinit var remindersFakeDataSource: FakeDataSource

    @Before
    fun setupViewModel() {
        remindersFakeDataSource = FakeDataSource()
        viewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), remindersFakeDataSource)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun loadReminders_showLoading() {
        mainCoroutineRule.pauseDispatcher()
        viewModel.loadReminders()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isTrue()
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue()).isFalse()
    }

    @Test
    fun loadReminders_remainderListNotEmpty() = mainCoroutineRule.runBlockingTest {
        val reminder = ReminderDTO("Title", "Description", "Location", 30.033333, 31.233334)
        remindersFakeDataSource.saveReminder(reminder)
        viewModel.loadReminders()
        assertThat(viewModel.remindersList.getOrAwaitValue()).isNotEmpty()
    }

    @Test
    fun loadReminders_updateSnackBarValue() {
        mainCoroutineRule.pauseDispatcher()
        remindersFakeDataSource.setReturnError(true)
        viewModel.loadReminders()
        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showSnackBar.getOrAwaitValue()).isEqualTo("Error getting reminders")
    }
}