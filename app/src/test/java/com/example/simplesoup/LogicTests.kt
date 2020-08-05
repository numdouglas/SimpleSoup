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
        val students=ArrayList<String>()
        val currentStudents=ArrayList<String>()
        val sameStudents=ArrayList<String>()
        students.addAll(listOf("Five","JAinme","Bill","Defodil"))
        currentStudents.addAll(listOf("Bill","Defodi","JAinme"))


        for(Bid in students){
            for(Sid in currentStudents){
                if(Sid == Bid){
                   sameStudents.add(Sid)
                } }
        }
        currentStudents.removeAll(sameStudents)
        println(currentStudents)

        assertTrue(currentStudents.size>0)
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