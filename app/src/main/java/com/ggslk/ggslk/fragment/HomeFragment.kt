package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ggslk.ggslk.R
import com.ggslk.ggslk.activity.MainActivity
import com.ggslk.ggslk.adapter.ArticleRecyclerAdapter
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import org.json.JSONException
import java.util.*

class HomeFragment : Fragment() {
    private var mRequestQueue: RequestQueue? = null

    private var linearLayoutManager: LinearLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var articleRecyclerAdapter: ArticleRecyclerAdapter? = null

    private val articles = ArrayList<Article>()

    private var loading = true
    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestQueue = MainActivity.getmRequestQueue()

        // Initialize LinearLayoutManager
        linearLayoutManager = LinearLayoutManager(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.articleRecyclerView)
        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView!!.setHasFixedSize(true)

        loadRecentPosts(10, pageNo++)

        articleRecyclerAdapter = ArticleRecyclerAdapter(articles)
        recyclerView!!.adapter = articleRecyclerAdapter

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                // Check for scroll down
                {
                    visibleItemCount = linearLayoutManager!!.childCount
                    totalItemCount = linearLayoutManager!!.itemCount
                    pastVisibleItems = linearLayoutManager!!.findFirstVisibleItemPosition()

                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            loadRecentPosts(10, pageNo++)
                        }
                    }
                }
            }
        })

        return view
    }

    private fun loadRecentPosts(count: Int, page: Int) {
        val jsonRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_recent_posts?count=$count&page=$page", null, Response.Listener { response ->
            try {
                val articlesJsonArray = response.getJSONArray("posts")

                for (i in 0 until articlesJsonArray.length()) {
                    val article = Article()
                    article.title = articlesJsonArray.getJSONObject(i).get("title").toString()
                    article.content = articlesJsonArray.getJSONObject(i).get("content").toString()
                    article.publishedDate = articlesJsonArray.getJSONObject(i).get("date").toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    article.imageUrl = articlesJsonArray.getJSONObject(i).getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString()

                    val author = Author()
                    author.name = articlesJsonArray.getJSONObject(i).getJSONObject("author").get("name").toString()
                    author.profilePictureUrl = ""
                    author.slug = articlesJsonArray.getJSONObject(i).getJSONObject("author").get("slug").toString()
                    article.author = author

                    articles.add(article)
                }

                articleRecyclerAdapter!!.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                loading = true
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            loading = true
        })
        mRequestQueue!!.add(jsonRequest)
    }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
