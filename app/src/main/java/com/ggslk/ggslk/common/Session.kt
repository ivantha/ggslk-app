package com.ggslk.ggslk.common

import com.android.volley.RequestQueue
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Category
import java.util.*
import kotlin.collections.HashMap

object Session {
    val categories = ArrayList<Category>()
    val articles = ArrayList<Article>()
    val favorites = HashMap<Int, Article>()

    var mRequestQueue: RequestQueue? = null
}
