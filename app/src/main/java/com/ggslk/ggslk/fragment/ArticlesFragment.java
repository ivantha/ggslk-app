package com.ggslk.ggslk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ggslk.ggslk.R;
import com.ggslk.ggslk.activity.MainActivity;
import com.ggslk.ggslk.adapter.CategoryRecyclerAdapter;
import com.ggslk.ggslk.model.Article;
import com.ggslk.ggslk.model.Author;
import com.ggslk.ggslk.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ArticlesFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private CategoryRecyclerAdapter categoryRecyclerAdapter;
    private RequestQueue mRequestQueue;

    private final ArrayList<Category> categories = new ArrayList<>();

    public ArticlesFragment() {
        // Required empty public constructor
    }

    public static ArticlesFragment newInstance() {
        ArticlesFragment fragment = new ArticlesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = MainActivity.getmRequestQueue();
        mRequestQueue = Volley.newRequestQueue(getContext());

        // Initialize GridLayoutManager
        gridLayoutManager = new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        recyclerView = view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_index",null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray categoriesJsonArray = response.getJSONArray("categories");

                            for (int i = 0; i < categoriesJsonArray.length(); i++) {
                                Category category = new Category();
                                category.setSlug(categoriesJsonArray.getJSONObject(i).get("slug").toString());
                                category.setTitle(categoriesJsonArray.getJSONObject(i).get("title").toString());

                                Author author = new Author();
                                author.setName("");

                                Article article = new Article();
                                article.setTitle("");
                                article.setContent("");
                                article.setAuthor(author);

                                category.setFeaturedArticle(article);

                                categories.add(category);
                            }

                            categoryRecyclerAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(jsonRequest);

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(categories);
        recyclerView.setAdapter(categoryRecyclerAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onArticlesFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onArticlesFragmentInteraction(Uri uri);
    }
}
