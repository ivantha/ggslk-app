package com.ggslk.ggslk.model

import java.io.Serializable

class Article : Serializable{
    var id: String? = null
    var title: String? = null
    var content: String? = null
    var author: Author? = null
    var publishedDate: String? = null
    var imageUrl: String? = null
}