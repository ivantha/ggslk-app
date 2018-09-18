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
import com.ggslk.ggslk.adapter.SmallArticleRecyclerAdapter
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import kotlinx.android.synthetic.main.fragment_category_articles.*
import org.json.JSONException

private const val ARG_CATEGORY_SLUG = "category_slug"

class CategoryArticlesFragment : Fragment() {

    companion object {
        fun newInstance(categorySlug: String): CategoryArticlesFragment {
            return CategoryArticlesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CATEGORY_SLUG, categorySlug)
                }
            }
        }
    }

    private var categorySlug: String? = null

    private var articles: ArrayList<Article> = ArrayList()
    private var smallArticleRecyclerAdapter: SmallArticleRecyclerAdapter? = null
    private var loading = true
    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categorySlug = it.getString(ARG_CATEGORY_SLUG)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_articles, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context)
        categoryArticleRecyclerView.layoutManager = linearLayoutManager
        categoryArticleRecyclerView.setHasFixedSize(true)

        smallArticleRecyclerAdapter = SmallArticleRecyclerAdapter(context!!, articles)
        categoryArticleRecyclerView.adapter = smallArticleRecyclerAdapter

        categoryArticleRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            categoryArticlesFragmentSwipeContainer.isRefreshing = true
                            loadRecentPosts(10, pageNo++)
                        }
                    }
                }
            }
        })

        Session.mRequestQueue!!.cancelAll("cat")

        categoryArticlesFragmentSwipeContainer.setOnRefreshListener {
            pageNo = 1
            loadRecentPosts(10, pageNo++, clear = true)
        }

        // Trigger auto refresh on the first time
        categoryArticlesFragmentSwipeContainer!!.post {
            categoryArticlesFragmentSwipeContainer!!.isRefreshing = true

            pageNo = 1
            loadRecentPosts(10, pageNo++, clear = true)
        }
    }

    private fun loadRecentPosts(count: Int, page: Int, clear: Boolean = false) {
        val jsonRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=$categorySlug&count=$count&page=$page", null, Response.Listener { response ->
            try {
                val articlesJsonArray = response.getJSONArray("posts")

                if (clear) {
                    articles.clear()
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

                    articles.add(article)
                }

                smallArticleRecyclerAdapter!!.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            } finally {
                endLoading()
            }
        }, Response.ErrorListener { error ->
            error.printStackTrace()
            endLoading()
        })

        jsonRequest.tag = "cat"

        Session.mRequestQueue!!.add(jsonRequest)
    }

    private fun endLoading() {
        try {
            loading = true
            categoryArticlesFragmentSwipeContainer.isRefreshing = false
        } catch (e: Exception) {
            // Handle this
        }
    }
}
