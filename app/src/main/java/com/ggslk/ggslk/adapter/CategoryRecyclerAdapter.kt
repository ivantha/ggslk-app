package com.ggslk.ggslk.adapter

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.ggslk.ggslk.R
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Category
import com.squareup.picasso.Picasso
import org.json.JSONException

class CategoryRecyclerAdapter(private val context: Context, private val categories: List<Category>) : RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_category, parent, false)
        val categoryViewHolder = CategoryViewHolder(v)

        return categoryViewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.categoryArticleName.text = categories[position].featuredArticle!!.title
        holder.categoryArticleAuthor.text = categories[position].featuredArticle!!.author!!.name
        holder.categoryTitle.text = categories[position].title
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + categories[position].slug + "&count=1", null, Response.Listener { response ->
            try {
                val title = response.getJSONArray("posts").getJSONObject(0)
                        .get("title").toString()
                val author = response.getJSONArray("posts").getJSONObject(0)
                        .getJSONObject("author").get("name").toString()
                val imageUrl = response.getJSONArray("posts").getJSONObject(0)
                        .getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString()

                categories[position].featuredArticle!!.title = title
                categories[position].featuredArticle!!.author!!.name = author
                categories[position].featuredArticle!!.imageUrl = imageUrl

                Picasso.get().load(imageUrl).fit().centerCrop().into(holder.categoryArticleImageView)
                holder.categoryArticleName.text = title
                holder.categoryArticleAuthor.text = author

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener { error -> error.printStackTrace() })

        jsonObjectRequest.retryPolicy = DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        Session.mRequestQueue!!.add(jsonObjectRequest)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.categoryCardView)
        val categoryArticleImageView: ImageView = itemView.findViewById(R.id.categoryArticleImageView)
        val categoryArticleName: TextView = itemView.findViewById(R.id.categoryArticleNameTextView)
        val categoryArticleAuthor: TextView = itemView.findViewById(R.id.categoryArticleAuthorTextView)
        val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitleTextView)
    }
}
