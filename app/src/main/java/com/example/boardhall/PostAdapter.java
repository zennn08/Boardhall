package com.example.boardhall;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Set title
        holder.titleTextView.setText(Html.fromHtml(post.getTitle(), Html.FROM_HTML_MODE_LEGACY));

        // Set excerpt
        String excerpt = post.getExcerpt();
        if (excerpt != null && !excerpt.isEmpty()) {
            holder.excerptTextView.setText(Html.fromHtml(excerpt, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.excerptTextView.setText("Tidak ada preview konten");
        }

        // Set date
        holder.dateTextView.setText(post.getDatePublished());

        // Set category
        if (post.getCategoryName() != null) {
            holder.categoryTextView.setText(post.getCategoryName());
        }

        // Load featured image with Glide
        if (post.getFeaturedImage() != null && !post.getFeaturedImage().isEmpty()) {
            Glide.with(context)
                    .load(post.getFeaturedImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.featuredImageView);
        } else {
            holder.featuredImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailPostActivity.class);
            intent.putExtra("post_id", post.getId());
            intent.putExtra("post_title", post.getTitle());
            intent.putExtra("post_content", post.getContent());
            intent.putExtra("post_image", post.getFeaturedImage());
            intent.putExtra("post_date", post.getDatePublished());
            intent.putExtra("post_author", post.getAuthor());
            intent.putExtra("post_category", post.getCategoryName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<Post> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView featuredImageView;
        TextView titleTextView;
        TextView excerptTextView;
        TextView dateTextView;
        TextView categoryTextView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            featuredImageView = itemView.findViewById(R.id.featuredImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            excerptTextView = itemView.findViewById(R.id.excerptTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
        }
    }
}