package com.example.simplesoup

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.Order
import kotlinx.coroutines.currentCoroutineContext
import java.lang.Exception

class OrderConsolidatorWorker(appContext: Context, params: WorkerParameters):
    CoroutineWorker(appContext,params) {
    val mOrderIds=ArrayList<Int>()
    val oldOrderIds=ArrayList<Int>()


    companion object{
        const val WORK_NAME="OrderConsolidatorWorker"
    }

    private val orderNotifier=OrderNotifier(appContext)
    override suspend fun doWork(): Result {
        return try{
        DataSource.setUpSoup()
//        if(consolidateIds(mOrderIds,DataSource.idList))orderNotifier.makeNotification(
//            "Woof!","Found a new Order!")
//        return Result.success()}

        if(consolidateIds(mOrderIds,DataSource.idList)){orderNotifier.makeNotification(
            "Arf!","Orders ONCOMING!!")
            mOrderIds.addAll(DataSource.idList)
        }
        return Result.success()}
        catch (exception: Exception){
            return Result.retry()
        }
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
}