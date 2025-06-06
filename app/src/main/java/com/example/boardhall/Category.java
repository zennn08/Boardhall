package com.example.boardhall;

public class Category {
    private int id;
    private String name;
    private String slug;
    private int count;

    public Category() {}

    public Category(int id, String name, String slug, int count) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.count = count;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}