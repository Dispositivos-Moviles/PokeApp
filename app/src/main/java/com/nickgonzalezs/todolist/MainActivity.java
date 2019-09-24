package com.nickgonzalezs.todolist;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.nickgonzalezs.todolist.adapters.PokeAdapter;
import com.nickgonzalezs.todolist.model.PokeResponse;
import com.nickgonzalezs.todolist.model.Pokemon;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {


    private final String TAG = getClass().getName();
    private Retrofit retrofit;
    private PokeAdapter pokeAdapter;
    private boolean canLoad;
    private int offset;
    private int limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        RecyclerView rvPokemons = findViewById(R.id.rv_pokemons);

        pokeAdapter = new PokeAdapter(this);
        rvPokemons.setAdapter(pokeAdapter);

        rvPokemons.setHasFixedSize(true);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvPokemons.setLayoutManager(gridLayoutManager);

        rvPokemons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

//                Log.i(TAG, "onScrolled: dx " + dx);

                if (dy > 0) {
                    Log.i(TAG, "onScrolled: dy " + dy);

                    int visibleItemCount = gridLayoutManager.getChildCount();
                    int totalItemCount = gridLayoutManager.getItemCount();
                    int pastVisibleCount = gridLayoutManager.findFirstVisibleItemPosition();


                    Log.i(TAG, "onScrolled: " + visibleItemCount);
                    Log.i(TAG, "onScrolled: " + totalItemCount);
                    Log.i(TAG, "onScrolled: " + pastVisibleCount);

                    if (canLoad) {
                        if ((visibleItemCount + pastVisibleCount) >= totalItemCount) {
                            Log.i(TAG, "onScrolled: Bottom");

                            offset += limit;
                            getPokemons(offset, limit);
                        }
                    }

                }
            }
        });

        offset = 0;
        limit = 20;
        canLoad = false;

        getPokemons(offset, limit);
    }

    private void getPokemons(int offset, int limit) {

        canLoad = false;

        PokeService service = retrofit.create(PokeService.class);
        Call<PokeResponse> pokeResponseCall = service.getPokemons(offset, limit);

        pokeResponseCall.enqueue(new Callback<PokeResponse>() {
            @Override
            public void onResponse(Call<PokeResponse> call, Response<PokeResponse> response) {

                Log.i(TAG, String.valueOf(call.request().url()));

                if (response.isSuccessful()) {

                    PokeResponse pokeResponse = response.body();

                    ArrayList<Pokemon> pokemons = pokeResponse.getResults();
                    //Log.i(TAG,pokemons.toString());
                    pokeAdapter.addPokemons(pokemons);
                    canLoad = true;

                } else {
                    Log.i(TAG, "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokeResponse> call, Throwable t) {

            }
        });

    }

}

