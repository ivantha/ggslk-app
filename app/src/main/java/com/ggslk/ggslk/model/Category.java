package com.ggslk.ggslk.model;

public class Category {
    private String id;
    private String slug;
    private String title;
    private Article featuredArticle;

    public Category() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Article getFeaturedArticle() {
        return featuredArticle;
    }

    public void setFeaturedArticle(Article featuredArticle) {
        this.featuredArticle = featuredArticle;
    }
}
