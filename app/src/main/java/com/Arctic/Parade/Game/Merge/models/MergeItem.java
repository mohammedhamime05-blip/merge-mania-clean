package com.Arctic.Parade.Game.Merge.models;

public final class MergeItem {
    private final int level;
    private final int iconId;
    private long id;

    public MergeItem(int level, int iconId, long id) {
        this.level = level;
        this.iconId = iconId;
        this.id = id;
    }

    public int getLevel() {
        return this.level;
    }

    public int getIconId() {
        return this.iconId;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MergeItem)) return false;
        MergeItem mergeItem = (MergeItem) o;
        return level == mergeItem.level && iconId == mergeItem.iconId && id == mergeItem.id;
    }

    @Override
    public int hashCode() {
        int result = level;
        result = 31 * result + iconId;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "MergeItem(level=" + level + ", iconId=" + iconId + ", id=" + id + ")";
    }
}
