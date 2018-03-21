package com.ggslk.ggslk;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.View;
import android.view.ViewGroup;

public class ArticleRecyclerAdapter extends Adapter<ArticleRecyclerAdapter.ArticleViewHolder> {
    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private CardView view;
        private Article article;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            
        }
    }
}