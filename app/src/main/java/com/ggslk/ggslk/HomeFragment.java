package com.ggslk.ggslk;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggslk.ggslk.adapter.ArticleRecyclerAdapter;
import com.ggslk.ggslk.model.Article;
import com.ggslk.ggslk.model.Author;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;

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

        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.articleRecyclerView);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        Article a1 = new Article();
        a1.setTitle("Some title");
        a1.setContent("Some content");
        Author aa1 = new Author();
        aa1.setName("My naaaame");
        a1.setAuthor(aa1);
        a1.setPublishedDate("123123123");

        Article a2 = new Article();
        a2.setTitle("Some title");
        a2.setContent("Some content");
        Author aa2 = new Author();
        aa2.setName("My naaaame");
        a2.setAuthor(aa2);
        a2.setPublishedDate("123123123");

        Article a3 = new Article();
        a3.setTitle("Some title");
        a3.setContent("Some content");
        Author aa3 = new Author();
        aa3.setName("My naaaame");
        a3.setAuthor(aa3);
        a3.setPublishedDate("123123123");

        ArrayList<Article> articles = new ArrayList<>();
        articles.add(a1);
        articles.add(a2);
        articles.add(a3);

        ArticleRecyclerAdapter articleRecyclerAdapter = new ArticleRecyclerAdapter(articles);
        recyclerView.setAdapter(articleRecyclerAdapter);

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
}
