package com.bordozer.jphoto.core.general.cache.entries;

import com.bordozer.jphoto.core.interfaces.Cacheable;

public class RankInGenrePointsEntry implements Cacheable {

    private int rank;
    private int upperPoints;

    public RankInGenrePointsEntry(final int rank, final int upperPoints) {
        this.rank = rank;
        this.upperPoints = upperPoints;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(final int rank) {
        this.rank = rank;
    }

    public int getUpperPoints() {
        return upperPoints;
    }

    public void setUpperPoints(final int upperPoints) {
        this.upperPoints = upperPoints;
    }
}
