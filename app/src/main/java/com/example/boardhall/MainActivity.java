package com.example.boardhall;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts;
    private RequestQueue requestQueue;

    private CategoryAdapter categoryAdapter;
    private boolean isShowingAllPosts = true;

    private static final String BASE_URL = "https://boardhall.web.id/wp-json/wp/v2/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupToolbar();
        setupDrawer();
        setupRecyclerView();

        requestQueue = Volley.newRequestQueue(this);
        loadPosts();
        loadCategories();
    }

    private void initViews() {
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupDrawer() {
        TextView allPostsTextView = findViewById(R.id.allPostsTextView);
        allPostsTextView.setOnClickListener(v -> {
            loadPosts();
            isShowingAllPosts = true;
            updateAllPostsAppearance();
            if (categoryAdapter != null) {
                categoryAdapter.clearSelection();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Set initial appearance
        updateAllPostsAppearance();
    }

    private void setupRecyclerView() {
        posts = new ArrayList<>();
        postAdapter = new PostAdapter(this, posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(postAdapter);
    }

    private void loadPosts() {
        String url = BASE_URL + "posts?_embed";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject postObj = response.getJSONObject(i);
                                Post post = parsePost(postObj);
                                posts.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        postAdapter.updatePosts(posts);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Error loading posts: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void loadCategories() {
        String url = BASE_URL + "categories?per_page=50";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<Category> categories = new ArrayList<>();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject catObj = response.getJSONObject(i);
                                // Skip "Uncategorized" category and only show categories with posts
                                if (catObj.getInt("count") > 0 && !catObj.getString("slug").equals("uncategorized")) {
                                    Category category = new Category();
                                    category.setId(catObj.getInt("id"));
                                    category.setName(catObj.getString("name"));
                                    category.setSlug(catObj.getString("slug"));
                                    category.setCount(catObj.getInt("count"));
                                    categories.add(category);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (categories.size() > 0) {
                            setupCategoriesRecyclerView(categories);
                        } else {
                            Toast.makeText(MainActivity.this, "No categories found", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Error loading categories: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }

    private void setupCategoriesRecyclerView(List<Category> categories) {
        RecyclerView categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);
        categoryAdapter = new CategoryAdapter(this, categories, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(int categoryId) {
                loadPostsByCategory(categoryId);
                isShowingAllPosts = false;
                updateAllPostsAppearance();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void loadPostsByCategory(int categoryId) {
        String url = BASE_URL + "posts?categories=" + categoryId + "&_embed&per_page=20";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        posts.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject postObj = response.getJSONObject(i);
                                Post post = parsePost(postObj);
                                posts.add(post);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        postAdapter.updatePosts(posts);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,
                                "Error loading posts: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }

    private Post parsePost(JSONObject postObj) throws JSONException {
        Post post = new Post();

        post.setId(postObj.getInt("id"));
        post.setTitle(postObj.getJSONObject("title").getString("rendered"));
        post.setContent(postObj.getJSONObject("content").getString("rendered"));
        post.setExcerpt(postObj.getJSONObject("excerpt").getString("rendered"));

        // Parse date
        String dateString = postObj.getString("date");
        post.setDatePublished(formatDate(dateString));

        // Get featured image
        try {
            JSONObject embedded = postObj.getJSONObject("_embedded");
            if (embedded.has("wp:featuredmedia")) {
                JSONArray featuredMedia = embedded.getJSONArray("wp:featuredmedia");
                if (featuredMedia.length() > 0) {
                    JSONObject media = featuredMedia.getJSONObject(0);
                    post.setFeaturedImage(media.getString("source_url"));
                }
            }
        } catch (JSONException e) {
            // No featured image
        }

        // Get author
        try {
            JSONObject embedded = postObj.getJSONObject("_embedded");
            if (embedded.has("author")) {
                JSONArray authors = embedded.getJSONArray("author");
                if (authors.length() > 0) {
                    JSONObject author = authors.getJSONObject(0);
                    post.setAuthor(author.getString("name"));
                }
            }
        } catch (JSONException e) {
            post.setAuthor("Unknown");
        }

        // Get category
        try {
            JSONArray categories = postObj.getJSONArray("categories");
            if (categories.length() > 0) {
                int categoryId = categories.getInt(0);
                post.setCategoryId(categoryId);

                // Get category name from embedded data
                JSONObject embedded = postObj.getJSONObject("_embedded");
                if (embedded.has("wp:term")) {
                    JSONArray terms = embedded.getJSONArray("wp:term");
                    if (terms.length() > 0) {
                        JSONArray categoryTerms = terms.getJSONArray(0);
                        if (categoryTerms.length() > 0) {
                            JSONObject category = categoryTerms.getJSONObject(0);
                            post.setCategoryName(category.getString("name"));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            post.setCategoryName("Uncategorized");
        }

        return post;
    }

    private void updateAllPostsAppearance() {
        TextView allPostsTextView = findViewById(R.id.allPostsTextView);
        if (isShowingAllPosts) {
            allPostsTextView.setBackgroundColor(getResources().getColor(R.color.light_gray));
            allPostsTextView.setTextColor(getResources().getColor(R.color.black));
        } else {
            allPostsTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            allPostsTextView.setTextColor(getResources().getColor(R.color.text_gray));
        }
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateString;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}