package com.ggslk.ggslk.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ggslk.ggslk.R
import com.ggslk.ggslk.adapter.CategoryRecyclerAdapter
import com.ggslk.ggslk.common.SaveHandler
import com.ggslk.ggslk.common.Session
import kotlinx.android.synthetic.main.fragment_categories.*


class CategoriesFragment : Fragment() {

    private var categoryRecyclerAdapter: CategoryRecyclerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        categoryRecyclerView.layoutManager = gridLayoutManager
        categoryRecyclerView.setHasFixedSize(true)

        categoryRecyclerAdapter = CategoryRecyclerAdapter(context!!, Session.categories, fragmentManager!!)
        categoryRecyclerView.adapter = categoryRecyclerAdapter

        Session.mRequestQueue!!.cancelAll("cat")
    }

    override fun onDestroy() {
        super.onDestroy()

        SaveHandler.save(context!!, "categories", Session.categories)
    }

    companion object {
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

}
