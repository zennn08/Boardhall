package com.example.boardhall;

import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

public class DetailPostActivity extends AppCompatActivity {
    private ImageView featuredImageView;
    private TextView titleTextView;
    private TextView metaTextView;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        initViews();
        setupToolbar();
        loadPostData();
    }

    private void initViews() {
        featuredImageView = findViewById(R.id.detailFeaturedImageView);
        titleTextView = findViewById(R.id.detailTitleTextView);
        metaTextView = findViewById(R.id.detailMetaTextView);
        contentTextView = findViewById(R.id.detailContentTextView);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Detail Post");
        }
    }

    private void loadPostData() {
        // Get data from intent
        String title = getIntent().getStringExtra("post_title");
        String content = getIntent().getStringExtra("post_content");
        String image = getIntent().getStringExtra("post_image");
        String date = getIntent().getStringExtra("post_date");
        String author = getIntent().getStringExtra("post_author");
        String category = getIntent().getStringExtra("post_category");

        // Set title
        if (title != null) {
            titleTextView.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));
        }

        // Set meta information
        StringBuilder meta = new StringBuilder();
        if (date != null) {
            meta.append(date);
        }
        if (author != null && !author.isEmpty()) {
            if (meta.length() > 0) meta.append(" • ");
            meta.append("By ").append(author);
        }
        if (category != null && !category.isEmpty()) {
            if (meta.length() > 0) meta.append(" • ");
            meta.append(category);
        }
        metaTextView.setText(meta.toString());

        // Set content
        if (content != null) {
            contentTextView.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY));
        }

        // Load featured image
        if (image != null && !image.isEmpty()) {
            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(featuredImageView);
        } else {
            featuredImageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}