package com.ggslk.ggslk.adapter

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
import com.ggslk.ggslk.activity.MainActivity
import com.ggslk.ggslk.model.Article
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ArticleRecyclerAdapter(private val articles: List<Article>) : Adapter<ArticleRecyclerAdapter.ArticleViewHolder>() {
    private var storageRef: StorageReference? = null        // Firebase Storage

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_article, parent, false)
        val articleViewHolder = ArticleViewHolder(v)

        storageRef = FirebaseStorage.getInstance().reference

        return articleViewHolder
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        storageRef!!.child("team/" + articles[position].author!!.slug + ".jpg").downloadUrl.addOnSuccessListener { uri -> Picasso.get().load(uri.toString()).fit().centerCrop().into(holder.authorImageView) }.addOnFailureListener {
            // Some error occurred
        }
        holder.authorName.text = articles[position].author!!.name
        holder.publishedDate.text = articles[position].publishedDate
        holder.title.text = Html.fromHtml(articles[position].title)
        holder.content.text = Html.fromHtml(articles[position].content)
        Picasso.get().load(articles[position].imageUrl).fit().centerCrop().into(holder.articleImageView)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.articleCardView)
        val authorImageView: CircleImageView = itemView.findViewById(R.id.authorImageView)
        val authorName: TextView = itemView.findViewById(R.id.authorNameTextView)
        val publishedDate: TextView = itemView.findViewById(R.id.articleDateTextView)
        val title: TextView = itemView.findViewById(R.id.articleTitleTextView)
        val content: TextView = itemView.findViewById(R.id.articleContentTextView)
        val articleImageView: ImageView = itemView.findViewById(R.id.articleImageView)

        init {
            cardView.setOnClickListener {
                val intent = Intent(MainActivity.context, ArticleViewActivity::class.java)
                MainActivity.context!!.startActivity(intent)
            }
        }
    }
}