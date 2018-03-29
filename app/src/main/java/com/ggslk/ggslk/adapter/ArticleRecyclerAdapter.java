package com.ggslk.ggslk.adapter;

import android.net.Uri;
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

import com.android.volley.RequestQueue;
import com.ggslk.ggslk.R;
import com.ggslk.ggslk.activity.MainActivity;
import com.ggslk.ggslk.model.Article;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleRecyclerAdapter extends Adapter<ArticleRecyclerAdapter.ArticleViewHolder> {
    private StorageReference storageRef;        // Firebase Storage
    private RequestQueue mRequestQueue;         // Volley request queue
    private List<Article> articles;

    public ArticleRecyclerAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_article, parent, false);
        ArticleViewHolder articleViewHolder = new ArticleViewHolder(v);

        mRequestQueue = MainActivity.getmRequestQueue();
        storageRef = MainActivity.getmStorageRef();

        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        storageRef.child("team/" + articles.get(position).getAuthor().getSlug() + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).fit().centerCrop().into(holder.authorImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Some error occurred
            }
        });
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
        private CircleImageView authorImageView;
        private TextView authorName;
        private TextView publishedDate;
        private TextView title;
        private TextView content;
        private ImageView articleImageView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.articleCardView);
            authorImageView = itemView.findViewById(R.id.authorImageView);
            authorName = itemView.findViewById(R.id.authorNameTextView);
            publishedDate = itemView.findViewById(R.id.articleDateTextView);
            title = itemView.findViewById(R.id.articleTitleTextView);
            content = itemView.findViewById(R.id.articleContentTextView);
            articleImageView = itemView.findViewById(R.id.articleImageView);
        }
    }
}