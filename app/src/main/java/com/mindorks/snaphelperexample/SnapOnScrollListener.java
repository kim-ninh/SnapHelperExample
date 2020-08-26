package com.mindorks.snaphelperexample;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mindorks.snaphelperexample.utils.SnapHelperExt;

public class SnapOnScrollListener extends RecyclerView.OnScrollListener {
    private String TAG = SnapOnScrollListener.class.getSimpleName();
    public enum Behaviour {
        NOTIFY_ON_SCROLL,
        NOTIFY_ON_SCROLL_STATE_IDLE
    }

    private int snapPosition = RecyclerView.NO_POSITION;

    private SnapHelper snapHelper;
    private Behaviour behaviour = Behaviour.NOTIFY_ON_SCROLL;
    private OnSnapPositionChangeListener onSnapPositionChangeListener;

    public SnapOnScrollListener(
            SnapHelper snapHelper,
            Behaviour behaviour,
            OnSnapPositionChangeListener onSnapPositionChangeListener) {

        this.snapHelper = snapHelper;
        this.behaviour = behaviour;
        this.onSnapPositionChangeListener = onSnapPositionChangeListener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        Log.d(TAG, "onScrolled: ");
        if (behaviour == Behaviour.NOTIFY_ON_SCROLL) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView,
                                     int newState) {
        Log.d(TAG, "onScrollStateChanged: ");
        if (behaviour == Behaviour.NOTIFY_ON_SCROLL_STATE_IDLE
                && newState == RecyclerView.SCROLL_STATE_IDLE) {
            maybeNotifySnapPositionChange(recyclerView);
        }
    }

    private void maybeNotifySnapPositionChange(RecyclerView recyclerView) {
        int snapPosition =
                SnapHelperExt.getSnapPosition(snapHelper, recyclerView);

        boolean snapPositionChanged = this.snapPosition != snapPosition;
        if (snapPositionChanged){
            if (onSnapPositionChangeListener != null) {
                onSnapPositionChangeListener.onSnapPositionChange(snapPosition);
                this.snapPosition = snapPosition;
            }
        }
    }
}
