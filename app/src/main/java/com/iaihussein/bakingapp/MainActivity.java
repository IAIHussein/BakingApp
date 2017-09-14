package com.iaihussein.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_grid)
    GridView mMainGridView;
    SharedPreferences mSharedPreferences;
    List<Recipe> mRecipes;
    AsyncTask<String, String, String> mAsyncTask = new AsyncTask<String, String, String>() {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL mUrl = new URL(Variables.URL);
                HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();

                if (mHttpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    //get the Data's stream
                    InputStream in = new BufferedInputStream(mHttpURLConnection.getInputStream(), 1024);
                    ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        byteBuffer.write(buffer, 0, len);
                    }
                    // return response
                    String mResponse = byteBuffer.toString();
                    in.close();
                    mSharedPreferences.edit().putString(Variables.SP_RESPONSE, mResponse).apply();
                    mRecipes = new Gson().fromJson(mResponse, new TypeToken<List<Recipe>>() {
                    }.getType());

                }
                mHttpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        mSharedPreferences = getSharedPreferences(Variables.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        String mData = mSharedPreferences.getString(Variables.SP_RESPONSE, "");
        if (mData.isEmpty())
            mAsyncTask.execute();
        else {
            mRecipes = new Gson().fromJson(mData, new TypeToken<List<Recipe>>() {
            }.getType());

        }

        mMainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getBaseContext(), ItemListActivity.class).putExtra(Variables.SELECTED_RECIPE, mRecipes.get(i)));
            }
        });
        mMainGridView.setAdapter(new MainAdapter(getBaseContext(), mRecipes));

    }
}
