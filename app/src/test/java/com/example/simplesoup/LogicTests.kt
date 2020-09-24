package com.example.simplesoup


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplesoup.data.Order
import org.junit.Test
import org.junit.Assert.*

class LogicTests{
    @Test
    fun regexIsFoin() {
        assertTrue("java".toRegex().containsMatchIn("jAva".toLowerCase()))
    }

    @Test
    fun newStudentAddedPostRemoval(){
        val students=ArrayList<Int>()
        val currentStudents=ArrayList<Int>()
        val sameStudents=ArrayList<Int>()


        students.addAll(arrayListOf(2345,54327,32424))
//        currentStudents.addAll(arrayListOf(2345,32424))


        for(Bid in students){
            for(Sid in currentStudents){
                if(Sid == Bid){
                   sameStudents.add(Sid)
                } }
        }
        currentStudents.removeAll(sameStudents)
        println("$currentStudents of size ${currentStudents.size}")
        sameStudents.clear()

        assertTrue(currentStudents.isNotEmpty())
    }
    @Test
    fun hashedLinkedSetUniquenessTest(){
        val orders=LinkedHashSet<Order>()
        val uniqueOrders=ArrayList<Order>()

        orders.add(Order("Report"))
        orders.add(Order("Chart Analysis"))
        orders.add(Order("Python Ass"))
        orders.add(Order("Report"))

        uniqueOrders.addAll(orders)
        println("orders $uniqueOrders.")
        assertEquals(orders.size,3)
    }

    @Test
    fun bitWiseComparisonTest(){
        val upperNumbers:IntArray = intArrayOf(1,2,4)
        val lowerNumbers:IntArray = intArrayOf(2,3,4)

        println(upperNumbers.toString())
        assertEquals(upperNumbers.hashCode() and lowerNumbers.hashCode(),2)
    }
}