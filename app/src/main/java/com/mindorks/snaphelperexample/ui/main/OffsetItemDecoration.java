package com.mindorks.snaphelperexample.ui.main;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class OffsetItemDecoration extends RecyclerView.ItemDecoration {

    private int screenWidth = -1;

    private int itemWidth = 0;
    private int itemSpacing = 0;

    public OffsetItemDecoration(int itemWidth, int itemSpacing) {
        this.itemWidth = itemWidth;
        this.itemSpacing = itemSpacing;
    }

    @Override
    public void getItemOffsets(
            @NonNull Rect outRect,
            @NonNull View view,
            @NonNull RecyclerView parent,
            @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int e = 20;
        int spacing =
                (int) (getScreenWidth(parent.getContext()) / 2f)
                        - view.getLayoutParams().width / 2 - itemWidth / 2 - e;

        int childAdapterPosition = parent.getChildAdapterPosition(view);

        if (childAdapterPosition == 0){
            outRect.left = spacing -  itemSpacing;
        }

        if (childAdapterPosition == state.getItemCount() - 1){
            outRect.right = spacing;
        }else {
            outRect.right = itemSpacing;
        }
    }

    private int getScreenWidth(Context context) {
        if (screenWidth == -1) {
            WindowManager windowManager = ContextCompat.getSystemService(context, WindowManager.class);
            Display display = windowManager.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }
}
