package com.example.ddobagi.Class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        TextView gameType, difficulty;
        ImageView star1, star2, star3, star4, star5;
        LinearLayout layout;

        public ViewHolder(View itemView){
            super(itemView);

            layout = itemView.findViewById(R.id.record_card_layout);
            gameType = itemView.findViewById(R.id.record_game_type);
            difficulty = itemView.findViewById(R.id.record_difficulty);
            star1 = itemView.findViewById(R.id.record_blue_star1);
            star2 = itemView.findViewById(R.id.record_blue_star2);
            star3 = itemView.findViewById(R.id.record_blue_star3);
            star4 = itemView.findViewById(R.id.record_blue_star4);
            star5 = itemView.findViewById(R.id.record_blue_star5);
        }

        public void setItem(Record record){
            gameType.setText(record.gameName);
            String str;
            switch (record.difficulty){
                case 0:
                    str = "쉬움";
                    layout.setBackgroundResource(R.drawable.green_btn);
                    break;
                case 1:
                    str = "보통";
                    layout.setBackgroundResource(R.drawable.blue_btn);
                    break;
                case 2:
                    str = "어려움";
                    layout.setBackgroundResource(R.drawable.red_btn);
                    break;
                default:
                    str = "난이도 오류";
                    break;
            }
            difficulty.setText(str);

            star1.setVisibility(View.VISIBLE);
            star2.setVisibility(View.GONE);
            star3.setVisibility(View.GONE);
            star4.setVisibility(View.GONE);
            star5.setVisibility(View.GONE);

            float correctRate = record.correctRate;

            if(correctRate < 0.2){
            }
            else if(0.2 <= correctRate && correctRate < 0.4){
                star2.setVisibility(View.VISIBLE);
            }
            else if(0.4 <= correctRate && correctRate < 0.6){
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
            }
            else if(0.6 <= correctRate && correctRate < 0.8){
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
            }
            else if(0.8 <= correctRate){
                star2.setVisibility(View.VISIBLE);
                star3.setVisibility(View.VISIBLE);
                star4.setVisibility(View.VISIBLE);
                star5.setVisibility(View.VISIBLE);
            }
        }
    }
}