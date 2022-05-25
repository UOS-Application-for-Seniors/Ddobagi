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
        TextView leftText, centerText, rightText, coinText;
        //ImageView coinImg;

        public ViewHolder(View itemView){
            super(itemView);

            leftText = itemView.findViewById(R.id.play_result_card_left_text);
            centerText = itemView.findViewById(R.id.play_result_card_center_text);
            rightText = itemView.findViewById(R.id.play_result_card_right_text);
            //coinImg = itemView.findViewById(R.id.play_result_card_coin_img);
            coinText = itemView.findViewById(R.id.play_result_card_coin_text);
        }

        public void setItem(QuizResult quizResult){
            leftText.setText(quizResult.quizIndex + ". ");
            centerText.setText(quizResult.quizName);
            String difficulty;
            switch (quizResult.difficulty){
                case 0:
                    difficulty = "쉬움";
                    break;
                case 1:
                    difficulty = "보통";
                    break;
                case 2:
                    difficulty = "어려움";
                    break;
                default:
                    difficulty = "";
            }
            rightText.setText(difficulty);
            coinText.setText(Integer.toString(quizResult.coin));
        }
    }
}
