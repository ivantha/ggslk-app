package com.ggslk.ggslk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ggslk.ggslk.R;
import com.ggslk.ggslk.model.Article;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

public class ArticleRecyclerAdapter extends Adapter<ArticleRecyclerAdapter.ArticleViewHolder> {
    private List<Article> articles;
    private RequestQueue mRequestQueue;

    public ArticleRecyclerAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_article, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(v);

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(v.getContext());

        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        holder.authorName.setText(articles.get(position).getAuthor().getName());
        holder.publishedDate.setText(articles.get(position).getPublishedDate());
        holder.title.setText(Html.fromHtml(articles.get(position).getTitle()));
        holder.content.setText(Html.fromHtml(articles.get(position).getContent()));
        Picasso.get().load(articles.get(position).getImageUrl()).fit().centerCrop().into(holder.articleImageView);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView authorName;
        private TextView publishedDate;
        private TextView title;
        private TextView content;
        private ImageView articleImageView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.articleCardView);
            authorName = itemView.findViewById(R.id.authorNameTextView);
            publishedDate = itemView.findViewById(R.id.articleDateTextView);
            title = itemView.findViewById(R.id.articleTitleTextView);
            content = itemView.findViewById(R.id.articleContentTextView);
            articleImageView = itemView.findViewById(R.id.articleImageView);
        }
    }
}