package com.example.imdb.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imdb.Model.Model;
import com.example.imdb.Model.ParentModel;
import com.example.imdb.R;

import java.util.ArrayList;

public class VerticalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<ParentModel> models;

    VerticalRecyclerAdapter(Context context,ArrayList<ParentModel> models){
        this.context=context;
        this.models=models;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler, parent);
        return new VerticalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParentModel model=models.get(position);
        VerticalViewHolder viewHolder=(VerticalViewHolder) holder;
        viewHolder.topic.setText(model.getTitle());
        String topic=model.getTitle();
        ArrayList<Model> cards=model.getModels();
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        TextView topic;
        public VerticalViewHolder(View itemView) {
            super(itemView);
            recyclerView=(RecyclerView) itemView.findViewById(R.id.horizontal_recycler);
            topic=(TextView)itemView.findViewById(R.id.topic);
        }
    }

}


