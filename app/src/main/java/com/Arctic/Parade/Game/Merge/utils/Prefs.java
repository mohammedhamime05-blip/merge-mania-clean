package com.Arctic.Parade.Game.Merge.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
    private final SharedPreferences prefs;

    public Prefs(Context context) {
        this.prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE);
    }

    public void setLevelUnlocked(int level) {
        this.prefs.edit().putBoolean("level_" + level + "_unlocked", true).apply();
    }

    public boolean isLevelUnlocked(int level) {
        if (level == 1) {
            return true;
        }
        return this.prefs.getBoolean("level_" + level + "_unlocked", false);
    }

    public void setHighScore(int score) {
        if (score > getHighScore()) {
            this.prefs.edit().putInt("high_score", score).apply();
        }
    }

    public int getHighScore() {
        return this.prefs.getInt("high_score", 0);
    }
}
