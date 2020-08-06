package com.example.simplesoup

import android.util.Log
import androidx.lifecycle.*
import com.example.simplesoup.data.DataSource
import com.example.simplesoup.data.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainsViewModel:ViewModel() {

    var orders= MutableLiveData<LinkedHashSet<Order>>()

//    var time= MutableLiveData<Int>()

    init {
        //time.value=0
      updateOrders()
    }

    fun updateOrders(){
        viewModelScope.launch(Dispatchers.IO) {
        orders.postValue(DataSource.setUpSoup())}}
}