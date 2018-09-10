package com.ggslk.ggslk.model

import java.io.Serializable

class Category(var id: String?, var slug: String?, var title: String?) : Serializable {
    var featuredArticle: Article? = null
    var articles: ArrayList<Article> = ArrayList()
}