package com.Arctic.Parade.Game.Merge.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.Arctic.Parade.Game.Merge.R;
import com.Arctic.Parade.Game.Merge.databinding.ItemGridCellBinding;
import com.Arctic.Parade.Game.Merge.models.GridCell;
import com.Arctic.Parade.Game.Merge.models.MergeItem;
import java.util.List;
import kotlin.jvm.functions.Function1;

public final class GameBoardAdapter extends RecyclerView.Adapter<GameBoardAdapter.ViewHolder> {
    private List<GridCell> cells;
    private final Function1<Integer, Void> onCellClicked;

    public GameBoardAdapter(List<GridCell> cells, Function1<Integer, Void> onCellClicked) {
        this.cells = cells;
        this.onCellClicked = onCellClicked;
    }

    public List<GridCell> getCells() {
        return this.cells;
    }

    public void setCells(List<GridCell> cells) {
        this.cells = cells;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGridCellBinding binding = ItemGridCellBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(cells.get(position));
    }

    @Override
    public int getItemCount() {
        return this.cells.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemGridCellBinding binding;

        public ViewHolder(ItemGridCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(GridCell cell) {
            MergeItem item = cell.getItem();
            if (item != null) {
                binding.ivItem.setImageResource(getItemIcon(item.getLevel()));
                binding.tvLv.setText(String.valueOf(item.getLevel()));
                binding.getRoot().setAlpha(1.0f);
            } else {
                binding.ivItem.setImageDrawable(null);
                binding.tvLv.setText("");
                binding.getRoot().setAlpha(0.5f);
            }

            binding.getRoot().setOnClickListener(v -> {
                onCellClicked.invoke(getAdapterPosition());
            });
        }

        private int getItemIcon(int level) {
            switch (level) {
                case 1:
                    return R.drawable.merge_item_1;
                case 2:
                    return R.drawable.merge_item_2;
                case 3:
                    return R.drawable.merge_item_3;
                case 4:
                    return R.drawable.merge_item_4;
                case 5:
                    return R.drawable.merge_item_5;
                case 6:
                    return R.drawable.merge_item_6;
                case 7:
                    return R.drawable.merge_item_7;
                case 8:
                    return R.drawable.merge_item_8;
                case 9:
                    return R.drawable.merge_item_9;
                default:
                    return R.drawable.merge_item_10;
            }
        }
    }
}
