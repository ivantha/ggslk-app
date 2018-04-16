package com.ggslk.ggslk.model

import java.io.Serializable

class Category : Serializable {
    var id: String? = null
    var slug: String? = null
    var title: String? = null
    var featuredArticle: Article? = null
    var articles: ArrayList<Article> = ArrayList()
}
