package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ggslk.ggslk.R
import com.ggslk.ggslk.adapter.SmallArticleRecyclerAdapter
import com.ggslk.ggslk.common.Session
import kotlinx.android.synthetic.main.fragment_favorites.*

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    private var smallArticleRecyclerAdapter: SmallArticleRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var linearLayoutManager = LinearLayoutManager(context)
        favoriteRecyclerView.layoutManager = linearLayoutManager
        favoriteRecyclerView.setHasFixedSize(true)

        smallArticleRecyclerAdapter = SmallArticleRecyclerAdapter(context!!, Session.favorites.values.toList())
        favoriteRecyclerView.adapter = smallArticleRecyclerAdapter
    }
}
