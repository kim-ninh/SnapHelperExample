package com.mindorks.snaphelperexample.utils;

import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class SnapHelperExt {
    private static String TAG = SnapHelperExt.class.getSimpleName();

    public static int getSnapPosition(
            SnapHelper snapHelper,
            RecyclerView recyclerView
            ){

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager == null){
            Log.d(TAG, "getSnapPosition: layoutManager == null");
            return RecyclerView.NO_POSITION;
        }

        final View snapView = snapHelper.findSnapView(layoutManager);

        if (snapView != null) {
            Log.d(TAG, "getSnapPosition: snapView == null");
            return RecyclerView.NO_POSITION;
        }

        return layoutManager.getPosition(snapView);
    }
}
