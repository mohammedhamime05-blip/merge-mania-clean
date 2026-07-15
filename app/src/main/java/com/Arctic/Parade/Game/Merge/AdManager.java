package com.Arctic.Parade.Game.Merge;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class AdManager {
    private static final String TAG = "AdManager";
    private final Activity activity;
    private OnStatusListener statusListener;
    private final OkHttpClient client = new OkHttpClient();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final List<AdItem> adItems = new ArrayList<>();
    private boolean isActive = false;

    public interface OnStatusListener {
        void onStatusChanged(boolean isActive);
    }

    public void setOnStatusListener(OnStatusListener onStatusListener) {
        this.statusListener = onStatusListener;
    }

    public static class AdItem {
        public String asin;
        public String name;
        public String imageUrl;

        AdItem(String asin, String name, String imageUrl) {
            this.asin = asin;
            this.name = name;
            this.imageUrl = imageUrl;
        }
    }

    public AdManager(Activity activity) {
        this.activity = activity;
    }

    public void fetchConfig(String url) {
        Request request = new Request.Builder()
                .url(url + "?cb=" + System.currentTimeMillis())
                .addHeader("Cache-Control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch config", e);
                mainHandler.post(() -> {
                    isActive = false;
                    if (statusListener != null) {
                        statusListener.onStatusChanged(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    mainHandler.post(() -> {
                        isActive = false;
                        if (statusListener != null) {
                            statusListener.onStatusChanged(false);
                        }
                    });
                    return;
                }

                try {
                    String body = response.body().string();
                    JSONObject json = new JSONObject(body);
                    isActive = json.optBoolean("isActive", false);
                    
                    adItems.clear();
                    if (json.has("games")) {
                        JSONArray games = json.getJSONArray("games");
                        for (int i = 0; i < games.length(); i++) {
                            JSONObject game = games.getJSONObject(i);
                            adItems.add(new AdItem(
                                game.getString("asin"),
                                game.optString("name", ""),
                                game.optString("imageUrl", "")
                            ));
                        }
                    }

                    mainHandler.post(() -> {
                        if (statusListener != null) {
                            statusListener.onStatusChanged(isActive);
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "Error parsing config JSON", e);
                    mainHandler.post(() -> {
                        isActive = false;
                        if (statusListener != null) {
                            statusListener.onStatusChanged(false);
                        }
                    });
                }
            }
        });
    }

    // A safe function to open the Amazon Appstore link when the user wants to see more games
    public void openGameOnStore(String asin) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("amzn://apps/android?asin=" + asin));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amazon.com/gp/product/" + asin));
                activity.startActivity(intent);
            } catch (Exception ex) {
                Log.e(TAG, "Could not open Amazon Appstore links", ex);
            }
        }
    }

    public List<AdItem> getAdItems() {
        return adItems;
    }

    public boolean isActive() {
        return isActive;
    }
}
