package com.android.basics.features.todo.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.android.basics.MainCoroutineRule
import com.android.basics.TestDataFactory
import com.android.basics.features.todo.domain.model.User
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class UserLocalDataSourceNativeTest {

    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var database: TodoDatabase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // using an in-memory database for testing, since it doesn't survive killing the process
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        localDataSource = UserLocalDataSource(database.userDao(), Dispatchers.Main)
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun testPreConditions() {
        Truth.assertThat(localDataSource).isNotNull()
    }

    @Test
    fun register_authenticate() = runBlockingTest {
        // GIVEN - a new task saved in the database
        val user = TestDataFactory.getUserScope().user!!
        localDataSource.register(user)

        // WHEN  - Task retrieved by ID
        val result = localDataSource.authenticate(user)

        // THEN - Same task is returned
        Truth.assertThat(result.isRight).isTrue()
        val value = result.fold({}, { it }) as User
        assertThat(value.userName).isEqualTo(user.userName)
        assertThat(value.passWord).isEqualTo(user.passWord)
    }
}