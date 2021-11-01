package com.example.news

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val items = loadNews()
        val adapter: ListAdapter = ListAdapter(items)
        recyclerView.adapter = adapter

    }

    private fun loadNews(): ArrayList<String> {
        val headLine = ArrayList<String>()
        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=3a4df8f3d03c4b2199c332136cca8f96"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { val jsonArray = it.optJSONArray("articles")

              if (jsonArray != null) {
                  for (i in 0..jsonArray.length()) {
                      val jsonObject = jsonArray.optJSONObject(i)

                      val headline = jsonObject.optString("title")
                      Log.d("msg", headline.toString())

                      headLine.add(headline)

                  }
              }
            },
            {

            })
        queue.add(request)
        return headLine

    }



}