package com.mindorks.snaphelperexample;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class RecyclerViewExt {
    public static void attachSnapHelperWithListener(
            RecyclerView recyclerView,
            SnapHelper snapHelper,
            SnapOnScrollListener.Behaviour behaviour,
            OnSnapPositionChangeListener onSnapPositionChangeListener
    ){
        snapHelper.attachToRecyclerView(recyclerView);
        SnapOnScrollListener snapOnScrollListener = new SnapOnScrollListener(
                snapHelper, behaviour, onSnapPositionChangeListener);
        recyclerView.addOnScrollListener(snapOnScrollListener);
    }
}
