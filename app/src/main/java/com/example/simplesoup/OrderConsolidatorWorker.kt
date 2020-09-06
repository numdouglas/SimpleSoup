package com.example.simplesoup

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.ID
import com.example.simplesoup.data.IDRepo
import java.lang.Exception

class OrderConsolidatorWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext,params) {
    var mOrderIds=ArrayList<Int>()
    private val oldOrderIds=ArrayList<Int>()

    private var idDB=IDRepo.getDatabase(appContext)
//        Room.databaseBuilder(applicationContext,IDRepo::class.java,"id-database").build()

    companion object{
        const val WORK_NAME="OrderConsolidatorWorker"
    }

    private val orderNotifier=OrderNotifier(appContext)
    override suspend fun doWork(): Result {
        mOrderIds=ArrayList(idDB.idDao().getAll().map { it.id })

        return try{
            DataSource.setUpSoup()
//        if(consolidateIds(mOrderIds,DataSource.idList))orderNotifier.makeNotification(
//            "Woof!","Found a new Order!")
//        return Result.success()}
        Log.i("Current","$mOrderIds and ${DataSource.idList}")
        if(consolidateIds(mOrderIds,DataSource.idList)){
            orderNotifier.makeNotification(
                "Arf!","Difference!! originally $mOrderIds" +
                    ", now ${DataSource.idList}")
            //Run the next 2 as a single transaction as one is asynchronous and another synchronous
            //thus one may not complete
//            idDB.idDao().deleteAll()
//            idDB.idDao().insertAll(*DataSource.idList.map { it->ID(it) }.toTypedArray())
            idDB.idDao().deleteAllAndInsertInTransaction(*DataSource.idList.map { it->ID(it) }.toTypedArray())
            Log.i("Current","${idDB.idDao().getAll()} and ${DataSource.idList}")
        }
            //destroyDBInstance()

        return Result.success()}
        catch (exception: Exception){
            Result.retry() }
   }


    private fun consolidateIds(previousIds:ArrayList<Int>,currentIds:ArrayList<Int>):Boolean{
        for(Bid in previousIds){
                for(Sid in currentIds){
                    if(Sid == Bid){
                        oldOrderIds.add(Sid)
                    } }
        }
        Log.i("Consolidated IDs",currentIds.toString())

        currentIds.removeAll(oldOrderIds)
        oldOrderIds.clear()
        return (currentIds.isNotEmpty())
    }


    fun destroyDBInstance() {
        if (idDB.isOpen) {
            idDB.close()
        }

    }
}