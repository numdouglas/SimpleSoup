package com.example.simplesoup

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.simplesoup.data.IDRepo
import org.junit.Test
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.ID
import com.example.simplesoup.data.idDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert
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
    fun writeIDAndReadInList() {
        idDao.insertAll(ID(67),ID(90))
        Log.i("Remaining DB",idDao.getAll().toString())
        idDao.delete(ID(67))
        idDao.delete(ID(89))
        //idDao.deleteAll()
        Log.i("Now loading..",DataSource.setUpSoup().toString())
        idDao.insertAll(*DataSource.idList.map { it->ID(it) }.toTypedArray())
        idDao.insertAll(ID(54),ID(23))
        Log.i("Remaining DB",idDao.getAll().toString())
        idDao.deleteAllAndInsertInTransaction(ID(112),ID(239),ID(400))
        Log.i("Remaining DB",idDao.getAll().toString())
        assertThat(idDao.getAll()[0].id, equalTo(112))
    }


    @Test
    fun deleteAndConfirm(){

        idDao.deleteAll()
        assertThat(idDao.getAll().isEmpty(), equalTo(true))
    }

    @Test
    fun valueOfNullLiveData(){
        val scope = CoroutineScope(Job() + Dispatchers.Main)



        fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply {
            scope.launch{
            value = initialValue;Log.i("AndTest","$initialValue added") }}
        val student:MutableLiveData<String>?= MutableLiveData()
        student?.default("Molly")

        assertThat(student?.value?:return, equalTo(""))
    }
}