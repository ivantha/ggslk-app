package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ggslk.ggslk.R
import com.ggslk.ggslk.activity.MainActivity
import com.ggslk.ggslk.adapter.CategoryRecyclerAdapter
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import com.ggslk.ggslk.model.Category
import org.json.JSONException
import java.util.*


class ArticlesFragment : Fragment() {

    private var gridLayoutManager: GridLayoutManager? = null
    private var recyclerView: RecyclerView? = null
    private var categoryRecyclerAdapter: CategoryRecyclerAdapter? = null
    private var mRequestQueue: RequestQueue? = null

    private val categories = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRequestQueue = MainActivity.getmRequestQueue()
        mRequestQueue = Volley.newRequestQueue(context!!)

        // Initialize GridLayoutManager
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_articles, container, false)

        recyclerView = view.findViewById(R.id.categoryRecyclerView)
        recyclerView!!.layoutManager = gridLayoutManager
        recyclerView!!.setHasFixedSize(true)

        val jsonRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_index", null, Response.Listener { response ->
            try {
                val categoriesJsonArray = response.getJSONArray("categories")

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

                    categories.add(category)
                }

                categoryRecyclerAdapter!!.notifyDataSetChanged()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })
        mRequestQueue!!.add(jsonRequest)

        categoryRecyclerAdapter = CategoryRecyclerAdapter(categories)
        recyclerView!!.adapter = categoryRecyclerAdapter

        return view
    }

    companion object {

        fun newInstance(): ArticlesFragment {
            return ArticlesFragment()
        }
    }

}
