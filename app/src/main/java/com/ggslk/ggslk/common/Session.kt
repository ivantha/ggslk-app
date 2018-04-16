package com.ggslk.ggslk.common

import com.android.volley.RequestQueue
import com.ggslk.ggslk.model.Article
import com.ggslk.ggslk.model.Category
import java.util.*

object Session {
    val categories = ArrayList<Category>()
    val articles = ArrayList<Article>()

    var mRequestQueue: RequestQueue? = null
}
