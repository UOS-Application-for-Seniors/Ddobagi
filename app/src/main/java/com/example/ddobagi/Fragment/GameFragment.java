package com.example.ddobagi.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class GameFragment extends Fragment {
    int gameID;
    String gameName;
    String gameField;
    String detail;
    Bitmap pictures[];
    String helpdata;

    abstract public void commit();
    abstract void onHelp();
    abstract void loadGame();
    abstract void getGameData();
    abstract public void onGetGameDataResponse(String response);
}
