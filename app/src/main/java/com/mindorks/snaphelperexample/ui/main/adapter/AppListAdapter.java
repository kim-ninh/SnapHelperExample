package com.mindorks.snaphelperexample.ui.main.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.snaphelperexample.R;
import com.mindorks.snaphelperexample.data.model.App;
import com.mindorks.snaphelperexample.databinding.ItemAppBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by amitshekhar on 14/01/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private List<App> appList;
    private Context context;

    public AppListAdapter(Context context) {
        this.context = context;
        appList = new ArrayList<>();
    }

    public void updateList(List<App> appList) {
        this.appList = appList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewName;

        public ViewHolder(View itemView) {
            super(itemView);

            ItemAppBinding binding = ItemAppBinding.bind(itemView);
            imageViewIcon = binding.imageViewIcon;
            textViewName = binding.textViewName;
        }

        public void onBind(final int position) {
            final App app = appList.get(position);
            textViewName.setText(app.name);
            Glide.with(context)
                    .load(app.drawable)
                    .into(imageViewIcon);
        }
    }

}
