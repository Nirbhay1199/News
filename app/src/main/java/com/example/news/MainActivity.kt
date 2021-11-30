package com.example.news

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Color.parseColor
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.news.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: ListAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        loadNews()
        mAdapter = ListAdapter(this)
        binding.recyclerView.adapter = mAdapter

    }

    private fun loadNews(){
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=3a4df8f3d03c4b2199c332136cca8f96"

        val request = object: JsonObjectRequest(Request.Method.GET,
            url,
            null,
            {
                val nJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for (i in 0 until nJsonArray.length()){
                    val newsJsonObject = nJsonArray.getJSONObject(i)

                    if (newsJsonObject.getString("description")=="null" && newsJsonObject.getString("urlToImage")=="null"){
                        newsArray.add(News(description = " ",
                            title = newsJsonObject.getString("title"),
                            url = newsJsonObject.getString("url"),
                            imageUrl = ""))
                    }
                    else if (newsJsonObject.getString("description")=="null"){
                        newsArray.add(News(description = " ",
                            title = newsJsonObject.getString("title"),
                            url = newsJsonObject.getString("url"),
                            imageUrl = newsJsonObject.getString("urlToImage")))
                    }
                    else
                    {
                        val news = News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("description"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                        newsArray.add(news)
                    }

                }

                mAdapter.updateNews(newsArray)
            },
            {
                it.printStackTrace()
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["User-Agent"]="Mozilla/5.0"
                return headers
            }
        }
        Singleton.getInstance(this).addToRequestQueue(request)

    }


    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
        builder.setInstantAppsEnabled(true)
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))

    }


}