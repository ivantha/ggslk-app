package com.ggslk.ggslk.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ggslk.ggslk.R
import com.ggslk.ggslk.activity.ArticleViewActivity
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.like.LikeButton
import com.like.OnLikeListener
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ArticleRecyclerAdapter(private val context: Context, private val articles: List<Article>) : Adapter<ArticleRecyclerAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_article, parent, false)
        return ArticleViewHolder(v)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.article = articles[position]
        Picasso.get().load("file:///android_asset/team/${articles[position].author!!.id}.jpg").fit().centerCrop().into(holder.authorImageView)
        holder.authorName.text = articles[position].author!!.name
        holder.publishedDate.text = articles[position].publishedDate
        holder.title.text = Html.fromHtml(articles[position].title)
        holder.content.text = Html.fromHtml(articles[position].content)
        Picasso.get().load(articles[position].imageUrl).fit().centerCrop().into(holder.articleImageView)

        if (Session.favorites.containsKey(articles[position].id!!.toInt())) {
            holder.articleFavButton.isLiked = true
        }
        if (Session.liked.containsKey(articles[position].id!!.toInt())) {
            holder.articleLikeButton.isLiked = true
        }
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var article: Article? = null

        val cardView: CardView = itemView.findViewById(R.id.articleCardView)
        val authorImageView: CircleImageView = itemView.findViewById(R.id.authorImageView)
        val authorName: TextView = itemView.findViewById(R.id.authorNameTextView)
        val publishedDate: TextView = itemView.findViewById(R.id.articleDateTextView)
        val title: TextView = itemView.findViewById(R.id.articleActivityTitleTextView)
        val content: TextView = itemView.findViewById(R.id.articleContentTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)
        val articleLikeButton: LikeButton = itemView.findViewById(R.id.articleActivityLikeButton)
        val articleFavButton: LikeButton = itemView.findViewById(R.id.articleActivityFavoriteButton)

        init {
            cardView.setOnClickListener {
                val intent = Intent(context, ArticleViewActivity::class.java)
                intent.putExtra("article", article)
                context.startActivity(intent)
            }

            articleFavButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    Session.favorites[article!!.id!!.toInt()] = article!!
                    SaveHandler.save(context, "favorites", Session.favorites)
                }

                override fun unLiked(likeButton: LikeButton) {
                    Session.favorites.remove(article!!.id!!.toInt())
                    SaveHandler.save(context, "favorites", Session.favorites)
                }
            })

            articleLikeButton.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton) {
                    Session.liked[article!!.id!!.toInt()] = article!!
                    SaveHandler.save(context, "liked", Session.liked)
                }

                override fun unLiked(likeButton: LikeButton) {
                    Session.liked.remove(article!!.id!!.toInt())
                    SaveHandler.save(context, "liked", Session.liked)
                }
            })
        }
    }
}