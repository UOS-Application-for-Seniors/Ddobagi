package com.example.ddobagi.Class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ddobagi.R;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder>{
    ArrayList<Record> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.record_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Record item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Record item){
        items.add(item);
    }

    public void setItems(ArrayList<Record> items){
        this.items = items;
    }

    public Record getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Record item){
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView gameType, difficulty, totalYellowStar;
        ImageView star1, star1_5, star2, star2_5, star3;

        public ViewHolder(View itemView){
            super(itemView);

            gameType = itemView.findViewById(R.id.record_game_type);
            difficulty = itemView.findViewById(R.id.record_difficulty);
            totalYellowStar = itemView.findViewById(R.id.record_total_star_num);
            star1 = itemView.findViewById(R.id.record_blue_star1);
            star1_5 = itemView.findViewById(R.id.record_blue_star1_5);
            star2 = itemView.findViewById(R.id.record_blue_star2);
            star2_5 = itemView.findViewById(R.id.record_blue_star2_5);
            star3 = itemView.findViewById(R.id.record_blue_star3);
        }

        public void setItem(Record record){
            gameType.setText(record.gameName);
            String str;
            switch (record.difficulty){
                case 0:
                    str = "쉬움";
                    break;
                case 1:
                    str = "보통";
                    break;
                case 2:
                    str = "어려움";
                    break;
                default:
                    str = "난이도 오류";
                    break;
            }
            difficulty.setText(str);
            totalYellowStar.setText("X "+ Integer.toString(record.stars));

            star2.setVisibility(View.INVISIBLE);
            star2_5.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);

            float correctRate = record.correctRate;

            if(0.25 <= correctRate && correctRate < 0.5){
                star2.setVisibility(View.VISIBLE);
            }
            else if(0.5 <= correctRate && correctRate < 0.75){
                star2.setVisibility(View.VISIBLE);
                star2_5.setVisibility(View.VISIBLE);
            }
            else if(0.75 <= correctRate){
                star2.setVisibility(View.VISIBLE);
                star2_5.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
            }
        }
    }
}