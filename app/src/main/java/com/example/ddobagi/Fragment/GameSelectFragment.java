package com.example.ddobagi.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ddobagi.Activity.PlayActivity;
import com.example.ddobagi.Class.Communication;
import com.example.ddobagi.Class.GameInfoSummary;
import com.example.ddobagi.Class.GameSelectAdapter;
import com.example.ddobagi.Class.OnGameItemClickListener;
import com.example.ddobagi.Class.PlayResultAdapter;
import com.example.ddobagi.Class.Quiz;
import com.example.ddobagi.Class.QuizInfoSummary;
import com.example.ddobagi.Class.QuizResult;
import com.example.ddobagi.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class GameSelectFragment extends Fragment {
    final int openCost = 5000;
    LinearLayout listLayout, difficultLayout;

    RecyclerView recyclerView;
    GameSelectAdapter adapter;

    TextView descriptText, fieldText;
    Button easyBtn, normalBtn, hardBtn;
    Button exitBtn;

    GameInfoSummary curGame;
    SharedPreferences share;

    boolean isLogin;

    public GameSelectFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_game_select, container, false);

        setComponent(rootView);

        return rootView;
    }

    private void setComponent(ViewGroup rootView){
        share = getActivity().getSharedPreferences("PREF", MODE_PRIVATE);

        listLayout = rootView.findViewById(R.id.game_select_list_layout);
        difficultLayout = rootView.findViewById(R.id.game_select_difficulty_layout);

        recyclerView = rootView.findViewById(R.id.game_select_recylerView);

        descriptText = rootView.findViewById(R.id.game_select_descript);
        fieldText = rootView.findViewById(R.id.game_select_field);

        easyBtn = rootView.findViewById(R.id.game_select_easy);
        normalBtn = rootView.findViewById(R.id.game_select_normal);
        hardBtn = rootView.findViewById(R.id.game_select_hard);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new GameSelectAdapter();

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnGameItemClickListener() {
            @Override
            public void onItemClick(GameSelectAdapter.ViewHolder holder, View view, int position) {
                GameInfoSummary item = adapter.getItem(position);

                curGame = item;
                descriptText.setText(curGame.gamedescript);
                fieldText.setText("효과 영역: " + curGame.field);
                setDifficultBtn(easyBtn, 0, curGame.openedDifficulty, position);
                setDifficultBtn(normalBtn, 1, curGame.openedDifficulty, position);
                setDifficultBtn(hardBtn, 2, curGame.openedDifficulty, position);

                listLayout.setVisibility(View.INVISIBLE);
                difficultLayout.setVisibility(View.VISIBLE);
                PlayActivity activity = (PlayActivity) getActivity();
                exitBtn = activity.getExitBtn();
                exitBtn.setText("뒤로가기");
                exitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        difficultLayout.setVisibility(View.INVISIBLE);
                        listLayout.setVisibility(View.VISIBLE);
                        curGame = null;
                        activity.setCenterText("선택 놀이");
                        exitBtn.setText("나가기");
                        exitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });
                    }
                });
                activity.setCenterText(curGame.gamename);

                //Toast.makeText(getActivity(), "아이템 선택됨: " + item.gamename, Toast.LENGTH_LONG).show();
            }
        });
        getSelectGameList();
    }

    private void getSelectGameList(){
        PlayActivity play = (PlayActivity) getActivity();
        String url;
        StringRequest request;

        if(play.isLogin()){
            isLogin = true;
            url = Communication.selectGameListLoginUrl;
            request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Communication.println("onGetSelectGameList" + response);
                            onGetSelectGameList(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(!String.valueOf(error).equals("com.android.volley.TimeoutError")){
                                if(error.networkResponse.statusCode==401) {
                                    Communication.refreshToken(getActivity());
                                }
                            }
                            else{
                                Communication.handleVolleyError(error);
                            }
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + share.getString("Access_token", ""));

                    return params;
                }
            };
        }
        else{
            isLogin = false;
            url = Communication.selectGameListUnLoginUrl;
            request = new StringRequest(
                    Request.Method.GET,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Communication.println("onGetSelectGameList" + response);
                            onGetSelectGameList(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(!String.valueOf(error).equals("com.android.volley.TimeoutError")){
                                if(error.networkResponse.statusCode==401) {
                                    Communication.refreshToken(getActivity());
                                }
                            }
                            else{
                                Communication.handleVolleyError(error);
                            }
                        }
                    }
            );
        }

        request.setShouldCache(false);
        Communication.requestQueue.add(request);
        Communication.println("selectGameList 요청 보냄");
    }

    private void onGetSelectGameList(String response){
        Gson gson = new Gson();
        GameInfoSummary[] gameList = gson.fromJson(response, GameInfoSummary[].class);

        for(int i=0;i<gameList.length;i++){
            adapter.addItem(gameList[i]);
        }
        adapter.notifyDataSetChanged();
    }

    private void setDifficultBtn(Button button, int difficulty, int openedDifficulty, int position){
        if (difficulty <= openedDifficulty) {
            switch (difficulty){
                case 0:
                    button.setBackgroundResource(R.drawable.green_btn);
                    break;
                case 1:
                    button.setBackgroundResource(R.drawable.yellow_btn);
                    break;
                case 2:
                    button.setBackgroundResource(R.drawable.red_btn);
                    break;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(curGame != null){
                        PlayActivity activity = (PlayActivity) getActivity();
                        activity.resultPerQuizControl(4);
                        activity.onGameSelect(curGame, difficulty);
                        difficultLayout.setVisibility(View.INVISIBLE);
                        listLayout.setVisibility(View.VISIBLE);
                        exitBtn.setText("나가기");
                        exitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                activity.finish();
                            }
                        });
                        curGame = null;
                    }
                }
            });
        }
        else{
            button.setBackgroundResource(R.drawable.grey_btn);
            if(!isLogin){
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "로그인 이후 사용할 수 있습니다",Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(getActivity());
                    if(openedDifficulty == difficulty - 1){
                        dialog.setContentView(R.layout.open_difficulty_dialog);

                        TextView title, body;
                        Button yesBtn, noBtn;

                        title = dialog.findViewById(R.id.title_text);
                        title.setText(button.getText().toString() + " 난이도 개방하기");

                        body = dialog.findViewById(R.id.body_text);
                        body.setText("개방하기 위해서는 " + difficulty * openCost + "금화가 필요합니다.\n개방하시겠습니까?");

                        yesBtn = dialog.findViewById(R.id.yes_btn);
                        yesBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PlayActivity play = (PlayActivity) getActivity();
                                int curCoin = play.getCoin();
                                if(curCoin >= difficulty * openCost){
                                    String url = Communication.unlock;
                                    StringRequest request = new StringRequest(
                                            Request.Method.POST,
                                            url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    Communication.println("openDifficulty: " + response);
                                                    play.setCoin(play.getCoin() - difficulty * openCost);
                                                    setDifficultBtn(button, difficulty, openedDifficulty + 1, position);
                                                    if(difficulty == 1){
                                                        setDifficultBtn(hardBtn, 2, openedDifficulty + 1, position);
                                                    }
                                                    GameInfoSummary game = adapter.getItem(position);
                                                    game.openedDifficulty += 1;
                                                    adapter.setItem(position, game);
                                                    adapter.notifyItemChanged(position);
                                                    Toast.makeText(getActivity(), "개방되었습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    if(!String.valueOf(error).equals("com.android.volley.TimeoutError")){
                                                        if(error.networkResponse.statusCode==401) {
                                                            Communication.refreshToken(getActivity());
                                                        }
                                                        else if(error.networkResponse.statusCode==406){
                                                            Toast.makeText(play, "금화가 부족합니다(서버)", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else{
                                                        Communication.handleVolleyError(error);
                                                    }
                                                }
                                            }
                                    ) {
                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            Map<String, String> params = new HashMap<String, String>();
                                            params.put("gameID", Integer.toString(curGame.gameid));
                                            params.put("difficulty", Integer.toString(difficulty));
                                            return params;
                                        }

                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError{
                                            Map<String, String> headers = new HashMap<String, String>();
                                            headers.put("Authorization", "Bearer " + share.getString("Access_token", ""));
                                            return headers;
                                        }
                                    };
                                    request.setShouldCache(false);
                                    Communication.requestQueue.add(request);
                                    Communication.println("selectQuizList 요청 보냄");
                                }
                                else{Toast.makeText(getActivity(), "금화가 모자랍니다", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });

                        noBtn = dialog.findViewById(R.id.no_btn);
                        noBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getActivity(), "취소되었습니다", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                    else{
                        dialog.setContentView(R.layout.information_dialog);

                        TextView body = dialog.findViewById(R.id.body_text);
                        body.setText("보통 단계 난이도를 먼저 개방하셔야합니다");

                        Button btn = dialog.findViewById(R.id.ok_btn);
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                    dialog.show();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}