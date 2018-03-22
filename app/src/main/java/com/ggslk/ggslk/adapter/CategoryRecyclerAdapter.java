package com.ggslk.ggslk.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ggslk.ggslk.R;
import com.ggslk.ggslk.model.Category;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private RequestQueue mRequestQueue;

    public CategoryRecyclerAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v);

        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(v.getContext());

        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.categoryArticleName.setText(categories.get(position).getFeaturedArticle().getTitle());
        holder.categoryArticleAuthor.setText(categories.get(position).getFeaturedArticle().getAuthor().getName());
        holder.categoryTitle.setText(categories.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        mRequestQueue.add(new JsonObjectRequest
                (Request.Method.GET, "https://ggslk.com/api/get_category_posts?slug=" + categories.get(position).getSlug() + "&count=1", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String title = response.getJSONArray("posts").getJSONObject(0)
                                    .get("title").toString();
                            String author = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONObject("author").get("name").toString();
                            String imageUrl = response.getJSONArray("posts").getJSONObject(0)
                                    .getJSONObject("thumbnail_images").getJSONObject("full").get("url").toString();

                            categories.get(position).getFeaturedArticle().setTitle(title);
                            categories.get(position).getFeaturedArticle().getAuthor().setName(author);
                            categories.get(position).getFeaturedArticle().setImageUrl(imageUrl);

                            Picasso.get().load(imageUrl).fit().centerCrop().into(holder.categoryArticleImageView);
                            holder.categoryArticleName.setText(title);
                            holder.categoryArticleAuthor.setText(author);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }));
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView categoryArticleImageView;
        private TextView categoryArticleName;
        private TextView categoryArticleAuthor;
        private TextView categoryTitle;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.categoryCardView);
            categoryArticleImageView = itemView.findViewById(R.id.categoryArticleImageView);
            categoryArticleName = itemView.findViewById(R.id.categoryArticleNameTextView);
            categoryArticleAuthor = itemView.findViewById(R.id.categoryArticleAuthorTextView);
            categoryTitle = itemView.findViewById(R.id.categoryTitleTextView);
        }
    }
}
