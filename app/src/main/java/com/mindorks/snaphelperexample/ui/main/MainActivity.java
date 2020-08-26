package com.mindorks.snaphelperexample.ui.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.mindorks.snaphelperexample.OnSnapPositionChangeListener;
import com.mindorks.snaphelperexample.R;
import com.mindorks.snaphelperexample.RecyclerViewExt;
import com.mindorks.snaphelperexample.SnapOnScrollListener;
import com.mindorks.snaphelperexample.data.model.App;
import com.mindorks.snaphelperexample.databinding.ActivityMainBinding;
import com.mindorks.snaphelperexample.injection.ActivityDependency;
import com.mindorks.snaphelperexample.ui.base.BaseActivity;
import com.mindorks.snaphelperexample.ui.common.StartSnapHelper;
import com.mindorks.snaphelperexample.ui.main.adapter.AppListAdapter;

import java.util.List;


public class MainActivity extends BaseActivity implements MainMvpView {

    private MainMvpPresenter<MainMvpView> mMainMvpPresenter;
    private AppListAdapter appListCenterAdapter;
    private AppListAdapter appListStartAdapter;

    public RecyclerView centerSnapRecyclerView;
    public RecyclerView startSnapRecyclerView;

    private ActivityMainBinding binding;
    private RecyclerView.LayoutManager layoutManagerCenter;
    private SnapHelper snapHelperCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        centerSnapRecyclerView = binding.centerSnapRecyclerView;
        startSnapRecyclerView = binding.startSnapRecyclerView;

        ActivityDependency.MainActivityDependency dependency = ActivityDependency.inject(this);
        mMainMvpPresenter = dependency.getMvpPresenter();
        mMainMvpPresenter.attachView(this);
        setUpRecyclerView();
        mMainMvpPresenter.getAppList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainMvpPresenter.detachView();
    }

    @Override
    public void showApps(List<App> appList) {
        appListCenterAdapter.updateList(appList);
        appListStartAdapter.updateList(appList);
    }

    private void setUpRecyclerView() {
        int itemWidth = getResources().getDimensionPixelSize(R.dimen.item_width);
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        layoutManagerCenter
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        centerSnapRecyclerView.setLayoutManager(layoutManagerCenter);
        centerSnapRecyclerView.addItemDecoration(new OffsetItemDecoration(itemWidth, itemSpacing));
        appListCenterAdapter = new AppListAdapter(this);
        centerSnapRecyclerView.setAdapter(appListCenterAdapter);
        snapHelperCenter = new LinearSnapHelper();
//        snapHelperCenter.attachToRecyclerView(centerSnapRecyclerView);
        RecyclerViewExt.attachSnapHelperWithListener(
                centerSnapRecyclerView,
                snapHelperCenter,
                SnapOnScrollListener.Behaviour.NOTIFY_ON_SCROLL,
                new OnSnapPositionChangeListener() {
                    @Override
                    public void onSnapPositionChange(int position) {
                        binding.snapPositionTextView.setText("Position: " + position);
                    }
                }
        );

        LinearLayoutManager layoutManagerStart
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        startSnapRecyclerView.setLayoutManager(layoutManagerStart);
        appListStartAdapter = new AppListAdapter(this);
        startSnapRecyclerView.setAdapter(appListStartAdapter);
        SnapHelper snapHelperStart = new StartSnapHelper();
        snapHelperStart.attachToRecyclerView(startSnapRecyclerView);

    }
}
