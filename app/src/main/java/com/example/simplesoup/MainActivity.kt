package com.example.simplesoup


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.Order
import com.example.simplesoup.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var webView: WebView
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainsViewModel
    private lateinit var mainsAdapter: MainsAdapter
    private val appScope= CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel=ViewModelProvider
            .AndroidViewModelFactory
            .getInstance(application)
            .create(MainsViewModel::class.java)
        binding= DataBindingUtil.setContentView( this,R.layout.activity_main)

        initRecyclerView()

            viewModel.orders.observe(this, Observer {
            try {
                mainsAdapter.submitList(it!!)
            }catch (e:NullPointerException){}
                mainsAdapter.notifyDataSetChanged()})
    scheduleRepeatRequest()
    setUpButton()
    }




    private fun initRecyclerView(){
        binding.recycler.apply {
            layoutManager= LinearLayoutManager(this@MainActivity.applicationContext)
            mainsAdapter= MainsAdapter(viewModel)
            adapter=mainsAdapter
        }
    }


    private fun scheduleRepeatRequest(){
      /* appScope.launch {
        val oneTimeRequest= OneTimeWorkRequestBuilder<OrderWorker>().build()

        WorkManager.getInstance(applicationContext).enqueue(oneTimeRequest)
        }
*/
        appScope.launch(Dispatchers.IO) {
            val periodicWorkRequest= PeriodicWorkRequestBuilder<OrderConsolidatorWorker>(
                15,TimeUnit.MINUTES).build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
                OrderConsolidatorWorker.WORK_NAME,ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }

    private fun setUpButton(){

        binding.button.setOnClickListener {
  //          Log.i("New_Orders",viewModel.orders.value.toString())
//            val latestOrders=arrayOf("Junes", "Phil","Ralph")

    //        mainsAdapter.notifyDataSetChanged()
        }
    }
    }