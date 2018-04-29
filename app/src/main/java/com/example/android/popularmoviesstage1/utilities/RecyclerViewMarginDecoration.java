package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.popularmoviesstage1.R;

public class RecyclerViewMarginDecoration extends RecyclerView.ItemDecoration{
    private int marginTopBotton;
    private int marginLeftRight;

    public RecyclerViewMarginDecoration(Context context)
    {
        marginTopBotton = context.getResources().getDimensionPixelSize(R.dimen.item_margin_topbottom);
        marginLeftRight = context.getResources().getDimensionPixelSize(R.dimen.item_margin_leftright);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(marginLeftRight, marginTopBotton, marginLeftRight, marginTopBotton);
    }
}
