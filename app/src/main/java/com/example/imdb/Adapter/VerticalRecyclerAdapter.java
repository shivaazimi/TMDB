package com.example.imdb.Adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imdb.Model.Model;
import com.example.imdb.Model.ParentModel;
import com.example.imdb.R;
import com.example.imdb.Utility.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ParentModel> models;

    public VerticalRecyclerAdapter(Context context, ArrayList<ParentModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler, parent,false);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParentModel model = models.get(position);
        VerticalViewHolder viewHolder = (VerticalViewHolder) holder;
        viewHolder.topic.setText(model.getTitle());
        List<Model> cards = model.getModels();

        //RecycleView
        RecyclerView recyclerView = viewHolder.recyclerView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(gridLayoutManager);

        //RecycleViewAdapter
        HorizontalRecyclerAdapter recyclerAdapter = new HorizontalRecyclerAdapter(context, (ArrayList<Model>) cards);
        recyclerView.setAdapter(recyclerAdapter);

        //item Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //item Decoration
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(7), false));

        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return models==null ? 0 : models.size();
    }


    public int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()));

    }


    public class VerticalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView topic;

        public VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycler);
            topic = (TextView) itemView.findViewById(R.id.topic);
        }
    }

}


