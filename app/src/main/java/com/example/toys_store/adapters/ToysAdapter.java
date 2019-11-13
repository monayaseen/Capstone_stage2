package com.example.toys_store.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toys_store.R;
import com.example.toys_store.data.Toy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToysAdapter extends RecyclerView.Adapter<ToysAdapter.ViewHolder>{

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.book_name)
        TextView name;

        private ViewHolder(View itemView) {
            super(itemView);
            itemView.setTag(this);
            itemView.setOnClickListener(onToysClickListener);
            ButterKnife.bind(this, itemView);
        }
    }

    private List< Toy > toys;
    private View.OnClickListener onToysClickListener;
    private Context context;

    // Pass in the tasks array into the constructor
    public ToysAdapter(List<Toy> toys , Context context) {
        this.toys = toys;
        this.context = context;
    }

    public void setToys(List<Toy> toys){
        this.toys = toys;
    }

    public List<Toy> getToys(){
        return toys;
    }

    public void setItemClickListener(View.OnClickListener clickListener) {
        onToysClickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View TaskView = inflater.inflate(R.layout.book_card, parent, false);

        // Return a new holder instance
        return new ViewHolder(TaskView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Toy e = toys.get(position);
        holder.name.setText(e.getName());
    }

    @Override
    public int getItemCount() {
        return toys.size();
    }
}