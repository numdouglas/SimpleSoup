package com.example.simplesoup

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.ID
import com.example.simplesoup.data.IDRepo
import java.lang.Exception

class OrderConsolidatorWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext,params) {
    var mOrderIds=ArrayList<Int>()
    val oldOrderIds=ArrayList<Int>()

    private var idDB= Room.databaseBuilder(applicationContext,IDRepo::class.java,"id-database")
        .build()

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
                    ", now ${DataSource.idList}") }
            idDB.idDao().deleteAll()
            idDB.idDao().insertAll(*DataSource.idList.map { it->ID(it) }.toTypedArray())
            destroyDBInstance()
            Log.i("Current","$mOrderIds and ${DataSource.idList}")

        return Result.success()}
        catch (exception: Exception){
            Result.retry() }
   }

    private fun consolidateIds(previousIds:ArrayList<Int>,currentIds:ArrayList<Int>):Boolean{
        val updateSize:Int=currentIds.size

        for(Bid in previousIds){
                for(Sid in currentIds){
                    if(Sid == Bid){
                        oldOrderIds.add(Sid)
                    } }
        }
        Log.i("Consolidated IDs",currentIds.toString())

//        currentIds.removeAll(oldOrderIds)
        currentIds.removeAll(oldOrderIds)
        oldOrderIds.clear()
        return (currentIds.size>0)
    }


    fun destroyDBInstance() {

        if (idDB.isOpen == true) {
            idDB.close()
        }

    }
}