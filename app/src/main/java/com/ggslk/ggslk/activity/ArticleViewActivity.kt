package com.ggslk.ggslk.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.ggslk.ggslk.R
import com.ggslk.ggslk.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_article_view.*


class ArticleViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Set article
        var article: Article = intent.getSerializableExtra("article") as Article
        articleActivityTitleTextView.text = article.title
        Picasso.get().load(article.imageUrl).fit().centerCrop().into(articleActivityImageImageView)
        articleActivityContentWebView.loadData(article.content, "text/html", "UTF-8")

        // Set author
        articleActivityAuthorNameTextView.text = article.author!!.name
        Picasso.get().load("file:///android_asset/team/${article.author!!.id}.jpg").fit().centerCrop().into(articleActivityAuthorImageView)
        articleActivityArticleDateTextView.text = article.publishedDate
    }
}
