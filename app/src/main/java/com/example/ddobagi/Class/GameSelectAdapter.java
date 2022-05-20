package com.example.ddobagi.Class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddobagi.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameSelectAdapter extends RecyclerView.Adapter<GameSelectAdapter.ViewHolder> implements OnGameItemClickListener {
    ArrayList<GameInfoSummary> items = new ArrayList<>();
    OnGameItemClickListener listener;

    public void setOnItemClickListener(OnGameItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onItemClick(ViewHolder holder, View view, int position) {
        if(listener!= null){
            listener.onItemClick(holder, view, position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.game_select_card, parent, false);

        return new ViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameInfoSummary item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(GameInfoSummary item){
        items.add(item);
    }

    public void setItems(ArrayList<GameInfoSummary> items){
        this.items = items;
    }

    public GameInfoSummary getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, GameInfoSummary item){
        items.set(position, item);
    }


    static public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ViewHolder(View itemView, final OnGameItemClickListener listener){
            super(itemView);
            textView = itemView.findViewById(R.id.game_select_button);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null){
                        listener.onItemClick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        public void setItem(GameInfoSummary item){
            textView.setText(item.gamename);
        }
    }
}
