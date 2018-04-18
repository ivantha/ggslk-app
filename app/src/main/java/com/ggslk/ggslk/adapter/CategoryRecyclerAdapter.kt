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
import com.ggslk.ggslk.R
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.fragment.CategoryArticlesFragment
import com.ggslk.ggslk.model.Category
import com.squareup.picasso.Picasso

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
