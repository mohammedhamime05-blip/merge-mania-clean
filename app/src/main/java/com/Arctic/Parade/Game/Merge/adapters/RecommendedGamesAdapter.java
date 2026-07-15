package com.Arctic.Parade.Game.Merge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.Arctic.Parade.Game.Merge.R;
import com.Arctic.Parade.Game.Merge.AdManager.AdItem;
import java.util.List;

public final class RecommendedGamesAdapter extends RecyclerView.Adapter<RecommendedGamesAdapter.ViewHolder> {
    private final List<AdItem> games;
    private final OnGameClickListener listener;

    public interface OnGameClickListener {
        void onGameClick(AdItem game);
    }

    public RecommendedGamesAdapter(List<AdItem> games, OnGameClickListener listener) {
        this.games = games;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(games.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivGameIcon;
        private final TextView tvGameName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivGameIcon = itemView.findViewById(R.id.ivGameIcon);
            tvGameName = itemView.findViewById(R.id.tvGameName);
        }

        public void bind(final AdItem game, final OnGameClickListener listener) {
            tvGameName.setText(game.name);
            
            // Load image using Glide
            Glide.with(itemView.getContext())
                .load(game.imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .into(ivGameIcon);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onGameClick(game);
                }
            });
        }
    }
}
