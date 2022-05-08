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

public class PlayResultAdapter extends RecyclerView.Adapter<PlayResultAdapter.ViewHolder>{
    ArrayList<QuizResult> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.play_result_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QuizResult item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(QuizResult item){
        items.add(item);
    }

    public void setItems(ArrayList<QuizResult> items){
        this.items = items;
    }

    public QuizResult getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, QuizResult item){
        items.set(position, item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView leftText, rightText;
        ImageView star1, star2, star3;

        public ViewHolder(View itemView){
            super(itemView);

            leftText = itemView.findViewById(R.id.play_result_card_left_text);
            rightText = itemView.findViewById(R.id.play_result_card_right_text);
            star1 = itemView.findViewById(R.id.play_result_star1);
            star2 = itemView.findViewById(R.id.play_result_star2);
            star3 = itemView.findViewById(R.id.play_result_star3);
        }

        public void setItem(QuizResult quizResult){
            leftText.setText(quizResult.quizIndex + ". ");
            rightText.setText(quizResult.quizBrief);
            star1.setVisibility(View.INVISIBLE);
            star2.setVisibility(View.INVISIBLE);
            star3.setVisibility(View.INVISIBLE);

            switch (quizResult.score){
                case 0:
                    star3.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    star1.setVisibility(View.VISIBLE);
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    star2.setVisibility(View.VISIBLE);
                    star3.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
