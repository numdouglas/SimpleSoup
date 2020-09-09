package com.example.simplesoup

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.Order
import java.lang.Exception

class OrderWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val orderNotifier = OrderNotifier(appContext)

    private val mClassNames = ArrayList<Order>()
    private val oldClassNames = ArrayList<Order>()

    companion object {
        const val WORK_NAME = "OrderWorker"
    }

    @Override
    override suspend fun doWork(): Result {
        return try {

            mClassNames.addAll(DataSource.orderList)
            DataSource.setUpSoup()
            if (consolidateIds(mClassNames, DataSource.orderList))
                orderNotifier.makeNotification("Sniff Sniff", "Excited About new Orders?")
            return Result.success()
        } catch (exception: Exception) {
            return Result.retry()
        }
    }


    private fun consolidateIds(
        previousIds: ArrayList<Order>,
        currentIds: ArrayList<Order>
    ): Boolean {
        val updateSize: Int = currentIds.size

        for (Bid in previousIds) {
            for (Sid in currentIds) {
                if (Sid == Bid) {
                    //oldOrderIds.add(Sid)
                    oldClassNames.add(Sid)
                }
            }
        }
        Log.i("Consolidated IDs", currentIds.toString())

//        currentIds.removeAll(oldOrderIds)
        currentIds.removeAll(oldClassNames)

        return (currentIds.size > 0)
    }
}