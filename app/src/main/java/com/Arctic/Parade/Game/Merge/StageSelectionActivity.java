package com.Arctic.Parade.Game.Merge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.Arctic.Parade.Game.Merge.databinding.ActivityStagesBinding;
import com.Arctic.Parade.Game.Merge.utils.Prefs;
import java.util.ArrayList;
import java.util.List;

public final class StageSelectionActivity extends AppCompatActivity {
    private ActivityStagesBinding binding;
    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = new Prefs(this);

        binding.rvStages.setLayoutManager(new GridLayoutManager(this, 2));

        List<Integer> stages = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            stages.add(i);
        }

        binding.rvStages.setAdapter(new StageAdapter(this, stages));

        binding.btnBackToMenu.setOnClickListener(v -> finish());
    }

    public final class StageAdapter extends RecyclerView.Adapter<StageAdapter.ViewHolder> {
        private final Context context;
        private final List<Integer> stages;

        public StageAdapter(Context context, List<Integer> stages) {
            this.context = context;
            this.stages = stages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            final int stageId = stages.get(position);
            TextView textView = holder.itemView.findViewById(android.R.id.text1);
            textView.setText("Stage " + stageId);
            textView.setTextColor(androidx.core.content.ContextCompat.getColor(context, android.R.color.white));
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.rounded_stage_bg);

            final boolean isUnlocked = prefs.isLevelUnlocked(stageId);
            holder.itemView.setAlpha(isUnlocked ? 1.0f : 0.5f);

            holder.itemView.setOnClickListener(v -> {
                if (isUnlocked) {
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putExtra("STAGE_ID", stageId);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return stages.size();
        }

        public final class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}
