package com.verisence.zoackadventures.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class OverlapDecoration extends RecyclerView.ItemDecoration {

    private final static int vertOverlap = -120;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(vertOverlap, 0, 0, 0);

    }
}