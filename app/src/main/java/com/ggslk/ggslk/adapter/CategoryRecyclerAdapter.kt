package com.ggslk.ggslk.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentManager
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
import com.ggslk.ggslk.fragment.CategoryArticlesFragment
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Author
import com.ggslk.ggslk.model.Category
import com.squareup.picasso.Picasso
import org.json.JSONException

class CategoryRecyclerAdapter(private val context: Context, private val categories: List<Category>, private val fragmentManager: FragmentManager) : RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_category, parent, false)
        return CategoryViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.cardViewBackground.setBackgroundColor(Session.categoryColors[position % Session.categoryColors.size])
        holder.category = categories[position]
        holder.categoryTitle.text = categories[position].title

        var featuredArticle = categories[position].featuredArticle
        if (featuredArticle != null) {
            Picasso.get().load(featuredArticle.imageUrl).fit().centerCrop().into(holder.categoryArticleImageView)
            holder.categoryArticleName.text = featuredArticle.title
            holder.categoryArticleAuthor.text = featuredArticle.author!!.name
        } else {
            val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + categories[position].slug + "&count=1", null, Response.Listener { response ->
                try {
                    val firstPost = response.getJSONArray("posts").getJSONObject(0)

                    var author = Author()
                    author.name = firstPost.getJSONObject("author").get("name").toString()

                    var article = Article()
                    article.title = firstPost.get("title").toString()
                    article.imageUrl = firstPost.getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString()
                    article.author = author

                    categories[position].featuredArticle = article

                    Picasso.get().load(article.imageUrl).fit().centerCrop().into(holder.categoryArticleImageView)
                    holder.categoryArticleName.text = article.title
                    holder.categoryArticleAuthor.text = author.name
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

    override fun getItemCount(): Int {
        return categories.size
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var category: Category? = null

        val cardView: CardView = itemView.findViewById(R.id.categoryCardView)
        val cardViewBackground: ConstraintLayout = itemView.findViewById(R.id.categoryCardViewBackgroundLayout)
        val categoryArticleImageView: ImageView = itemView.findViewById(R.id.categoryArticleImageView)
        val categoryArticleName: TextView = itemView.findViewById(R.id.categoryArticleNameTextView)
        val categoryArticleAuthor: TextView = itemView.findViewById(R.id.categoryArticleAuthorTextView)
        val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitleTextView)

        init {
            cardView.setOnClickListener {
                val transaction = fragmentManager.beginTransaction()
                val fragment = CategoryArticlesFragment.newInstance(category!!.slug!!)
                transaction.replace(R.id.container, fragment)
                transaction.commit()
            }
        }
    }
}
