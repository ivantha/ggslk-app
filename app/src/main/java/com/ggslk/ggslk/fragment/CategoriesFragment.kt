package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ggslk.ggslk.R
import com.ggslk.ggslk.adapter.CategoryRecyclerAdapter
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import kotlinx.android.synthetic.main.fragment_categories.*
import org.json.JSONException


class CategoriesFragment : Fragment() {

    private var categoryRecyclerAdapter: CategoryRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        categoryRecyclerView.layoutManager = gridLayoutManager
        categoryRecyclerView.setHasFixedSize(true)

        categoryRecyclerAdapter = CategoryRecyclerAdapter(context!!, Session.categories, fragmentManager!!)
        categoryRecyclerView.adapter = categoryRecyclerAdapter

        Session.mRequestQueue!!.cancelAll("cat")

       categoryFragmentSwipeContainer.setOnRefreshListener({
           loadArticles()
       })

        // Trigger auto refresh on the first time
        categoryFragmentSwipeContainer!!.post({
            if (Session.categoriesFragmentFirstOpen) {
                loadArticles()

                Session.categoriesFragmentFirstOpen = false
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(context!!, "categories", Session.categories)
    }

    private fun loadArticles(){
        // Set empty data
        for (category in Session.categories){
            var author = Author()
            author.name = ""

            var article = Article()
            article.title = ""
            article.imageUrl = "http://gbchope.com/wp-content/uploads/2016/10/events-placeholder.jpg"
            article.author = author

            category.featuredArticle = article
        }

        for (category in Session.categories){
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + category.slug + "&count=1", null, Response.Listener { response ->
                try {
                    val firstPost = response.getJSONArray("posts").getJSONObject(0)

                    var author = Author()
                    author.name = firstPost.getJSONObject("author").get("name").toString()

                    var article = Article()
                    article.title = firstPost.get("title").toString()
                    article.imageUrl = firstPost.getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString()
                    article.author = author

                    category.featuredArticle = article
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> error.printStackTrace() })

            jsonObjectRequest.retryPolicy = DefaultRetryPolicy(500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

            jsonObjectRequest.tag = "cat"

            Session.mRequestQueue!!.add(jsonObjectRequest)
        }
    }

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

}
