package com.ggslk.ggslk.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ggslk.ggslk.R
import com.ggslk.ggslk.activity.ArticleViewActivity
import com.ggslk.ggslk.model.Article
import com.squareup.picasso.Picasso

class SmallArticleRecyclerAdapter(private val context: Context, private val articles: List<Article>) : RecyclerView.Adapter<SmallArticleRecyclerAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_article_small, parent, false)
        return ArticleViewHolder(v)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.article = articles[position]
        holder.title.text = Html.fromHtml(articles[position].title)
        holder.content.text = Html.fromHtml(articles[position].content)
        Picasso.get().load(articles[position].imageUrl).fit().centerCrop().into(holder.articleImageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var article: Article? = null

        val cardView: CardView = itemView.findViewById(R.id.recyclerViewArticleSmallCardView)
        val title: TextView = itemView.findViewById(R.id.recyclerViewArticleSmallTitleTextView)
        val content: TextView = itemView.findViewById(R.id.recyclerViewArticleSmallContentTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.recyclerViewArticleSmallImageView)

        init {
            cardView.setOnClickListener {
                val intent = Intent(context, ArticleViewActivity::class.java)
                intent.putExtra("article", article)
                context.startActivity(intent)
            }
        }
    }
}