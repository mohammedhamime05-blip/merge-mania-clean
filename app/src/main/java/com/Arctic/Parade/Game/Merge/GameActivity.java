package com.Arctic.Parade.Game.Merge;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import com.Arctic.Parade.Game.Merge.adapters.GameBoardAdapter;
import com.Arctic.Parade.Game.Merge.databinding.ActivityGameBinding;
import com.Arctic.Parade.Game.Merge.models.GridCell;
import com.Arctic.Parade.Game.Merge.models.MergeItem;
import com.Arctic.Parade.Game.Merge.utils.Prefs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameActivity extends AppCompatActivity {
    private ActivityGameBinding binding;
    private GameBoardAdapter adapter;
    private Prefs prefs;
    private int score;
    private Integer selectedPosition = null;
    private final List<GridCell> cells = new ArrayList<>();
    private int stageId = 1;
    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(this);
        stageId = getIntent().getIntExtra("STAGE_ID", 1);

        setupStage();

        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnHome.setOnClickListener(v -> finish());
        binding.btnSpawn.setOnClickListener(v -> spawnItem());
    }

    private void setupStage() {
        binding.tvStageName.setText("Stage " + stageId);
        
        // 3x3 for Stage 1-3, 4x4 for Stage 4-7, 5x5 for Stage 8-10
        int size = 3;
        if (stageId > 3 && stageId <= 7) {
            size = 4;
        } else if (stageId > 7) {
            size = 5;
        }

        int goalLevel = stageId + 2;
        binding.tvGoal.setText("Goal: Level " + goalLevel);
        
        cells.clear();
        int totalCells = size * size;
        for (int i = 0; i < totalCells; i++) {
            cells.add(new GridCell(null, i));
        }

        binding.rvBoard.setLayoutManager(new GridLayoutManager(this, size));
        adapter = new GameBoardAdapter(cells, position -> {
            handleCellClick(position);
            return null;
        });
        binding.rvBoard.setAdapter(adapter);

        // Spawn initial two items
        spawnItem();
        spawnItem();
    }

    private void spawnItem() {
        List<GridCell> emptyCells = new ArrayList<>();
        for (GridCell cell : cells) {
            if (cell.getItem() == null) {
                emptyCells.add(cell);
            }
        }

        if (emptyCells.isEmpty()) {
            Toast.makeText(this, "Board Full!", Toast.LENGTH_SHORT).show();
            return;
        }

        GridCell randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
        randomCell.setItem(new MergeItem(1, 0, System.currentTimeMillis()));
        adapter.notifyItemChanged(randomCell.getPosition());
    }

    private void handleCellClick(int position) {
        GridCell clickedCell = cells.get(position);

        if (selectedPosition == null) {
            if (clickedCell.getItem() != null) {
                selectedPosition = position;
                Toast.makeText(this, "Selected Level " + clickedCell.getItem().getLevel(), Toast.LENGTH_SHORT).show();
            }
        } else {
            int prevPos = selectedPosition;
            GridCell prevCell = cells.get(prevPos);

            if (prevPos == position) {
                // Deselect
                selectedPosition = null;
                Toast.makeText(this, "Deselected", Toast.LENGTH_SHORT).show();
                return;
            }

            if (clickedCell.getItem() == null) {
                // Move item
                clickedCell.setItem(prevCell.getItem());
                prevCell.setItem(null);
                adapter.notifyItemChanged(prevPos);
                adapter.notifyItemChanged(position);
                selectedPosition = null;
            } else {
                // Attempt merge
                MergeItem prevItem = prevCell.getItem();
                MergeItem clickedItem = clickedCell.getItem();

                if (prevItem.getLevel() == clickedItem.getLevel()) {
                    int currentLevel = prevItem.getLevel();
                    if (currentLevel < 10) {
                        int nextLevel = currentLevel + 1;
                        clickedCell.setItem(new MergeItem(nextLevel, 0, System.currentTimeMillis()));
                        prevCell.setItem(null);
                        
                        score += currentLevel * 10;
                        updateUI();
                        
                        adapter.notifyItemChanged(prevPos);
                        adapter.notifyItemChanged(position);
                        
                        checkWin(nextLevel);
                    }
                    selectedPosition = null;
                } else {
                    // Change selection
                    selectedPosition = position;
                    Toast.makeText(this, "Selected Level " + clickedCell.getItem().getLevel(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateUI() {
        binding.tvScore.setText("Score: " + score);
    }

    private void checkWin(int reachedLevel) {
        int goal = stageId + 2;
        if (reachedLevel >= goal) {
            prefs.setLevelUnlocked(stageId + 1);
            prefs.setHighScore(score);

            new AlertDialog.Builder(this)
                .setTitle("Victory!")
                .setMessage("Stage " + stageId + " Cleared!")
                .setPositiveButton("Next Stage", (dialog, which) -> {
                    if (stageId >= 10) {
                        finish();
                    } else {
                        stageId++;
                        score = 0;
                        updateUI();
                        setupStage();
                    }
                })
                .setNegativeButton("Menu", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
        }
    }
}
