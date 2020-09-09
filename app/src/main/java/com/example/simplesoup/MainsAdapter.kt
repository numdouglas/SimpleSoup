package com.example.simplesoup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.simplesoup.data.Order
import kotlinx.android.synthetic.main.list_item.view.*

//Use list adapter instead as this will work with diffutil
class MainsAdapter ://RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    ListAdapter<Order, HomeViewHolder>(OrdersDiffCallBack()) {
//No need to keep track of data with Diff util
    //    private var orders: LinkedHashSet<Order> = LinkedHashSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.list_item, parent, false)
        )
    }
//No need to keep track of items size with Diffutil
//    override fun getItemCount(): Int {
//        return orders.size
//    }


    //We will replace this with DIffUtils Own SubmitList
//    fun submitList(ordersList:LinkedHashSet<Order>){
//        orders =ordersList
//    }


    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
//        when (holder) {
//
//            //       is HomeViewHolder -> holder.bind(viewmodel,people.get(position))
//            is HomeViewHolder -> holder.bind(viewmodel,position)
//        }
    }
}


class OrdersDiffCallBack : DiffUtil.ItemCallback<Order>() {
    //particular data you wanna check for equality
    override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem.orderName == newItem.orderName
    }

    override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
        return oldItem == newItem
    }

}

//This is the structure that defines what a view will look like in the recyclerview
class HomeViewHolder constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val myName: TextView = itemView.order_name

    // private val ordersList:ArrayList<Order>?=ArrayList()
//Bind properties defined above to the recyclerview
    fun bind(order: Order) {

//        ordersList?.let {
//            it.addAll(viewModel.orders?.value?:run{return})
        myName.text = order.orderName
    }
}