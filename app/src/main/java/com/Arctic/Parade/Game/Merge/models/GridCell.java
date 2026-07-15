package com.Arctic.Parade.Game.Merge.models;

import java.util.Objects;

public final class GridCell {
    private MergeItem item;
    private final int position;

    public GridCell(MergeItem item, int position) {
        this.item = item;
        this.position = position;
    }

    public MergeItem getItem() {
        return this.item;
    }

    public void setItem(MergeItem item) {
        this.item = item;
    }

    public int getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GridCell)) return false;
        GridCell gridCell = (GridCell) o;
        return position == gridCell.position && Objects.equals(item, gridCell.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, position);
    }

    @Override
    public String toString() {
        return "GridCell(item=" + item + ", position=" + position + ")";
    }
}
