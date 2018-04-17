package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ggslk.ggslk.R
import com.ggslk.ggslk.adapter.ArticleRecyclerAdapter
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException


class HomeFragment : Fragment() {

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private var articleRecyclerAdapter: ArticleRecyclerAdapter? = null
    private var loading = true
    private var pageNo = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context)
        articleRecyclerView.layoutManager = linearLayoutManager
        articleRecyclerView.setHasFixedSize(true)

        articleRecyclerAdapter = ArticleRecyclerAdapter(context!!, Session.articles)
        articleRecyclerView.adapter = articleRecyclerAdapter

        articleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0)
                // Check for scroll down
                {
                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (loading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            loading = false
                            loadRecentPosts(10, pageNo++)
                        }
                    }
                }
            }
        })

        homeFragmentSwipeContainer.setOnRefreshListener({
            pageNo = 1
            loadRecentPosts(10, pageNo++, clear = true)
        })

        // Trigger auto refresh on the first time
        homeFragmentSwipeContainer!!.post({
            homeFragmentSwipeContainer!!.isRefreshing = true

            pageNo = 1
            loadRecentPosts(10, pageNo++, clear = true)
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(context!!, "articles", Session.articles)
        SaveHandler.save(context!!, "favorites", Session.favorites)
    }

    private fun loadRecentPosts(count: Int, page: Int, clear: Boolean = false) {
        val jsonRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_recent_posts?count=$count&page=$page", null, Response.Listener { response ->
            try {
                val articlesJsonArray = response.getJSONArray("posts")

                if(clear){
                    Session.articles.clear()
                }

                for (i in 0 until articlesJsonArray.length()) {
                    val article = Article()
                    article.id = articlesJsonArray.getJSONObject(i).get("id").toString()
                    article.title = articlesJsonArray.getJSONObject(i).get("title").toString()
                    article.content = articlesJsonArray.getJSONObject(i).get("content").toString()
                    article.publishedDate = articlesJsonArray.getJSONObject(i).get("date").toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                    article.imageUrl = articlesJsonArray.getJSONObject(i).getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString()

                    val author = Author()
                    author.id = articlesJsonArray.getJSONObject(i).getJSONObject("author").get("id").toString()
                    author.name = articlesJsonArray.getJSONObject(i).getJSONObject("author").get("name").toString()
                    author.profilePictureUrl = ""
                    author.slug = articlesJsonArray.getJSONObject(i).getJSONObject("author").get("slug").toString()
                    article.author = author

                    Session.articles.add(article)
                }

                articleRecyclerAdapter!!.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                endLoading()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            endLoading()
        })
        Session.mRequestQueue!!.add(jsonRequest)
    }

    private fun endLoading() {
        try {
            loading = true
            homeFragmentSwipeContainer.isRefreshing = false
        } catch (e: Exception) {
            // Handle this
        }
    }
}
