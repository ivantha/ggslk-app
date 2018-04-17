package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ggslk.ggslk.R
import com.ggslk.ggslk.adapter.CategoryRecyclerAdapter
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import com.ggslk.ggslk.model.Category
import kotlinx.android.synthetic.main.fragment_categories.*
import org.json.JSONException


class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

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

        categoryRecyclerAdapter = CategoryRecyclerAdapter(context!!, Session.categories)
        categoryRecyclerView.adapter = categoryRecyclerAdapter

        categoriesFragmentSwipeContainer.setOnRefreshListener({
            refreshCategories()
        })

        // Trigger auto refresh on the first time
        categoriesFragmentSwipeContainer!!.post({
            categoriesFragmentSwipeContainer!!.isRefreshing = true

            refreshCategories()
        })
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(context!!, "categories", Session.categories)
    }

    private fun refreshCategories(){
        val jsonRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_index", null, Response.Listener { response ->
            try {
                val categoriesJsonArray = response.getJSONArray("categories")

                Session.categories.clear()

                for (i in 0 until categoriesJsonArray.length()) {
                    val category = Category()
                    category.slug = categoriesJsonArray.getJSONObject(i).get("slug").toString()
                    category.title = categoriesJsonArray.getJSONObject(i).get("title").toString()

                    val author = Author()
                    author.name = ""

                    val article = Article()
                    article.title = ""
                    article.content = ""
                    article.author = author

                    category.featuredArticle = article

                    Session.categories.add(category)
                }

                categoryRecyclerAdapter!!.notifyDataSetChanged()

            } catch (e: JSONException) {
                e.printStackTrace()
            }finally {
                endLoading()
            }
        }, Response.ErrorListener {
            error -> error.printStackTrace()
            endLoading()
        })
        Session.mRequestQueue!!.add(jsonRequest)
    }

    private fun endLoading() {
        try {
            categoriesFragmentSwipeContainer.isRefreshing = false
        } catch (e: Exception) {
            // Handle this
        }
    }

}
