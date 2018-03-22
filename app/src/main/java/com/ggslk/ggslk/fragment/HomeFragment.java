package com.ggslk.ggslk.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.ggslk.ggslk.adapter.ArticleRecyclerAdapter;
import com.ggslk.ggslk.model.Article;
import com.ggslk.ggslk.model.Author;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ArticleRecyclerAdapter articleRecyclerAdapter;
    private RequestQueue mRequestQueue;

    private final ArrayList<Article> articles = new ArrayList<>();

    private boolean loading = true;
    private int pastVisibleItems;
    private int visibleItemCount;
    private int totalItemCount;
    private int pageNo = 1;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());

        // Initialize LinearLayoutManager
        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.articleRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        loadRecentPosts(10, pageNo++);

        articleRecyclerAdapter = new ArticleRecyclerAdapter(articles);
        recyclerView.setAdapter(articleRecyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) // Check for scroll down
                {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisibleItems) >= totalItemCount)
                        {
                            loading = false;
                            loadRecentPosts(10, pageNo++);
                        }
                    }
                }
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHomeFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onHomeFragmentInteraction(Uri uri);
    }

    private void loadRecentPosts(int count, int page){
        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_recent_posts?count=" + count + "&page=" + page, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray articlesJsonArray = response.getJSONArray("posts");

                            for (int i = 0; i < articlesJsonArray.length(); i++) {
                                Article article = new Article();
                                article.setTitle(articlesJsonArray.getJSONObject(i).get("title").toString());
                                article.setContent(articlesJsonArray.getJSONObject(i).get("content").toString());
                                article.setPublishedDate(articlesJsonArray.getJSONObject(i).get("date").toString().split(" ")[0]);
                                article.setImageUrl(articlesJsonArray.getJSONObject(i).getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString());

                                Author author = new Author();
                                author.setName(articlesJsonArray.getJSONObject(i).getJSONObject("author").get("name").toString());
                                author.setProfilePictureUrl("");
                                article.setAuthor(author);

                                articles.add(article);
                            }

                            articleRecyclerAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            loading = true;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        loading = true;
                    }
                });
        mRequestQueue.add(jsonRequest);
    }
}
