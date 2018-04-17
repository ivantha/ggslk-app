package com.ggslk.ggslk.model

import java.io.Serializable

class Category : Serializable {
    var id: String? = null
    var slug: String? = null
    var title: String? = null
    var featuredArticle: Article? = null
    var articles: ArrayList<Article> = ArrayList()

    constructor(id: String?, slug: String?, title: String?) {
        this.id = id
        this.slug = slug
        this.title = title
    }
}