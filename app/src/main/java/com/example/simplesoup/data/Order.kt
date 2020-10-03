package com.example.simplesoup.data

import android.util.Log
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

data class Order(
    var orderName: String
    //var myphoto:Int,
    //var myConfirmation:Int
)


class DataSource {

    companion object DataSource {

        var cookies: HashMap<String, String> = HashMap()
        var formData: HashMap<String, String> = HashMap()

        //apparently, useragent should allow loading of whole page
        private const val USER_AGENT =
            "Mozilla/5.0 (Linux; Android 8.0.0; moto g(6) play Build/OPP27.91-87; pt-br) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.111 Mobile Safari/537.36 Puffin/7.5.3.20547AP";


        private const val loginFormUrl = "https://writers.academia-research.com/index.php?r=account%2Flogin"

        private const val username: String = "XXXXXXXXXXX"
        private const val password: String = "XXXXXXXXXXX"

        private val sievedList = LinkedHashSet<Order>()
        private val uniqueList = ArrayList<Order>()
        val idList = ArrayList<Int>()

        //LinkedHashSet's set property will prevent duplicity while allowing conversion back to List
        private val sievedOrderIds = LinkedHashSet<Int>()
        val orderList = ArrayList<Order>()



        fun setUpSoup(): LinkedHashSet<Order> {

            //No need for expensive login if we have a cookie
            if (cookies.isEmpty()) {
                Log.i("COOKIES","No Nubbins, Restart!!!")
                //Have to start from homepage, same way you would navigate, we always put the next page we intend to
                //go to in the argument
                val loginForm =
                    Jsoup.connect(loginFormUrl).method(Connection.Method.GET)
                        .userAgent(USER_AGENT).execute()
                loginForm.parse() // this is the document that contains response html

                formData["LoginForm[login]"] = username
                formData["LoginForm[password]"] = password
                formData["Timezone[offset]"] = "3"
                formData["Timezone[name]"] = "Africa/Nairobi"


                cookies.putAll(loginForm.cookies()) // save the cookies, this will be passed on to next request
                }
            val homePage =
                Jsoup.connect(loginFormUrl)
                    .cookies(cookies)
                    .data(formData)
                    .maxBodySize(0)
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .execute().parse()


            val orders =
                homePage
                    .select("html body div.academy-red div.b-page-container.b-page-container_flex div.b-page-right div#orderList div#w0.grid-view table.b-table.b-table_orders tbody")
                    //.select("html body div.academy-red div.b-page-container.b-page-container_flex div.b-page-right div div#orderList div#w0.grid-view table.table.table-striped.table-orders tbody")
            val rows = orders.select("tr")


            sievedList.clear()
            sievedOrderIds.clear()

            for (row in rows) {
                val rowDetails = row.text()
                var orderId: Int? = null

                try {
                    orderId = row.select("td")[0].select("a").text().toInt()
                } catch (e: NumberFormatException) {
                }

                if ("(.+)".toRegex()/*"(python|java|excel|application)"*/

                        .containsMatchIn(rowDetails.toLowerCase())
                )

                    sievedList.add(Order(rowDetails))
                orderId?.let {

                    sievedOrderIds.add(it)
                }
            }

            Log.i("orders_found", sievedList.toString())
            Log.i("order_ids", idList.toString())

            uniqueList.clear()
            uniqueList.addAll(sievedList)
            idList.clear()
            idList.addAll(sievedOrderIds)
            return sievedList
        }
    }
}
