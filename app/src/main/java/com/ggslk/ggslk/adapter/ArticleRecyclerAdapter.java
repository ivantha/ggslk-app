package com.ggslk.ggslk.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ggslk.ggslk.R;
import com.ggslk.ggslk.model.Article;

import java.util.List;

public class ArticleRecyclerAdapter extends Adapter<ArticleRecyclerAdapter.ArticleViewHolder> {
    List<Article> articles;

    public ArticleRecyclerAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_article, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(v);
        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
//        holder.content.setImageResource(persons.get(i).photoId);
        holder.authorName.setText(articles.get(position).getAuthor().getName());
        holder.publishedDate.setText(articles.get(position).getPublishedDate());
        holder.title.setText(Html.fromHtml(articles.get(position).getTitle()));
        holder.content.setText(Html.fromHtml(articles.get(position).getContent()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView authorName;
        private TextView publishedDate;
        private TextView title;
        private TextView content;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.articleCardView);
//            image = (ImageView)itemView.findViewById(R.id.person_photo);
            authorName = itemView.findViewById(R.id.authorNameTextView);
            publishedDate = itemView.findViewById(R.id.articleDateTextView);
            title = itemView.findViewById(R.id.articleTitleTextView);
            content = itemView.findViewById(R.id.articleContentTextView);
        }
    }
}