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

    init {
        // Add categories
        categories.add(Category("195", "accessories", "Accessories"))
        categories.add(Category("185", "android", "Android"))
        categories.add(Category("193", "chromebooks", "Chromebooks"))
        categories.add(Category("204", "communication_and_publishing", "Communication and Publishing"))
        categories.add(Category("1307", "communities", "Communities"))
        categories.add(Category("189", "connected_homes", "Connected Homes"))
        categories.add(Category("2274", "crowdsource", "Crowdsource"))
        categories.add(Category("187", "deep_learning", "Deep Learning"))
        categories.add(Category("199", "desktop_apps", "Desktop Applications"))
        categories.add(Category("197", "dev_tools", "Development Tools"))
        categories.add(Category("2", "devices", "Devices"))
        categories.add(Category("212", "english", "English"))
        categories.add(Category("210", "events", "Events"))
        categories.add(Category("154", "featured", "Featured"))
        categories.add(Category("209", "gmail", "Gmail"))
        categories.add(Category("2112", "google", "Google"))
        categories.add(Category("208", "google-translator", "Google Translator"))
        categories.add(Category("182", "internet", "Internet"))
        categories.add(Category("749", "learning", "Learning"))
        categories.add(Category("2187", "life-story", "Life Story"))
        categories.add(Category("206", "map_related_products", "Map Related Products"))
        categories.add(Category("200", "mobile_apps", "Mobile Applications"))
        categories.add(Category("2114", "opensource", "Opensource"))
        categories.add(Category("198", "os", "Operating Systems"))
        categories.add(Category("188", "phones-devices", "Phones"))
        categories.add(Category("181", "products", "Products"))
        categories.add(Category("202", "search_tools", "Search Tools"))
        categories.add(Category("183", "security", "Security"))
        categories.add(Category("205", "security_tools", "Security Tools"))
        categories.add(Category("201", "services", "Services"))
        categories.add(Category("211", "sinhala", "Sinhala"))
        categories.add(Category("191", "streaming", "Streaming Devices"))
        categories.add(Category("192", "tablets", "Tablets"))
        categories.add(Category("2279", "tamil", "Tamil"))
        categories.add(Category("184", "technology", "Technology"))
        categories.add(Category("186", "tensorflow", "Tensorflow"))
        categories.add(Category("190", "vr", "Virtual Reality"))
        categories.add(Category("194", "watches", "Watches"))
        categories.add(Category("196", "web_based_products", "Web Based Products"))
    }
}
