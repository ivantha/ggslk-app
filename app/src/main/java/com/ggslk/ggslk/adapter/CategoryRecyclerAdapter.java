package com.ggslk.ggslk.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggslk.ggslk.R;
import com.ggslk.ggslk.model.Category;

import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    List<Category> categories;

    public CategoryRecyclerAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_category, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(v);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
//        holder.content.setImageResource(persons.get(i).photoId);
        holder.categoryArticleName.setText(categories.get(position).getFeaturedArticle().getTitle());
        holder.categoryArticleAuthor.setText(categories.get(position).getFeaturedArticle().getAuthor().getName());
        holder.categoryTitle.setText(categories.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return categories.size();
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
//            image = (ImageView)itemView.findViewById(R.id.person_photo);
            categoryArticleName = itemView.findViewById(R.id.categoryArticleNameTextView);
            categoryArticleAuthor = itemView.findViewById(R.id.categoryArticleAuthorTextView);
            categoryTitle = itemView.findViewById(R.id.categoryTitleTextView);
        }
    }
}
