package com.example.boardhall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categories;
    private OnCategoryClickListener listener;
    private int selectedCategoryId = -1; // Track selected category

    public interface OnCategoryClickListener {
        void onCategoryClick(int categoryId);
    }

    public CategoryAdapter(Context context, List<Category> categories, OnCategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName() + " (" + category.getCount() + ")");

        // Set background and text color based on selection
        if (category.getId() == selectedCategoryId) {
            holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
            holder.categoryName.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.itemView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
            holder.categoryName.setTextColor(context.getResources().getColor(R.color.text_gray));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update selected category
                selectedCategoryId = category.getId();
                notifyDataSetChanged(); // Refresh all items to update selection

                if (listener != null) {
                    listener.onCategoryClick(category.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Method to clear selection when "All Posts" is clicked
    public void clearSelection() {
        selectedCategoryId = -1;
        notifyDataSetChanged();
    }

    // Method to set selected category programmatically
    public void setSelectedCategory(int categoryId) {
        selectedCategoryId = categoryId;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
        }
    }
}