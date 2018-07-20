package com.ggslk.ggslk.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Html
import com.ggslk.ggslk.R
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import com.ggslk.ggslk.model.Article
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_article_view.*
import android.webkit.WebViewClient
import org.jsoup.Jsoup


class ArticleViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_view)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Enable JavaScript on WebView
        articleActivityContentWebView.settings.javaScriptEnabled = true
        articleActivityContentWebView.settings.javaScriptCanOpenWindowsAutomatically = true
//        articleActivityContentWebView.setWebViewClient(WebViewClient())

        // Format content to be mobile responsive
        var article: Article = intent.getSerializableExtra("article") as Article
        val doc = Jsoup.parse(article.content)
        doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "style.css");
        article.content = doc.outerHtml()

        // Set article
        articleActivityTitleTextView.text = Html.fromHtml(article.title)
        Picasso.get().load(article.imageUrl).fit().centerCrop().into(articleActivityImageImageView)
        articleActivityContentWebView.loadDataWithBaseURL("file:///android_asset/", article.content, "text/html", "UTF-8", null)

        // Set author
        articleActivityAuthorNameTextView.text = article.author!!.name
        Picasso.get().load("file:///android_asset/team/${article.author!!.id}.jpg").fit().centerCrop().into(articleActivityAuthorImageView)
        articleActivityArticleDateTextView.text = article.publishedDate
        articleActivityFavButton.isFavorite = Session.favorites.containsKey(article.id!!.toInt())

        articleActivityFavButton.setOnFavoriteChangeListener { _, favorite ->
            if (favorite) {
                Session.favorites[article.id!!.toInt()] = article
                SaveHandler.save(this@ArticleViewActivity, "favorites", Session.favorites)
            } else {
                Session.favorites.remove(article.id!!.toInt())
                SaveHandler.save(this@ArticleViewActivity, "favorites", Session.favorites)
            }
        }
    }
}
