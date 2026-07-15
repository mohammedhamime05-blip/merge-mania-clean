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
    private static final String CONFIG_JSON_URL = "https://gist.githubusercontent.com/laourdani/437d9f1959cc6eb7e1be3627f7eb264c/raw/";

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
            Intent intent = new Intent(MainActivity.this, StageSelectionActivity.class);
            startActivity(intent);
        });

        // Initialize AdManager to fetch the configuration (if the user wants to host cross-promotions)
        adManager = new AdManager(this);
        adManager.setOnStatusListener(isActive -> {
            LogAdStatus(isActive);
            if (isActive && adManager.getAdItems() != null && !adManager.getAdItems().isEmpty()) {
                binding.tvMoreGamesLabel.setVisibility(View.VISIBLE);
                binding.rvRecommendedGames.setVisibility(View.VISIBLE);
                binding.rvRecommendedGames.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                RecommendedGamesAdapter adapter = new RecommendedGamesAdapter(adManager.getAdItems(), game -> {
                    adManager.openGameOnStore(game.asin);
                });
                binding.rvRecommendedGames.setAdapter(adapter);
            } else {
                binding.tvMoreGamesLabel.setVisibility(View.GONE);
                binding.rvRecommendedGames.setVisibility(View.GONE);
            }
        });
        adManager.fetchConfig(CONFIG_JSON_URL);
    }

    private void LogAdStatus(boolean isActive) {
        android.util.Log.d("MainActivity", "Cross-promotion configuration loaded. Active: " + isActive);
    }
}
