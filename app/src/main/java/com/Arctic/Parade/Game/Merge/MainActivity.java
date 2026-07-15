package com.Arctic.Parade.Game.Merge;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.Arctic.Parade.Game.Merge.adapters.RecommendedGamesAdapter;
import com.Arctic.Parade.Game.Merge.databinding.ActivityMainBinding;

public final class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AdManager adManager;

    // The user can host their JSON on GitHub and replace this URL with their raw GitHub Gist/Repository link
    private static final String CONFIG_JSON_URL = "https://raw.githubusercontent.com/mohammedhamime05-blip/merge-mania-clean/main/config.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animate title and logo
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        binding.logoImage.startAnimation(fadeIn);
        binding.gameTitle.startAnimation(fadeIn);

        // Play Button Click Listener
        binding.btnPlay.setOnClickListener(v -> {
            if (adManager != null && adManager.isActive() && adManager.getAdItems() != null && !adManager.getAdItems().isEmpty()) {
                // Pick a random game from the 5 recommended games
                java.util.Random random = new java.util.Random();
                AdManager.AdItem randomGame = adManager.getAdItems().get(random.nextInt(adManager.getAdItems().size()));
                adManager.openGameOnStore(randomGame.asin);
            } else {
                Intent intent = new Intent(MainActivity.this, StageSelectionActivity.class);
                startActivity(intent);
            }
        });

        // Initialize AdManager to fetch the configuration (if the user wants to host cross-promotions)
        adManager = new AdManager(this);
        adManager.setOnStatusListener(isActive -> {
            LogAdStatus(isActive);
            // Hide the bottom slider as requested (they shouldn't be listed there)
            binding.tvMoreGamesLabel.setVisibility(View.GONE);
            binding.rvRecommendedGames.setVisibility(View.GONE);
        });
        adManager.fetchConfig(CONFIG_JSON_URL);
    }

    private void LogAdStatus(boolean isActive) {
        android.util.Log.d("MainActivity", "Cross-promotion configuration loaded. Active: " + isActive);
    }
}
