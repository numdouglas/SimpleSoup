package com.example.simplesoup

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplesoup.data.IDRepo
import org.junit.Test
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.simplesoup.data.ID
import com.example.simplesoup.data.idDao
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DBTest {
    private lateinit var idDao: idDao
    private lateinit var db: IDRepo

    @Before
    fun createDb() {
        db =IDRepo.getDatabase(ApplicationProvider.getApplicationContext<Context>())
        idDao = db.idDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
         idDao.insertAll(ID(10),ID(11))
        assertThat(idDao.getAll()[0].id, equalTo(10))
    }
}