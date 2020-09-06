package com.example.simplesoup


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.ID
import com.example.simplesoup.data.IDRepo
import com.example.simplesoup.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.lang.NullPointerException
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainsViewModel
    private lateinit var mainsAdapter: MainsAdapter
    private val appScope = CoroutineScope(Dispatchers.IO)
    private lateinit var ordersNow:List<ID>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(application)
            .create(MainsViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initRecyclerView()

        viewModel.orders.observe(this, Observer {
            try {
                mainsAdapter.submitList(it!!)
            } catch (e: NullPointerException) {
            }
            mainsAdapter.notifyDataSetChanged()
        })
        scheduleRepeatRequest()

       val scope = CoroutineScope(Job() + Dispatchers.IO)


        scope.launch{

            ordersNow=IDRepo.getDatabase(applicationContext)
                .idDao().getAll()
            Log.i("Current","$ordersNow and ${DataSource.idList}"

            )

        }
        sleep(6313)
        Toast.makeText(applicationContext,"Spotting $ordersNow and ${DataSource.idList}",Toast.LENGTH_LONG).show()
    }


    private fun initRecyclerView() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(this@MainActivity.applicationContext)
            mainsAdapter = MainsAdapter(viewModel)
            adapter = mainsAdapter
        }
    }


    private fun scheduleRepeatRequest() {
//        appScope.launch {
//        val oneTimeRequest= OneTimeWorkRequestBuilder<OrderWorker>().build()
//
//        WorkManager.getInstance(applicationContext).enqueue(oneTimeRequest)
//        }

        appScope.launch(Dispatchers.IO) {
            val periodicWorkRequest = PeriodicWorkRequestBuilder<OrderConsolidatorWorker>(
                15, TimeUnit.MINUTES
            ).build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                OrderConsolidatorWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
    }