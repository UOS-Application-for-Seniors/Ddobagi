package com.example.ddobagi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {
    Bitmap bitmap;
    private OnPostDownLoadListener onPostDownLoad;

    public interface OnPostDownLoadListener {
        void onPost(Bitmap bitmap);
    }

    // 리스너 세팅
    public LoadImage(OnPostDownLoadListener paramOnPostDownLad) {
        onPostDownLoad = paramOnPostDownLad;
    }

    @Override
    public Bitmap doInBackground(String... args) {
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void onPostExecute(Bitmap image) {
        super.onPostExecute(image);
        if(onPostDownLoad != null){
            onPostDownLoad.onPost(image);
        }
    }
}