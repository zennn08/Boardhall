package com.example.boardhall;

import java.util.Date;

public class Post {
    private int id;
    private String title;
    private String content;
    private String excerpt;
    private String featuredImage;
    private String datePublished;
    private String author;
    private int categoryId;
    private String categoryName;

    public Post() {}

    public Post(int id, String title, String content, String excerpt,
                String featuredImage, String datePublished, String author,
                int categoryId, String categoryName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.excerpt = excerpt;
        this.featuredImage = featuredImage;
        this.datePublished = datePublished;
        this.author = author;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getExcerpt() { return excerpt; }
    public void setExcerpt(String excerpt) { this.excerpt = excerpt; }

    public String getFeaturedImage() { return featuredImage; }
    public void setFeaturedImage(String featuredImage) { this.featuredImage = featuredImage; }

    public String getDatePublished() { return datePublished; }
    public void setDatePublished(String datePublished) { this.datePublished = datePublished; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}