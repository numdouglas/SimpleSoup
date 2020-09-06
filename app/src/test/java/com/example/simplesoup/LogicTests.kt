package com.example.simplesoup


import android.util.Log
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
        System.out.println("orders $uniqueOrders.toString()")
        assertEquals(orders.size,3)
    }

}